package com.marco.ixigo.ui.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marco.ixigo.ui.config.Urls;

@Controller
@CrossOrigin("*")
public class Main {

    @Autowired
    private Urls urls;

    @RequestMapping
    public String landingPage(Model model) {
        model.addAttribute("URLS", capitaliseAllKeys());
        return "index";
    }

    private Map<String, Map<String, String>> capitaliseAllKeys() {
        Map<String, Map<String, String>> map = new HashMap<>();
        urls.getUrls().forEach((k, v) -> {
            Map<String, String> inner = new HashMap<>();

            v.forEach((k2, v2) -> inner.put(k2.toUpperCase(), v2));
            map.put(k.toUpperCase(), inner);
        });

        return map;
    }
}
