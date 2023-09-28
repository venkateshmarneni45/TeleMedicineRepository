package com.example.TeleMedicine.DoctorLogin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctorlogin")
public class DoctorLoginController {
	@Autowired
	private DoctorLoginService loginService;
	
	@PostMapping(value="/otprequest",consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> otpRequest(@RequestParam String mobile) {
		return loginService.otpRequest(mobile);
	}
	
	@PostMapping(value="/otpverify",consumes = "application/json", produces = "application/json")
	public Map<String, Object> verifyOtp(@RequestBody Map<String,Object> otpVerify){
		return loginService.verifyOtp(otpVerify);
	}
}
