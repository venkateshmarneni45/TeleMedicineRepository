package com.example.TeleMedicine.AdminAppointments;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adminappointments")
public class AdminAppointmentsController {
	@Autowired
	private AdminAppointmentsService adminAppointmentsService;
	
	@PostMapping(value="/data", consumes = "application/json", produces = "application/json")
	public Map<String,Object> getAppointments(@RequestBody Map<String,Object> dept){
		return adminAppointmentsService.getAppointments((int)dept.get("category"));
	}
}
