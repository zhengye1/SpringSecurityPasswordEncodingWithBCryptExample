package com.vincent.springsecurity.util;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class QuickPasswordEncodingGenerator {
	public static void main(String[] args){
		String password = "alice0102";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashpw = passwordEncoder.encode(password);
		System.out.println(hashpw);
	}
}
