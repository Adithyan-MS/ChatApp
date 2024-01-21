package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Exception.RoomNotFoundException;
import com.thinkpalm.ChatApplication.Exception.UserNotFoundException;
import com.thinkpalm.ChatApplication.Model.RoomAction;
import com.thinkpalm.ChatApplication.Repository.ParticipantModelRepository;
import com.thinkpalm.ChatApplication.Util.AppContext;
import com.thinkpalm.ChatApplication.Model.RoomModel;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.RoomRepository;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ImageService {

    @Value("${spring.servlet.multipart.location}")
    private String uploadDirectory;

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;
    private final ParticipantModelRepository participantModelRepository;

    @Autowired
    public ImageService(UserRepository userRepository, RoomRepository roomRepository, RoomService roomService, ParticipantModelRepository participantModelRepository){
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.roomService = roomService;
        this.participantModelRepository = participantModelRepository;
    }

    public String uploadPicture(MultipartFile multipartFile) throws IOException {
        if(!multipartFile.isEmpty()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String formattedDateTime = dateFormat.format(new Date());
            UserModel user = userRepository.findByName(AppContext.getUserName()).orElse(null);
            if (user != null) {
                String filePath = uploadDirectory + "/user_" + user.getId() + "/";
                try {
                    File directory = new File(filePath);
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    String fileName = formattedDateTime+"_"+multipartFile.getOriginalFilename();
                    Path path = Paths.get(filePath, fileName);
                    byte[] bytes = multipartFile.getBytes();
                    Files.write(path, bytes);

                    user.setProfilePic(fileName);
                    userRepository.save(user);

                    return fileName;
                } catch (IOException e) {
                    throw new IOException();
                }
            }else{
                throw new UserNotFoundException("User Not Found!");
            }
        }else{
            throw new FileNotFoundException();
        }
    }

    public String uploadPicture(Integer roomId,MultipartFile multipartFile) throws IllegalAccessException, IOException {
        UserModel currentUser = userRepository.findByName(AppContext.getUserName()).orElse(null);
        if(participantModelRepository.isUserActiveAdmin(roomId,currentUser.getId()).orElse(false)){
            if(!multipartFile.isEmpty()){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String formattedDateTime = dateFormat.format(new Date());
                RoomModel room = roomRepository.findById(roomId).orElse(null);
                if (room != null) {
                    String filePath = uploadDirectory + "/room_" + room.getId() + "/";
                    try {
                        File directory = new File(filePath);
                        if (!directory.exists()) {
                            directory.mkdirs();
                        }
                        String fileName = formattedDateTime+"_"+multipartFile.getOriginalFilename();
                        Path path = Paths.get(filePath, fileName);
                        byte[] bytes = multipartFile.getBytes();
                        Files.write(path, bytes);

                        room.setRoom_pic(fileName);
                        roomRepository.save(room);

                        this.roomService.setRoomLog(room,currentUser, RoomAction.changeRoomPicture);
                        this.roomService.setRoomEvent(room,"changed room picture");

                        return fileName;
                    } catch (IOException e) {
                        throw new IOException();
                    }
                }else{
                    throw new RoomNotFoundException("Room Not Found!");
                }
            }else{
                throw new FileNotFoundException();
            }
        }else{
            throw new IllegalAccessException("User is not an Admin!");
        }
    }
    public byte[] viewImage(String filename,String name) throws IOException {
        String filePath = uploadDirectory +"/" + name + "/"+ filename;
        Path path = Paths.get(filePath);
        byte[] imageBytes = Files.readAllBytes(path);
        return imageBytes;
    }
}
