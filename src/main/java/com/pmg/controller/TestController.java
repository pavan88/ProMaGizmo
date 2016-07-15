package com.pmg.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


public class TestController {


	@Autowired
	ReloadableResourceBundleMessageSource messageSource;
	// MessageSource messageSource;

	private static final Logger logger = LoggerFactory.getLogger(TestController.class);

	@Value("${name}")
	private String keystr;

	
	@RequestMapping(name = "/propfile", method = RequestMethod.GET)
	@ResponseBody
	public String test2() {

		System.out.println("Message Source Value:" + messageSource);
		System.out.println("Value:::::" + messageSource.getMessage("name", null, null));

		System.out.println("KeyStr:::" + keystr);

		return "KetSTR Value:" + keystr + "::::Reloadable Message Resource"
				+ messageSource.getMessage("name", null, null);

	}

}
