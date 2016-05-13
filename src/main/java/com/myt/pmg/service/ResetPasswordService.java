package com.myt.pmg.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordService implements UserDetailsService {

	@Autowired
	private UserService userService;

	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		if (email == null)
			return null;
		com.myt.pmg.model.User user = userService.findByEmail(email);
		System.out.println("Old User" + user.getEmail() + "Password" + user.getPassword());
		user.setPassword("admin");
		user.setActive(false);
		userService.updateUser(user);
		System.out.println("Updated User" + user.getEmail() + "Password" + user.getPassword());

		if (user == null)
			throw new UsernameNotFoundException("Oops!");

		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		/*
		 * for (String role : user.getRole()) { authorities.add(new
		 * SimpleGrantedAuthority(role)); }
		 */
		user.setRole("ROLE_USER");
		authorities.add(new SimpleGrantedAuthority(user.getRole()));

		return new User(user.getEmail(), user.getPassword(), user.isActive(), !user.isBanned(), true, true,
				authorities);

	}

}
