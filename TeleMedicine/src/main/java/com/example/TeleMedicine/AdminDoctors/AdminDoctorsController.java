package com.example.TeleMedicine.AdminDoctors;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admindoctors")
public class AdminDoctorsController {
	@Autowired
	private AdminDoctorsService adminDoctorsService;

	@PostMapping(value = "/doctorslist", produces = "application/json")
	public Map<String, Object> getDoctorsList(@RequestBody Map<String,Object> list) {
		return adminDoctorsService.getDoctorsList(list);
	}

	@PostMapping(value = "/doctorprofile", consumes = "application/json", produces = "application/json")
	public Map<String, Object> getDoctorProfile(@RequestBody Map<String,Object> map) {
		return adminDoctorsService.getDoctorProfile((int)map.get("doctorId"));
	}

	@PostMapping(value = "/updateprofile", consumes = "application/json", produces = "application/json")
	public Map<String, Object> profileUpdate(@RequestBody Map<String,Object> doctorProfile) {
		return adminDoctorsService.profileUpdate(doctorProfile);
	}

	@PostMapping(value = "/addspecialization", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> addSpecialization(@RequestParam int doctorId, @RequestParam int[] specialization) {
		return adminDoctorsService.addSpecialization(doctorId, specialization);
	}
	
	@PostMapping(value = "/profileinactivate", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> profileInactivate(@RequestParam int doctorId) {
		return adminDoctorsService.profileInactivate(doctorId);
	}
	
	@PostMapping(value = "/profileactivate", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> profileActivate(@RequestParam int doctorId) {
		return adminDoctorsService.profileActivate(doctorId);
	}
	
	@PostMapping(value = "/profilestatus", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> profileStatus(@RequestParam int doctorId) {
		return adminDoctorsService.profileStatus(doctorId);
	}

}
