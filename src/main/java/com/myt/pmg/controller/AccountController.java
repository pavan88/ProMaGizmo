
package com.myt.pmg.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.myt.pmg.common.UtilFunction;
import com.myt.pmg.model.Account;
import com.myt.pmg.model.User;
import com.myt.pmg.service.AccountService;
import com.myt.pmg.service.UserService;

@Controller
public class AccountController {

	static final Logger logger = Logger.getLogger(AccountController.class);

	private static String SESSION_OBJ = "SessionObj";

	@Autowired
	private UserService userService;

	@Autowired
	private AccountService accountService;

	@PreAuthorize(value = "hasRole('ROLE_USER')")
	@RequestMapping(value = "/account", method = RequestMethod.GET)
	public String accountPage(Model model, Principal principal, HttpServletRequest request, HttpSession session) {
		User user = userService.findByEmail(principal.getName());
		if (session.getAttribute(SESSION_OBJ) == null) {
			UtilFunction.setCurrentUser(session, user);
		}

		// Account account = accountService.findByUserId(user.getId());
		Account account = accountService.findByUserId(user.getEmail());

		model.addAttribute("account", account);
		model.addAttribute("user", user);

		return "account";
	}

	@PreAuthorize(value = "hasRole('ROLE_USER')")
	@RequestMapping(value = "/accountsetup", method = RequestMethod.POST)
	public String accountSetup(Model model, Principal principal, HttpServletRequest request, HttpSession session,
			@ModelAttribute("account") Account account) {
		User user = userService.findByEmail(principal.getName());
		if (session.getAttribute(SESSION_OBJ) == null) {
			UtilFunction.setCurrentUser(session, user);
		}
		account.setUserId(user.getEmail());
		account = accountService.saveAccount(account);
		System.out.println("*******Account Created for User" + account.getLinksSubmitted());
		model.addAttribute("user", user);

		return "account";
	}

}
