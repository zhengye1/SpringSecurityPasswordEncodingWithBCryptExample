package com.vincent.springsecurity.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vincent.springsecurity.model.User;
import com.vincent.springsecurity.model.UserProfile;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String ssoId) throws UsernameNotFoundException{
		User user = userService.findBySso(ssoId);
		logger.info("User: "+user);
		if (user==null){
			logger.error("User not found");
			throw new UsernameNotFoundException("Username not found");
		}
		return new org.springframework.security.core.userdetails.User(user.getSsoId(), user.getPassword(),
				                                                      user.getState().equals("Active"), true, true, true, getGrantedAuthorities(user));
	}
	private List<GrantedAuthority> getGrantedAuthorities(User user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (UserProfile userProfile : user.getUserProfiles()){
			logger.info("User Profile: "  +userProfile);
			authorities.add(new SimpleGrantedAuthority("Role_" + userProfile.getType()));
		}
		logger.info("authorities: " + authorities);
		return authorities;
	}


}
