package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Model.*;
import com.thinkpalm.ChatApplication.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class MessageService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageReceiverRepository messageReceiverRepository;
    private final RoomRepository roomRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final LikeRepository likeRepository;
    private final MessageHistoryRepository messageHistoryRepository;
    private final DeletedMessageRepository deletedMessageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserRepository userRepository, MessageReceiverRepository messageReceiverRepository, RoomRepository roomRepository, MessageRoomRepository messageRoomRepository, LikeRepository likeRepository, MessageHistoryRepository messageHistoryRepository, DeletedMessageRepository deletedMessageRepository){
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messageReceiverRepository = messageReceiverRepository;
        this.roomRepository = roomRepository;
        this.messageRoomRepository = messageRoomRepository;
        this.likeRepository = likeRepository;
        this.messageHistoryRepository = messageHistoryRepository;
        this.deletedMessageRepository = deletedMessageRepository;
    }

    public String sendMessage(MessageSendRequest messageSendRequest){
        Message message = messageSendRequest.getMessage();
        Receiver receiver = messageSendRequest.getReceiver();
        if("user".equals(receiver.getType())){
            UserModel receiverUser = userRepository.findByName(receiver.getName()).orElse(null);
            if(receiverUser != null){
                MessageReceiverModel messageReceiverModel = new MessageReceiverModel();
                messageReceiverModel.setReceiver(receiverUser);
                messageReceiverModel.setMessage(saveMessage(message));
                messageReceiverModel.setReceived_at(Timestamp.valueOf(LocalDateTime.now()));
                messageReceiverRepository.save(messageReceiverModel);

                return "message send successfully";

            }else{
                return "receiver not found!";
            }
        }
        else if("room".equals(receiver.getType())){
            RoomModel receiverRoom = roomRepository.findByName(receiver.getName());
            if(receiverRoom != null){
                MessageRoomModel messageRoomModel = new MessageRoomModel();
                messageRoomModel.setRoom(receiverRoom);
                messageRoomModel.setMessage(saveMessage(message));
                messageRoomModel.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));
                messageRoomRepository.save(messageRoomModel);

                return "message send successfully";

            }else{
                return "room not found!";
            }
        }else{
            return "invalid receiverType!";
        }
    }
    public MessageModel saveMessage(Message message){
        Optional<UserModel> sender = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
        MessageModel messageModel = new MessageModel();
        messageModel.setContent(message.getContent());
        messageModel.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));
        messageModel.setSender(sender.get());
        if(message.getParentMessage() != null){
            MessageModel parentMessage = messageRepository.findById(message.getParentMessage()).orElse(null);
            if(parentMessage != null){
                messageModel.setParent_message(parentMessage);
            }
        }
        messageRepository.save(messageModel);
        return messageModel;
    }

    public String forwardMessage(MessageForwardRequest messageForwardRequest){
        UserModel currentUser = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        for(Integer messageId : messageForwardRequest.getMessageIds()){
            MessageModel originalMessage = messageRepository.findById(messageId).orElse(null);
            if(originalMessage != null){
                MessageModel newMessage = new MessageModel();
                newMessage.setContent(originalMessage.getContent());
                newMessage.setSender(currentUser);
                newMessage.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));
                messageRepository.save(newMessage);
                for(Receiver receiver : messageForwardRequest.getReceivers()){
                    if("user".equals(receiver.getType())){
                        UserModel receiverUser = userRepository.findByName(receiver.getName()).orElse(null);
                        if(receiverUser != null){
                            MessageReceiverModel messageReceiver = new MessageReceiverModel();
                            messageReceiver.setReceiver(receiverUser);
                            messageReceiver.setMessage(newMessage);
                            messageReceiver.setReceived_at(Timestamp.valueOf(LocalDateTime.now()));
                            messageReceiverRepository.save(messageReceiver);
                        }
                    }else if("room".equals(receiver.getType())){
                        RoomModel receiverRoom = roomRepository.findByName(receiver.getName());
                        if(receiverRoom != null){
                            MessageRoomModel messageRoom = new MessageRoomModel();
                            messageRoom.setRoom(receiverRoom);
                            messageRoom.setMessage(newMessage);
                            messageRoom.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));
                            messageRoomRepository.save(messageRoom);
                        }
                    }
                }
            }
        }
        return "message forwarded successfully";
    }

    public String editMessage(EditRequest editRequest){
        MessageModel originalMessage = messageRepository.findById(editRequest.getMessageId()).orElse(null);
        UserModel currentUser = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        if(originalMessage!=null && currentUser!=null){
            if (Objects.equals(originalMessage.getSender().getId(), currentUser.getId())){
                MessageHistoryModel messageHistoryModel = new MessageHistoryModel();
                messageHistoryModel.setMessage(originalMessage);
                messageHistoryModel.setEdited_content(originalMessage.getContent());
                messageHistoryModel.setUser(currentUser);
                messageHistoryModel.setEdited_at(Timestamp.valueOf(LocalDateTime.now()));
                messageHistoryRepository.save(messageHistoryModel);

                originalMessage.setContent(editRequest.getNewContent());
                messageRepository.save(originalMessage);
                return "message edited successfully!";
            }else{
                return "this user can't edit this message";
            }
        }
        return null;
    }

    public String deleteMessage(List<Integer> messageIds) {
        UserModel currentUser = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        String response = "";
        for (Integer messageId : messageIds){
            MessageModel message = messageRepository.findById(messageId).orElse(null);
            if (message != null) {
                DeletedMessageModel deletedMessage = new DeletedMessageModel();
                deletedMessage.setMessage(message);
                deletedMessage.setUser(currentUser);
                deletedMessage.setDeleted_at(Timestamp.valueOf(LocalDateTime.now()));
                deletedMessageRepository.save(deletedMessage);
                response = response+ "\nMessage " + messageId + " deleted.";
            } else {
                response = response+ "\nInvalid messageId '" + messageId + "'!";
            }
        }
        return response;
    }

    public List<Map<String,Object>> getUserChatMessages(String otherUser){
        UserModel otherUserData = userRepository.findByName(otherUser).orElse(null);
        UserModel currentUser = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        if(otherUserData != null){
            List<Map<String,Object>> messages = messageReceiverRepository.getAllUserChatMessages(currentUser.getId(), otherUserData.getId());
            return messages;
        }
        else{
            return null;
        }
    }

    public List<Map<String,Object>> getRoomChatMessages(String room){
        UserModel currentUser = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        RoomModel roomData = roomRepository.findByName(room);
        if(roomData != null){
            return messageRoomRepository.getAllRoomChatMessages(roomData.getId(),currentUser.getId());
        }else {
            return null;
        }
    }

    public String likeOrDislikeMessage(Integer messageId){
        UserModel currentUser = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        MessageModel message = messageRepository.findById(messageId).orElse(null);

        if(likeRepository.checkAlreadyLiked(currentUser.getId(), messageId)==0){
            LikeModel likeMessageModel = new LikeModel();
            likeMessageModel.setUser(currentUser);
            likeMessageModel.setMessage(message);
            likeMessageModel.setLiked_at(Timestamp.valueOf(LocalDateTime.now()));
            likeRepository.save(likeMessageModel);
            updateLikeCount(message);
            return "message liked";
        }else{
            likeRepository.deleteLiked(currentUser.getId(),messageId);
            updateLikeCount(message);
            return "message unliked";
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

}
