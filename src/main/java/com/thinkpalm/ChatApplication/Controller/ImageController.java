package com.thinkpalm.ChatApplication.Controller;

import com.thinkpalm.ChatApplication.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;

@RestController
@RequestMapping("chatApi/v1/image")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService){
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProfilePic(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return new ResponseEntity<>(imageService.uploadPicture(multipartFile), HttpStatus.OK);
    }
    @PostMapping("/upload/{roomId}")
    public ResponseEntity<String> uploadProfilePic(@PathVariable Integer roomId,@RequestParam("file") MultipartFile multipartFile){
        return new ResponseEntity<>(imageService.uploadPicture(roomId,multipartFile), HttpStatus.OK);
    }
    @GetMapping("/view/{filename}")
    public ResponseEntity<byte[]> viewImage(@PathVariable String filename) throws IOException {
        String contentType = determineContentType(filename);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(imageService.viewImage(filename));
    }

    private String determineContentType(String filename) {
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filename.endsWith(".png")) {
            return "image/png";
        } else if (filename.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "application/octet-stream";
        }
    }

}
