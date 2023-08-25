package com.ixigo.ui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ixigo.ui.config.IxigoEndPoints;

@Controller
public class MainController {
	@Value("classpath:static/static/**")
	private Resource[] resources;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private IxigoEndPoints endPoints;

	@GetMapping(
	value = { 
		"/",
		"/demfiles",
		"/demfiles/**",
		"/teams",
		"/teams/**",
		"/stats", 
		"/stats/**",
		"/rcon",
		"/rcon/**",
		"/discordbot",
		"/discordbot/**",
		"/joinus",
		"/joinus/**",
	})
	public String ui(HttpServletRequest request, Model model) {
		model.addAttribute("js", getJsName());
		model.addAttribute("css", getCssName());

		try {
			String jsonString = objectMapper.writeValueAsString(endPoints.getEndPoints());
			model.addAttribute("endPoints", jsonString);
			model.addAttribute("srvContextPath", request.getContextPath() + Forward.REQUEST_MAPPING);
			model.addAttribute("uiContextPath", request.getContextPath());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "ui";
	}

	private String getJsName() {
		for (final Resource res : resources) {
			String fileName = res.getFilename();
			if (fileName.startsWith("main.") && fileName.endsWith(".js")) {
				return fileName;
			}
		}

		return "";
	}

	private String getCssName() {
		for (final Resource res : resources) {
			String fileName = res.getFilename();
			if (fileName.startsWith("main.") && fileName.endsWith(".css")) {
				return fileName;
			}
		}

		return "";
	}
}
