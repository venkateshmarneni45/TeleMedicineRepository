package com.example.TeleMedicine.DoctorSpare;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctorspare")
public class DoctorSpareController {
	@Autowired
	private DoctorSpareService doctorSpareService;

	@PostMapping("/patientprofile")
	public Map<String, Object> getPatientProfile(@RequestParam int patientId) {
		return doctorSpareService.getPatientProfile(patientId);
	}

}
