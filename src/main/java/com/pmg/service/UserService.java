/**
 * 
 */
package com.pmg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pmg.dao.UserDao;
import com.pmg.model.User;

/**
 * @author WM87
 *
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	public User findByUsername(String username) {
		User user = userDao.findByUsername(username);
		return user;
	}

	public void createUser(User user) {
		userDao.add(user);
	}

	public void updateUser(User user) {
		userDao.update(user);
	}

}
