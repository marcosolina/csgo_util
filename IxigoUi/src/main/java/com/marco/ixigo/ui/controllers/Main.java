package com.marco.ixigo.ui.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marco.ixigo.ui.config.Urls;
import com.marco.ixigo.ui.model.rest.RconCard;
import com.marco.ixigo.ui.model.rest.RconCardMap;

@Controller
public class Main {

    @Autowired
    private Urls urls;
    @Value("classpath:static/pictures/maps/*")
    private Resource[] resources;

    @RequestMapping
    public String landingPage(Model model) {
        model.addAttribute("URLS", capitaliseAllKeys());
        model.addAttribute("MAPS", getMapNames());
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
    
    private List<RconCard> getMapNames(){
        List<RconCard> maps = new ArrayList<>();
        for (final Resource res : resources) {
            RconCardMap rm = new RconCardMap();
            rm.setImgSrc("./pictures/maps/" + res.getFilename());
            String mapName = res.getFilename();
            mapName = mapName.substring(0, mapName.lastIndexOf('.'));
            rm.setRconCmd(String.format("map %s", mapName.replaceAll("-", "/")));
            rm.setCardDesc(mapName.split("-")[0]);
            maps.add(rm);
        }
        return maps;
    }
}
