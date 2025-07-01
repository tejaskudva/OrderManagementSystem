package com.ordermgmt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordermgmt.helpers.RestTemplateWrapper;
import com.ordermgmt.models.dto.ExternalServiceWrapperResponse;

@RestController
@RequestMapping("/test")
public class TestController {

	private static final Logger logger = LoggerFactory.getLogger(TestController.class);

	@Autowired
	private RestTemplateWrapper apiWrapper;

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

	@GetMapping("/test")
	public ResponseEntity<String> test() {
		try {

			ExternalServiceWrapperResponse response = apiWrapper.externalServiceWrapper("userinfo", null,
					String.valueOf(1234), null);

			if (!response.getHttpCode().is2xxSuccessful()) {
				throw new Exception("API Failure");
			}
			return new ResponseEntity<>("Test Success", HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error in test: ", e);
			return new ResponseEntity<>("Error in sending Notif", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
