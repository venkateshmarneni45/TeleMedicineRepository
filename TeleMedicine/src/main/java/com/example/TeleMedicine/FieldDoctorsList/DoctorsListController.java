package com.example.TeleMedicine.FieldDoctorsList;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fielddoctorslist")
public class DoctorsListController {
	@Autowired
	private DoctorsListService doctorsListService;
	
	@PostMapping(value="/doctors",produces="application/json")
	public Map<String,Object> getDoctorsList(){
		return doctorsListService.getDoctorsList();
	}
}
