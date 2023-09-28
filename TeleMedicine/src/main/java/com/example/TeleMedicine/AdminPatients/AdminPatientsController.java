package com.example.TeleMedicine.AdminPatients;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adminpatients")
public class AdminPatientsController {
	@Autowired
	private AdminPatientsService AdminPatientsService;

	@PostMapping(value = "/patientslist", produces = "application/json")
	public Map<String, Object> getPatientsList() {
		return AdminPatientsService.getPatientsList();
	}
	
	@PostMapping(value = "/patientprofile", consumes="application/json",produces = "application/json")
	public Map<String, Object> getPatientProfile(@RequestBody Map<String,Object> data) {
		return AdminPatientsService.getPatientProfile((int)data.get("patientId"));
	}
}
