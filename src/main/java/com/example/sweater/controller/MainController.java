package com.example.sweater.controller;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("message", new Message());
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Model model) {
        fillModel(model);
        return "main";
    }

    private void fillModel(Model model) {
        model.addAttribute("message", new Message());
        fillModelByMessages(model);
    }

    private void fillModelByMessages(Model model) {
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
    }


    @PostMapping("/add")
    public String add(
            @AuthenticationPrincipal User user,
            Message message,
            Model model,
            @RequestParam("file") MultipartFile file) throws IOException {
        model.addAttribute("message", message);
        message.setAuthor(user);


        //Сохранение файла
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();

            //Загрузка файла файла
            file.transferTo(new File(uploadPath + "/" + resultFileName));

            message.setFilename(resultFileName);
        }

        messageRepo.save(message);
        fillModelByMessages(model);
        return "main";
    }

    @PostMapping("/filter")
    public String add(@RequestParam String filter, Model model) {
        if (filter.isEmpty()) {
            fillModelByMessages(model);
        } else {
            Iterable<Message> filteredMessage = messageRepo.findByTag(filter);
            model.addAttribute("messages", filteredMessage);
        }
        model.addAttribute("message", new Message());
        return "main";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam String messageId, Model model) {
        messageRepo.deleteById(Long.valueOf(messageId.trim()));
        fillModelByMessages(model);
        model.addAttribute("message", new Message());
        return "main";
    }

}