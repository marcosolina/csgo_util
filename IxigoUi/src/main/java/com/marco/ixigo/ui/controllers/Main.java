package com.marco.ixigo.ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marco.ixigo.ui.config.Urls;

@Controller
public class Main {
    
    @Autowired
    private Urls urls;
    
    @RequestMapping
    public String landingPage(Model model) {
        model.addAttribute("URLS", urls);
        return "index";
    }
}
