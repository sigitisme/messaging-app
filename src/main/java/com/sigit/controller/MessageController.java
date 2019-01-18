package com.sigit.controller;

import com.sigit.entity.Message;
import com.sigit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sigit Kurniawan on 1/18/2019.
 */
@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    MessageRepository messageRepository;

    @PostMapping("/send")
    public Message sendMessage(@RequestBody Message msg){

        if(msg.getDateTime() == null || msg.getDateTime().equals("")) {
            msg.setDateTime(LocalDateTime.now().toString());
        }

        messageRepository.save(msg);
        return msg;
    }

    @GetMapping("/get/message/{receiver}")
    public List<Message> getMessage(@PathVariable("receiver")String receiver){
        return messageRepository.findAllByReceiver(receiver);
    }

    @GetMapping("/get/realtime/{sender}/{receiver}")
    public List<Message> getRealtime(@PathVariable("sender") String sender, @PathVariable("receiver") String receiver){
        List<Message> list = new ArrayList<>();

        //add messages from a to b & from b to a
        list.addAll(messageRepository.findAllBySenderAndReceiver(sender, receiver));
        list.addAll(messageRepository.findAllBySenderAndReceiver(receiver, sender));

        //sorting messages by time received
        list.sort((final Message m1, final Message m2) -> m1.getDateTime().compareTo(m2.getDateTime()));

        return list;
    }
}
