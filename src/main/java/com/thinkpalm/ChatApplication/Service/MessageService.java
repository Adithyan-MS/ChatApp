package com.thinkpalm.ChatApplication.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinkpalm.ChatApplication.Util.AppContext;
import com.thinkpalm.ChatApplication.Exception.InvalidDataException;
import com.thinkpalm.ChatApplication.Exception.RoomNotFoundException;
import com.thinkpalm.ChatApplication.Exception.UserNotFoundException;
import com.thinkpalm.ChatApplication.Model.*;
import com.thinkpalm.ChatApplication.Repository.*;
import net.coobird.thumbnailator.Thumbnails;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class MessageService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageReceiverRepository messageReceiverRepository;
    private final RoomRepository roomRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final ParticipantModelRepository participantModelRepository;
    private final LikeRepository likeRepository;
    private final MessageHistoryRepository messageHistoryRepository;
    private final DeletedMessageRepository deletedMessageRepository;
    private final StarredMessageRepository starredMessageRepository;

    @Value("${spring.servlet.multipart.location}")
    private String uploadDirectory;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserRepository userRepository, MessageReceiverRepository messageReceiverRepository, RoomRepository roomRepository, MessageRoomRepository messageRoomRepository, ParticipantModelRepository participantModelRepository, LikeRepository likeRepository, MessageHistoryRepository messageHistoryRepository, DeletedMessageRepository deletedMessageRepository, StarredMessageRepository starredMessageRepository){
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messageReceiverRepository = messageReceiverRepository;
        this.roomRepository = roomRepository;
        this.messageRoomRepository = messageRoomRepository;
        this.participantModelRepository = participantModelRepository;
        this.likeRepository = likeRepository;
        this.messageHistoryRepository = messageHistoryRepository;
        this.deletedMessageRepository = deletedMessageRepository;
        this.starredMessageRepository = starredMessageRepository;
    }

    public String sendMessage(MessageSendRequest messageSendRequest) throws IllegalAccessException {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        if(currentUser!=null){
            Message message = messageSendRequest.getMessage();
            Receiver receiver = messageSendRequest.getReceiver();
            if(ReceiverType.user == receiver.getType()){
                UserModel receiverUser = userRepository.findById(receiver.getId()).orElse(null);
                if(receiverUser != null){
                    MessageReceiverModel messageReceiverModel = new MessageReceiverModel();
                    messageReceiverModel.setReceiver(receiverUser);
                    messageReceiverModel.setMessage(saveMessage(message,currentUser));
                    messageReceiverRepository.save(messageReceiverModel);
                    return "message send successfully";
                }else{
                    throw new UserNotFoundException("No receiver found with Id :"+receiver.getId());
                }
            }
            else if(ReceiverType.room == receiver.getType()){
                RoomModel receiverRoom = roomRepository.findById(receiver.getId()).orElse(null);
                if(receiverRoom != null){
                    if(participantModelRepository.existsRoomParticipant(receiverRoom.getId(), currentUser.getId())!=0){
                        MessageRoomModel messageRoomModel = new MessageRoomModel();
                        messageRoomModel.setRoom(receiverRoom);
                        messageRoomModel.setMessage(saveMessage(message,currentUser));
                        messageRoomRepository.save(messageRoomModel);
                        return "message send successfully";
                    }else{
                        throw new IllegalAccessException("You are not a room member!");
                    }
                }else{
                    throw new RoomNotFoundException("No room found with Id :"+receiver.getId()) ;
                }
            }else{
                throw new InvalidDataException("invalid receiverType!");
            }
        }else {
            throw new UserNotFoundException("User not found!");
        }
    }
    public String sendFile(MultipartFile file,String messageSendRequestText) {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        if(currentUser!=null){
            MessageSendRequest messageSendRequest = getMessageSendRequestJSON(messageSendRequestText);
            try {
                String fileName = uploadFile(file, currentUser.getId(),messageSendRequest.getMessage().getType());

                messageSendRequest.getMessage().setContent(fileName);
                sendMessage(messageSendRequest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return "file send Successfully";
        }else {
            throw new UserNotFoundException("User not found!");
        }
    }

    public MessageSendRequest getMessageSendRequestJSON(String MessageSendRequestText){
        MessageSendRequest messageSendRequest = new MessageSendRequest();
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            messageSendRequest = objectMapper.readValue(MessageSendRequestText,MessageSendRequest.class);
        }catch (IOException e){
            e.printStackTrace();
        }
        return messageSendRequest;
    }

    public MessageModel saveMessage(Message message,UserModel sender){
        MessageModel messageModel = new MessageModel();
        messageModel.setContent(message.getContent());
        messageModel.setType(message.getType());
        messageModel.setSender(sender);
        if(message.getParentMessage() != null){
            MessageModel parentMessage = messageRepository.findById(message.getParentMessage()).orElse(null);
            if(parentMessage != null){
                messageModel.setParent_messageModel(parentMessage);
            }
        }
        messageRepository.save(messageModel);
        return messageModel;
    }

    public String uploadFile(MultipartFile multipartFile,Integer userId,MessageType messageType) throws IOException {
        if(!multipartFile.isEmpty()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String formattedDateTime = dateFormat.format(new Date());
            String filePath = uploadDirectory + "/user_" + userId + "/" + messageType;
            try {
                File directory = new File(filePath);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                String fileName = formattedDateTime+"_"+multipartFile.getOriginalFilename();
                Path path = Paths.get(filePath, fileName);
                byte[] bytes = multipartFile.getBytes();
                Files.write(path, bytes);

                if(messageType == MessageType.image){
                    generateImageThumbnail(path,fileName,userId);
                } else if (messageType == MessageType.video) {
                    generateVideoThumbnail(path.toString(),fileName,userId);
                }
                return fileName;
            } catch (IOException e) {
                throw new IOException();
            }
        }else{
            throw new UserNotFoundException("User Not Found!");
        }
    }

    public void generateVideoThumbnail(String videoPath, String fileName, int userId){
        int frameNumber = 0;
        String thumbDirectoryPath = uploadDirectory + "/user_" + userId + "/thumbnail";
        File thumbDirectory = new File(thumbDirectoryPath);
        if (!thumbDirectory.exists()) {
            thumbDirectory.mkdirs();
        }
        Path thumbPath = Paths.get(thumbDirectoryPath, fileName);
        try {
            Picture picture = FrameGrab.getFrameFromFile(new File(videoPath), frameNumber);
            BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
            try {
                Thumbnails.of(bufferedImage)
                        .scale(0.5)
                        .toFile(thumbPath+".png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void generateImageThumbnail(Path path, String fileName, int userId){
        File file = new File(String.valueOf(path));
        String thumbDirectoryPath = uploadDirectory + "/user_" + userId + "/thumbnail";
        File thumbDirectory = new File(thumbDirectoryPath);
        if (!thumbDirectory.exists()) {
            thumbDirectory.mkdirs();
        }
        Path thumbPath = Paths.get(thumbDirectoryPath, fileName);
        try {
            Thumbnails.of(file)
                    .scale(0.5)
                    .toFile(thumbPath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String forwardMessage(MessageForwardRequest messageForwardRequest){
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        for(Integer messageId : messageForwardRequest.getMessageIds()){
            MessageModel originalMessage = messageRepository.findById(messageId).orElse(null);
            if(originalMessage != null){
                String originalMessageText = originalMessage.getContent();
                if (originalMessage.getType()!=MessageType.text){
                    try{
                        byte[] fileBytes = viewFile(originalMessage.getContent(),"user_"+originalMessage.getSender().getId(),originalMessage.getType().toString());
                        MultipartFile multipartFile = new BASE64DecodedMultipartFile(fileBytes,extractFileName(originalMessage.getContent()));
                        originalMessageText = uploadFile(multipartFile,currentUser.getId(),originalMessage.getType());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                MessageModel newMessage = new MessageModel();
                newMessage.setContent(originalMessageText);
                newMessage.setType(originalMessage.getType());
                newMessage.setSender(currentUser);
                messageRepository.save(newMessage);
                for(Receiver receiver : messageForwardRequest.getReceivers()){
                    if(ReceiverType.user == receiver.getType()){
                        UserModel receiverUser = userRepository.findById(receiver.getId()).orElse(null);
                        if(receiverUser != null){
                            MessageReceiverModel messageReceiver = new MessageReceiverModel();
                            messageReceiver.setReceiver(receiverUser);
                            messageReceiver.setMessage(newMessage);
                            messageReceiverRepository.save(messageReceiver);
                        }
                    }else if(ReceiverType.room == receiver.getType()){
                        RoomModel receiverRoom = roomRepository.findById(receiver.getId()).orElse(null);
                        if(receiverRoom != null){
                            if(participantModelRepository.existsRoomParticipant(receiverRoom.getId(), currentUser.getId())!=0) {
                                MessageRoomModel messageRoom = new MessageRoomModel();
                                messageRoom.setRoom(receiverRoom);
                                messageRoom.setMessage(newMessage);
                                messageRoomRepository.save(messageRoom);
                            }
                        }
                    }
                }
            }
        }
        return "message forwarded successfully";
    }

    public String editMessage(EditRequest editRequest) throws IllegalAccessException {
        MessageModel originalMessage = messageRepository.findById(editRequest.getMessageId()).orElse(null);
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        if(originalMessage!=null && currentUser!=null){
            if (Objects.equals(originalMessage.getSender().getId(), currentUser.getId())){
                MessageHistoryModel messageHistoryModel = new MessageHistoryModel();
                messageHistoryModel.setMessage(originalMessage);
                messageHistoryModel.setEdited_content(originalMessage.getContent());
                messageHistoryModel.setUser(currentUser);
                messageHistoryRepository.save(messageHistoryModel);
                originalMessage.setContent(editRequest.getNewContent());
                messageRepository.save(originalMessage);
                return "message edited successfully!";
            }else{
                throw new IllegalAccessException("this user can't edit this message");
            }
        }else{
            throw new UserNotFoundException("User or Message not Found!");
        }
    }

    public String deleteMessage(List<Integer> messageIds) {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        String response = "";
        for (Integer messageId : messageIds){
            MessageModel message = messageRepository.findById(messageId).orElse(null);
            if (message != null) {
                DeletedMessageModel deletedMessage = new DeletedMessageModel();
                deletedMessage.setMessage(message);
                deletedMessage.setUser(currentUser);
                deletedMessageRepository.save(deletedMessage);
                response = response+ "\nMessage " + messageId + " deleted.";
            } else {
                response = response+ "\nInvalid messageId '" + messageId + "'!";
            }
        }
        return response;
    }

    public List<Map<String,Object>> getUserChatMessages(Integer otherUserId){
        UserModel otherUserData = userRepository.findById(otherUserId).orElse(null);
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        if(otherUserData != null){
            List<Map<String,Object>> messages = messageReceiverRepository.getAllUserChatMessages(currentUser.getId(), otherUserData.getId());
            return messages;
        }
        else{
            throw new UserNotFoundException("No user with Id : "+otherUserId);
        }
    }

    public List<Map<String,Object>> getRoomChatMessages(Integer roomId) throws IllegalAccessException {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        RoomModel roomData = roomRepository.findById(roomId).orElse(null);
        if(roomData != null){
            return messageRoomRepository.getAllRoomMessages(roomData.getId(),currentUser.getId());
        }else {
            throw new RoomNotFoundException("Room not found!");
        }
    }

    public Integer likeOrDislikeMessage(Integer messageId){
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        MessageModel message = messageRepository.findById(messageId).orElse(null);
        if(likeRepository.checkAlreadyLiked(currentUser.getId(), messageId)==0){
            LikeModel likeMessageModel = new LikeModel();
            likeMessageModel.setUser(currentUser);
            likeMessageModel.setMessage(message);
            likeRepository.save(likeMessageModel);
            updateLikeCount(message);
            return message.getLike_count();
        }else{
            likeRepository.deleteLiked(currentUser.getId(),messageId);
            updateLikeCount(message);
            return message.getLike_count();
        }
    }

    public void updateLikeCount(MessageModel message){
        Integer count = likeRepository.getMessageLikeCount(message.getId());
        message.setLike_count(count);
        messageRepository.save(message);
    }

    public List<Map<String,Object>> getMessageLikedUsers(Integer message_id){
        return likeRepository.getMessageLikedUsers(message_id);
    }

    public List<Map<String, Object>> searchUserChatMessage(Integer otherUserId, String searchContent) {
        UserModel otherUserData = userRepository.findById(otherUserId).orElse(null);
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        if(otherUserData != null){
            List<Map<String,Object>> messages = messageReceiverRepository.searchUserChatMessages(currentUser.getId(), otherUserData.getId(),searchContent);
            return messages;
        }
        else{
            throw new UserNotFoundException("No user with Id : "+otherUserId);
        }
    }

    public List<Map<String, Object>> searchRoomChatMessage(Integer roomId, String searchContent) {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        RoomModel roomData = roomRepository.findById(roomId).orElse(null);
        if(roomData != null){
            return messageRoomRepository.searchUserChatMessages(roomData.getId(),currentUser.getId(),searchContent);
        }else {
            return null;
        }
    }
    public String starOrUnstarMessage(List<Integer> messageIds) {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        String response = "";
        for (Integer messageId : messageIds){
            MessageModel message = messageRepository.findById(messageId).orElse(null);
            if(starredMessageRepository.checkAlreadyStarred(currentUser.getId(), messageId)==0){
                StarredMessageModel starredMessageModel = new StarredMessageModel();
                starredMessageModel.setUser(currentUser);
                starredMessageModel.setMessage(message);
                starredMessageRepository.save(starredMessageModel);
                response = response + "\n" + "message " + messageId + "starred";
            }else{
                starredMessageRepository.deleteLiked(currentUser.getId(),messageId);
                response = response + "\n" + "message " + messageId + "unstarred";
            }
        }
        return response;
    }

    public List<Map<String, Object>> searchAllChats(String searchContent) {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        if(!searchContent.isEmpty()) {
            return messageRoomRepository.searchAllChats(searchContent, currentUser.getId());
        }else {
            throw  new InvalidDataException("No search content");
        }
    }

    public byte[] viewFile(String filename,String name,String messageType) throws IOException {
        String filePath = uploadDirectory +"/" + name + "/" + messageType + "/" + filename;
        Path path = Paths.get(filePath);
        byte[] imageBytes = Files.readAllBytes(path);
        return imageBytes;
    }

    private static String extractFileName(String originalFileName) {
        Pattern pattern = Pattern.compile("\\d{8}_\\d{6}_(.*)");
        Matcher matcher = pattern.matcher(originalFileName);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return originalFileName;
        }
    }

}
