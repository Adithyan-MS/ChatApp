package com.thinkpalm.ChatApplication.Service;

import com.thinkpalm.ChatApplication.Model.MessageModel;
import com.thinkpalm.ChatApplication.Model.MessageReceiverModel;
import com.thinkpalm.ChatApplication.Model.MessageRequest;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.MessageReceiverRepository;
import com.thinkpalm.ChatApplication.Repository.MessageRepository;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

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

    public String sendMessage(MessageRequest msg) {
        try{
            Optional<UserModel> sender = userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
            List receivers = msg.getTo();
            Optional<List<UserModel>> receiverList = userRepository.findByNames(receivers);
            if(sender.isPresent()&& receiverList.isPresent()){

                Date currentTime = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentTime);
                Timestamp currentTimestamp = new Timestamp(calendar.getTimeInMillis());

                MessageModel messageModel = new MessageModel();
                messageModel.setMessage_content(msg.getMessage());
                messageModel.setSender(sender.get());
                messageModel.setCreated_at(currentTimestamp);
                messageRepository.save(messageModel);

                for(UserModel user: receiverList.get()){
                    MessageReceiverModel messageReceiverModel = new MessageReceiverModel();
                    messageReceiverModel.setMessage(messageModel);
                    messageReceiverModel.setReceiver(user);
                    messageReceiverModel.setReceived_at(currentTimestamp);
                    messageReceiverRepository.save(messageReceiverModel);
                }
            }
            return "Message send successfull";
        }catch(Exception e){
            return "unsuccessfull";
        }

    }
}
