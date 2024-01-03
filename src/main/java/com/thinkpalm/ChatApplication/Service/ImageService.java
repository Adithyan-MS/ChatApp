package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Exception.InvalidDataException;
import com.thinkpalm.ChatApplication.Exception.UserNotFoundException;
import com.thinkpalm.ChatApplication.Repository.ParticipantModelRepository;
import com.thinkpalm.ChatApplication.Util.AppContext;
import com.thinkpalm.ChatApplication.Model.RoomModel;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.RoomRepository;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
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
    private final ParticipantModelRepository participantModelRepository;

    public ImageService(UserRepository userRepository, RoomRepository roomRepository, ParticipantModelRepository participantModelRepository){
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.participantModelRepository = participantModelRepository;
    }

    public String uploadPicture(MultipartFile multipartFile) throws IOException {
        if(!multipartFile.isEmpty()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String formattedDateTime = dateFormat.format(new Date());

            UserModel user = userRepository.findByName(AppContext.getUserName()).orElse(null);
            if (user != null) {
                String filePath = uploadDirectory + "/" + user.getName() + "/";
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
                    String relativePath = user.getName() + "/" + fileName;

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
        if(participantModelRepository.isUserAdmin(roomId,currentUser.getId()).orElse(false)){
            if(!multipartFile.isEmpty()){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String formattedDateTime = dateFormat.format(new Date());
                RoomModel room = roomRepository.findById(roomId).orElse(null);
                if (room != null) {
                    String filePath = uploadDirectory + "/" + room.getName() + "/";
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
                        String relativePath = room.getName() + "/" + fileName;

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
        }else{
            throw new IllegalAccessException("User is not an Admin!");
        }
    }
    public byte[] viewImage(String filename,String name) throws IOException {
        String filePath = uploadDirectory +"/" + name + "/"+ filename;
        System.out.println(filePath);
        Path path = Paths.get(filePath);
        byte[] imageBytes = Files.readAllBytes(path);
        return imageBytes;
    }
}
