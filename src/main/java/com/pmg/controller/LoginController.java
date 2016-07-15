/**
 * 
 */
package com.pmg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pmg.service.LoginService;

/**
 * @author WM87
 *
 */

@Controller
public class LoginController {

	@Autowired
	LoginService loginservice;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	public String login() {
		System.out.println("Here in login Controller");
		// loginservice.loadUserByUsername("pavan");
		return "login";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup() {
		System.out.println("Here in Signup");
		// loginservice.loadUserByUsername("pavan");
		return "signup";
	}

}
