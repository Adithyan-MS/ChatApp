package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Model.MessageModel;
import com.thinkpalm.ChatApplication.Model.MessageReceiverModel;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.MessageReceiverRepository;
import com.thinkpalm.ChatApplication.Repository.MessageRepository;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class MessageService {

    private final UserRepository userRepository;

    private final MessageRepository messageRepository;

    private final MessageReceiverRepository messageReceiverRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository,UserRepository userRepository,MessageReceiverRepository messageReceiverRepository){
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messageReceiverRepository = messageReceiverRepository;
    }

    public String sendPersonalMessage(String receiverName, Map<String,String> msg) {
        try{
            Optional<UserModel> sender = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
            Optional<UserModel> receiver = userRepository.findByName(receiverName);
            if(sender.isPresent()&& receiver.isPresent()){
                MessageModel messageModel = new MessageModel();
                messageModel.setMessage_content(msg.get("message"));
                messageModel.setSender(sender.get());

                Date currentTime = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentTime);
                Timestamp currentTimestamp = new Timestamp(calendar.getTimeInMillis());
                messageModel.setCreated_at(currentTimestamp);

                messageRepository.save(messageModel);

                MessageReceiverModel messageReceiverModel = new MessageReceiverModel();
                messageReceiverModel.setMessage(messageModel);
                messageReceiverModel.setReceiver(receiver.get());
                messageReceiverModel.setReceived_at(currentTimestamp);

                messageReceiverRepository.save(messageReceiverModel);

            }

            return "successfull";
        }catch(Exception e){
            return "unsuccessfull";
        }



    }
}
