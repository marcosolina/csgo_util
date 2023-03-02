package com.ixigo.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MainController {
	@Value("classpath:static/static/**")
    private Resource[] resources;
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private IxigoEndPoints endPoints;
	
    private Comparator<? super File> c = (f1, f2) -> f1.lastModified() < f2.lastModified() ? 1 : -1; 
    
    @GetMapping(value = { "/" })
    public String ui(Model model) {
        model.addAttribute("js", getJsName());
        model.addAttribute("css", getCssName());
        
		try {
			String jsonString = objectMapper.writeValueAsString(endPoints.getEndPoints());
			model.addAttribute("endPoints", jsonString);
			model.addAttribute("proxyBasePath", endPoints.getEndPoints().get("gateway").get("base-url"));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return "ui";
    }
    
    private String getJsName() {
        List<File> files = new ArrayList<>();
        try {
            for (final Resource res : resources) {
                String fileName = res.getFilename();
                if (fileName.startsWith("main.") && fileName.endsWith(".js")) {
                    files.add(res.getFile());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        files.sort(c);
        return files.get(0).getName();
    }
    
    private String getCssName() {
        List<File> files = new ArrayList<>();
        try {
            for (final Resource res : resources) {
                String fileName = res.getFilename();
                if (fileName.startsWith("main.") && fileName.endsWith(".css")) {
                    files.add(res.getFile());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if(files.isEmpty()) {
        	return "";
        }
        
        files.sort(c);
        return files.get(0).getName();
    }
}
