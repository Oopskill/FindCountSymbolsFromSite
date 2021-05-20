package org.example.application.controllers;

import org.example.application.entity.Message;
import org.example.application.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private MessageService messageService;

    private List<Message> m = new ArrayList<>();
    private int minCount = 1;
    private int maxCount = 34333;

    @GetMapping("/")
    public String mainPage(Model model){
        model.addAttribute("message", new Message());
        model.addAttribute("messages",m);
        model.addAttribute("maxCount",maxCount);
        model.addAttribute("minCount",minCount);
        return "/main";
    }


    @PostMapping("/")
    public String parseString(@RequestParam("url") String url){
         m = messageService.getAllRef(url);
         maxCount = messageService.getMaxCount();
         minCount = messageService.getMinCount();
        return "redirect:";
    }
}
