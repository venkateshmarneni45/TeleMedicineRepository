package com.example.TeleMedicine.PatientProfile;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/patientprofile")
public class PatientProfileController {
	@Autowired
	private PatientProfileService profileService;

	@PostMapping(value = "/getprofiledata", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> getPatientData(@RequestParam String mobile) {
		return profileService.getPatientData(mobile);
	}

	@PostMapping(value = "/updateprofile", consumes = "application/json", produces = "application/json")
	public Map<String, Object> profileUpdate(@RequestBody Patient_Updation patientUpdation) {
		return profileService.profileUpdate(patientUpdation);
	}
	
	@PostMapping(value = "/updateprofilepic", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> profilePicUpdate(@RequestParam int patientId,@RequestParam MultipartFile img) {
		return profileService.profilePicUpdate(patientId,img);
	}
	
	@PostMapping(value = "/removeprofilepic", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> profilePicRemove(@RequestParam int patientId) {
		return profileService.profilePicRemove(patientId);
	}
}
