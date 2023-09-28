package com.example.TeleMedicine.CommonServices;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/spare")
public class SpareController {
	@Autowired
	private SpareService spareService;

	@PostMapping(value = "/departments", produces = "application/json")
	public Map<String, Object> getDepartments() {
		return spareService.getDepartments();
	}

	@PostMapping(value = "/medicaltests", produces = "application/json")
	public Map<String, Object> getMedicalTests() {
		return spareService.getMedicalTests();
	}

	@PostMapping(value = "/states", produces = "application/json")
	public Map<String, Object> getStates() {
		return spareService.getStates();
	}

	@PostMapping(value = "/cities", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> getCities(@RequestParam int state) {
		return spareService.getCities(state);
	}

	@PostMapping(value = "/complaints", produces = "application/json")
	public Map<String, Object> getComplaints() {
		return spareService.getComplaints();
	}

	@PostMapping(value = "/specializations", produces = "application/json")
	public Map<String, Object> getSpecializations() {
		return spareService.getSpecializations();
	}

	@PostMapping(value = "/downloadcasesheets", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> downloadCaseSheets(@RequestParam int appointmentId) {
		return spareService.downloadCaseSheets(appointmentId);
	}
	
	@PostMapping(value = "/downloadmedicalreports", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> downloadMedicalReports(@RequestParam int appointmentId) {
		return spareService.downloadMedicalReports(appointmentId);
	}
	
	@PostMapping(value = "/otprequest", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> otpRequest(@RequestParam String mobile) {
		return spareService.otpRequest(mobile);
	}

	@PostMapping(value = "/otpverify", consumes = "application/json", produces = "application/json")
	public Map<String, Object> verifyOtp(@RequestBody Map<String, Object> otpVerify) {
		return spareService.verifyOtp(otpVerify);
	}

	@PostMapping(value = "/adddoctor", consumes = "application/json", produces = "application/json")
	public Map<String, Object> registerDoctor(@RequestBody DoctorProfile doctorProfile) {
		return spareService.registerDoctor(doctorProfile);
	}
}
