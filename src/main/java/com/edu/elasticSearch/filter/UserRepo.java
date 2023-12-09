package com.edu.elasticSearch.filter;

import org.springframework.stereotype.Component;

@Component
public class UserRepo {

	public String findByUsername(String username) {
		if (username.equals("pdhongade")) {
			return "pdhongade";
		}
		return null;
	}

	public boolean validatePassword(String password) {
		if (password.equals("hello@123")) {
			return true;
		}
		return false;
	}
}
