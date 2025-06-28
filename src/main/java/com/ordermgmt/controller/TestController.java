package com.ordermgmt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

	@PostMapping("/pushOrderNotif")
	public ResponseEntity<String> pushOrderNotif() {
		try {
			Thread.sleep(5000);
			return new ResponseEntity<>("Notif Sent", HttpStatus.OK);

		} catch (InterruptedException e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error in sending Notif", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
