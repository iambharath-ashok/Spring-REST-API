package com.guru.bharath.rest.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guru.bharath.rest.model.User;

@RestController
public class UserController {
	
	
	
	@RequestMapping(value = "/getUser", produces=MediaType.APPLICATION_XML_VALUE) 
	public User getUser() {
		return new User(1l, "bhararth","bharath@iambh.com");
	}
	
	
}
