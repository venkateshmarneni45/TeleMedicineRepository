package com.example.TeleMedicine.DoctorProfile;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/doctorprofile")
public class DoctorProfileController {
	@Autowired
	private DoctorProfileService doctorProfileService;
	
	@PostMapping(value = "/profiledata", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> getProfileData(@RequestParam String mobile) {
		return doctorProfileService.getProfileData(mobile);
	}
	
	@PostMapping(value = "/updateprofile", consumes = "application/json", produces = "application/json")
	public Map<String, Object> profileUpdate(@RequestBody Map<String,Object> profile) {
		return doctorProfileService.profileUpdate(profile);
	}
	
	@PostMapping(value = "/updateprofilepic", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> profilePicUpdate(@RequestParam int doctorId,@RequestParam MultipartFile img) {
		return doctorProfileService.profilePicUpdate(doctorId,img);
	}
	
	@PostMapping(value = "/removeprofilepic", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> profilePicRemove(@RequestParam int doctorId) {
		return doctorProfileService.profilePicRemove(doctorId);
	}
	
	@PostMapping(value = "/profilehistory", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> profileHistory(@RequestParam int doctorId) {
		return doctorProfileService.profileHistory(doctorId);
	}
	
	@PostMapping(value = "/timechangeondate", consumes = "application/json", produces = "application/json")
	public Map<String, Object> timeChangeForDate(@RequestBody Map<String,Object> change) {
		return doctorProfileService.timeChangeForDate(change);
	}
	
	@PostMapping(value = "/applyholidays", consumes = "application/json", produces = "application/json")
	public Map<String, Object> doctorHolidays(@RequestBody Map<String,Object> holidays) {
		return doctorProfileService.doctorHolidays(holidays);
	}
}
