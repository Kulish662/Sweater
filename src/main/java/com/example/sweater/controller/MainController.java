package com.example.sweater.controller;

import com.example.sweater.domain.Message;
import com.example.sweater.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Map<String, Object> model) {
        fillModelByMessages(model);
        return "main";
    }

    private void fillModelByMessages(Map<String, Object> model) {
        Iterable<Message> messages = messageRepo.findAll();
        model.put("messages", messages);
    }

    @PostMapping("/add")
    public String add(@RequestParam String text, @RequestParam String tag, Map<String, Object> model) {
        Message message = new Message(text, tag);
        messageRepo.save(message);
        fillModelByMessages(model);
        return "main";
    }

    @PostMapping("/filter")
    public String add(@RequestParam String filter, Map<String, Object> model) {
        if(filter.isEmpty()){
            fillModelByMessages(model);
        }else {
            Iterable<Message> filteredMessage = messageRepo.findByTag(filter);
            model.put("messages", filteredMessage);
        }
        return "main";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam String messageId, Map<String, Object> model) {
        messageRepo.deleteById(Long.valueOf(messageId));
        fillModelByMessages(model);
        return "main";
    }

}