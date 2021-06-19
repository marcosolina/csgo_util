package com.marco.ixigo.ui.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Main {
    @RequestMapping
    public String landingPage() {
        return "index";
    }
}
