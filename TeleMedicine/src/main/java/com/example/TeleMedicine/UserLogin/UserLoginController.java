package com.example.TeleMedicine.UserLogin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserLoginController {
	@Autowired
	private UserLoginService userLoginService;
	
	@PostMapping("/userlogin")
	public Map<String,Object> login(@RequestBody LoginPojo loginPojo) {
		return userLoginService.login(loginPojo);
	}
}
