package com.ixigo.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	@Value("classpath:static/static/**")
    private Resource[] resources;
    
    private Comparator<? super File> c = (f1, f2) -> f1.lastModified() < f2.lastModified() ? 1 : -1; 
    
    @GetMapping(value = { "/ixigoui/**" })
    public String ui(Model model, HttpServletRequest request) {
        
        model.addAttribute("js", getJsName());
        model.addAttribute("css", getCssName());
        model.addAttribute("contextPath", request.getContextPath() + "/ixigoui");

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
        
        files.sort(c);
        return files.get(0).getName();
    }
}
