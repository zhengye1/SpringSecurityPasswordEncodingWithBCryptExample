package com.vincent.springsecurity.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vincent.springsecurity.model.User;
import com.vincent.springsecurity.model.UserProfile;
import com.vincent.springsecurity.service.UserProfileService;
import com.vincent.springsecurity.service.UserService;

@Controller
public class HelloWorldController {

	@Autowired
	UserProfileService userProfileService;
	
	@Autowired
	UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(HelloWorldController.class);
	@RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
	public String homePage(ModelMap model){
		model.addAttribute("greeting", "Hi, Welcome to my site");
		return "welcome";
	}
	
	@RequestMapping(value="/admin", method = RequestMethod.GET)
	public String adminPage(ModelMap model){
		String username = getPrincipal();
		logger.info("Username in line 47 in Controller.java: " + username);
		model.addAttribute("user", username);
		return "admin";
	}

	private String getPrincipal() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails){
			userName = ((UserDetails)principal).getUsername();
		}
		else{
			userName = principal.toString();
		}
		return userName;
	}
	
	@RequestMapping(value="/db", method = RequestMethod.GET)
	public String dbPage(ModelMap model){
		model.addAttribute("user", getPrincipal());
		return "dba";
	}
	
	@RequestMapping(value="/403", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model){
		model.addAttribute("user", getPrincipal());
		return "403";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String loginPage(){
		return "login";
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}
	
	@RequestMapping(value="/newUser", method = RequestMethod.GET)
	public String newRegistration(ModelMap model){
		User user = new User();
		model.addAttribute("user", user);
		return "newuser";
	}
	
	/*
	 * This method will be called on form submission, handing POST request
	 * It also validates the user input
	 */
	@RequestMapping(value = "/newUser", method = RequestMethod.POST)
	public String saveRegistration(@Valid User user, BindingResult result, ModelMap model){
		if (result.hasErrors()){
			logger.error("There are errors");
			return "newuser";
		}
		userService.save(user);
		logger.info("First Name: " + user.getFirstName());
        logger.info("Last Name : "+user.getLastName());
        logger.info("SSO ID : "+user.getSsoId());
        logger.info("Password : "+user.getPassword());
        logger.info("Email : "+user.getEmail());
        logger.info("Checking UsrProfiles....");
        if (user.getUserProfiles() != null){
        	for (UserProfile profile : user.getUserProfiles()){
        		logger.info("Profile: "+ profile.getType());
        	}
        }
        model.addAttribute("success", "User " + user.getFirstName() + " has been registered successfully");
        return "registrationsuccess";
	}
	
	@ModelAttribute("roles")
	public List<UserProfile> initializeProfiles(){
		return userProfileService.findAll();
	}
}
