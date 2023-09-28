package com.example.TeleMedicine.AdminDashboard;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admindashboard")
public class AdminDashboardController {
	@Autowired
	private AdminDashboardService adminDashboardService;
	
	@PostMapping(value="/data", consumes = "application/json", produces = "application/json")
	public Map<String, Object> dashBoardData(@RequestBody Map<String,Object> data){
		return adminDashboardService.dashBoardData(data);
	}
}
