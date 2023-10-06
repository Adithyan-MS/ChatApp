package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Model.RoomModel;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.RoomRepository;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

@Service
public class ImageService {

    @Value("${app.upload.dir}")
    private String uploadDirectory;

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public ImageService(UserRepository userRepository,RoomRepository roomRepository){
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    public String uploadPicture(MultipartFile multipartFile) {
        String fileName = LocalDateTime.now()+"_"+multipartFile.getOriginalFilename();
        String filePath = "C:/Users/adithyan.ms/Desktop/Spring/ChatApplication/src/main/java/com/thinkpalm/ChatApplication/Images/"+fileName;
        UserModel user = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
        if(user!=null){
            try(OutputStream outputStream = new FileOutputStream(filePath)){
                outputStream.write(multipartFile.getBytes());
                user.setProfilPic(fileName);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return fileName + " uploaded";
    }

    public String uploadPicture(Integer roomId,MultipartFile multipartFile) {
        String fileName = LocalDateTime.now()+"_"+multipartFile.getOriginalFilename();
        String filePath = uploadDirectory+fileName;
        RoomModel room = roomRepository.findById(roomId).orElse(null);
        if(room!=null){
            try(OutputStream outputStream = new FileOutputStream(filePath)){
                outputStream.write(multipartFile.getBytes());
                room.setRoom_pic(fileName);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return fileName + " uploaded";
    }
}
