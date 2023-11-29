package com.example.TeleMedicine.UserLogin;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoginService {
	@Autowired
	private UserLoginRepository userLoginRepository;
	private static final Logger logger=LoggerFactory.getLogger(UserLoginService.class);
	
	public Map<String,Object> login(LoginPojo loginPojo) {
		Map<String,Object> m=new HashMap<>();
		try {
			Map<String,Object> profile=userLoginRepository.login(loginPojo);
			if((boolean)profile.get("flag")==true) {
				m.put("message",profile);
				m.put("flag", true);
				m.put("status", 1);
				logger.debug(loginPojo.getUsername()+" logged in successful");
				return m;
			}
			m.put("message","Invalid User Credentials");
			m.put("flag", true);
			m.put("status", 0);
		}catch(Exception e){
			e.printStackTrace();
			m.put("message","Error while login");
			m.put("flag", false);
			m.put("status", 0);
		}
		return m;
	}
}
