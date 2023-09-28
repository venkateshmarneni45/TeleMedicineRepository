package com.example.TeleMedicine.PatientCategories;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patientcategories")
public class PatientCategoriesController {
	@Autowired
	private PatientCategoriesService categoriesService;

	@PostMapping(value = "/doctorinforeviews", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> getDoctorInfoReviews(@RequestParam int doctorId) {
		return categoriesService.getDoctorInfoReviews(doctorId);
	}

	@PostMapping(value = "/feedback", consumes = "application/json", produces = "application/json")
	public Map<String, Object> saveFeedBack(@RequestBody Map<String, Object> feedback) {
		return categoriesService.saveFeedBack(feedback);
	}

	@PostMapping(value = "/doctorslist", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> getDoctorsList(@RequestParam int category) {
		return categoriesService.getDoctorsList(category);
	}
}
