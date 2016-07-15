/**
 * 
 */
package com.pmg.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author WM87
 *
 */

@Service("loginService")
public class LoginService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("444444444444444444444444444444444");
		System.out.println("here in Login Service Class ::" + username);
		return null;
	}

}
