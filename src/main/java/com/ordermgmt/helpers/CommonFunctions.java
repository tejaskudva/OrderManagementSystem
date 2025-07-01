package com.ordermgmt.helpers;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

public class CommonFunctions {

	private static final Logger logger = LoggerFactory.getLogger(CommonFunctions.class);

	public static String getMachineHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();

		} catch (UnknownHostException e) {
			logger.error("Error in retrieving hostname: ", e);
			return "";
		}
	}

	public static HttpHeaders parseToHttpHeaders(String headerString) {
		HttpHeaders headers = new HttpHeaders();
		String[] pairs = headerString.split(",\\s*");

		for (String pair : pairs) {
			String[] kv = pair.split("=", 2);
			if (kv.length == 2) {
				headers.set(kv[0].trim(), kv[1].trim());
			}
		}

		return headers;
	}

}
