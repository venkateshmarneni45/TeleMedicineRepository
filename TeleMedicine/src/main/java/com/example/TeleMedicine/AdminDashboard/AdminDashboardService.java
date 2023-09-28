package com.example.TeleMedicine.AdminDashboard;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminDashboardService {
	@Autowired
	private AdminDashboardRepository adminDashboardRepository;
	private Logger logger = LoggerFactory.getLogger(AdminDashboardService.class);
	
	public Map<String, Object> dashBoardData(Map<String,Object> data){
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", adminDashboardRepository.dashBoardData(data));
			m.put("flag", true);
			m.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching admin dash board data");
			logger.error("Error, fetching admin dash board data");
		}
		return m;
	}
}
