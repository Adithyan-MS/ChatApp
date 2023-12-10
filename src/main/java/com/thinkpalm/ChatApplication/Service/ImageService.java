package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Util.AppContext;
import com.thinkpalm.ChatApplication.Model.RoomModel;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.RoomRepository;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

    public ImageService(UserRepository userRepository,RoomRepository roomRepository){
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    public String uploadPicture(MultipartFile multipartFile) throws IOException {
        if(!multipartFile.isEmpty()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String formattedDateTime = dateFormat.format(new Date());

            String fileName = formattedDateTime+"_"+multipartFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDirectory, fileName);

            UserModel user = userRepository.findByName(AppContext.getUserName()).orElse(null);
            if(user!=null){
                try(OutputStream outputStream = new FileOutputStream(String.valueOf(filePath))){
                    outputStream.write(multipartFile.getBytes());
                    user.setProfilePic(fileName);
                    userRepository.save(user);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return fileName;
        }else{
            return "file not found!";
        }

    }

    public String uploadPicture(Integer roomId,MultipartFile multipartFile) {
        if(!multipartFile.isEmpty()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String formattedDateTime = dateFormat.format(new Date());

            String fileName = formattedDateTime+"_"+multipartFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDirectory, fileName);
            RoomModel room = roomRepository.findById(roomId).orElse(null);
            if(room!=null){
                try(OutputStream outputStream = new FileOutputStream(String.valueOf(filePath))){
                    outputStream.write(multipartFile.getBytes());

                    room.setRoom_pic(fileName);
                    roomRepository.save(room);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return fileName;
        }else{
            return "file not found!";
        }
    }
    public byte[] viewImage(String filename) throws IOException {
        String filePath = uploadDirectory +"/"+ filename;
        Path path = Paths.get(filePath);
        byte[] imageBytes = Files.readAllBytes(path);
        return imageBytes;
    }
}
