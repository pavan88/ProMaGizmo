/**
 * 
 */
package com.pmg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pmg.model.User;
import com.pmg.service.UserService;

/**
 * @author WM87
 *
 */

@Controller
public class LoginController {

	@Autowired
	UserService userService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		System.out.println("Here in login Controller");
		// loginservice.loadUserByUsername("pavan");
		return "login";
	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashboard() {
		System.out.println("Here in dashboard");
		return "dashboard";
	}

	@PreAuthorize(value = "hasRole('ROLE_USER')")
	@RequestMapping(value = "/linkbroadcaster", method = RequestMethod.GET)
	public String linkbroadcaster() {
		System.out.println("Here in linkbroadcaster");
		return "linkbroadcaster";
	}

	@RequestMapping(value = "/newuser", method = RequestMethod.GET)
	@ResponseBody
	public String newUser() {

		User user = new User();
		user.setUsername("pavan");
		// user.setPassword("pavan");
		user.setPassword(passwordEncoder.encode("pavan"));

		userService.createUser(user);

		return "Sucess: User created Successfully";
	}

}
