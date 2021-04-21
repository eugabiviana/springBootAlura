package br.com.alura.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloControler {

	@RequestMapping("/")
	@ResponseBody //mostra o texto na página do browser
	public String hello() {
		return "Hello World!";
	}
	
	
}
