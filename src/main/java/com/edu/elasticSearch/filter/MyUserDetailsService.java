package com.edu.elasticSearch.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService {
	
	@Autowired 
	UserRepo repo;

	public String loadUserByUsername(String username) {
		
		return repo.findByUsername(username);
	}
	
	public boolean validatePassword(String password) {
		return repo.validatePassword(password);
	}
}
