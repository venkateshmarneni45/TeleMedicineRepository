package com.example.TeleMedicine.PatientDashBoard;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patientdashboard")
public class PatientDashBoardController {
	@Autowired
	private PatientDashBoardService dashBoardService;

	@PostMapping(value = "/alldata", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> getCompleteData(@RequestParam int patientId) {
		return dashBoardService.getCompleteData(patientId);
	}
}
