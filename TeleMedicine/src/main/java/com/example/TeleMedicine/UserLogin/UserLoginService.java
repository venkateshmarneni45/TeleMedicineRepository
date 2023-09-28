package com.example.TeleMedicine.UserLogin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoginService {
	@Autowired
	private UserLoginRepository userLoginRepository;

	public Map<String, Object> login(LoginPojo loginPojo) {
		Map<String, Object> login = new HashMap<>();
		try {
			int userCount = userLoginRepository.login(loginPojo);
			if (userCount == 1) {
				login.put("message", "User Exists");
				login.put("flag", true);
				login.put("status", 1);
				return login;
			} else {
				login.put("message", "Invalid User Credentials");
				login.put("flag", true);
				login.put("status", 0);
			}

		} catch (Exception e) {
			e.printStackTrace();
			login.put("message", "Error while login");
			login.put("flag", false);
			login.put("status", 0);
		}
		return login;
	}
}
