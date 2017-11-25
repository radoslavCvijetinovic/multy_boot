package com.example.multy_boot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Profile("live")
@RestController
public class LiveController {

	@Value("${spring.profiles.active}")
	private String springProfile;
	
	@RequestMapping("/hello")
	public String hello() {
		
		return "HELLO, "+springProfile+" controller here!";
	} 
	
}
