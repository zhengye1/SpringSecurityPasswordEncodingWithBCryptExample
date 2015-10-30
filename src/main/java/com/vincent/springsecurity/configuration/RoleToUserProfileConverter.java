package com.vincent.springsecurity.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.vincent.springsecurity.model.UserProfile;
import com.vincent.springsecurity.service.UserProfileService;

@Component
public class RoleToUserProfileConverter implements Converter<Object, UserProfile>{

	@Autowired
	UserProfileService userProfileService;

	private final Logger logger = LoggerFactory.getLogger(RoleToUserProfileConverter.class);
	/*
	 * Gets UserProfile by Id
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	public UserProfile convert(Object element){
		Integer id = Integer.parseInt((String)element);
		UserProfile profile = userProfileService.findById(id);
		logger.info("Profile: " + profile);
		return profile;
	}

	/*
	 * Gets UserProfile by type
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	/*
	public UserProfile convert(Object element) {
		String type = (String)element;
		UserProfile profile= userProfileService.findByType(type);
		logger.info("Profile ... : "+profile);
		return profile;
	}
	 */
}
