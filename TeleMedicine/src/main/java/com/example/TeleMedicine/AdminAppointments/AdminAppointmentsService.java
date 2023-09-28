package com.example.TeleMedicine.AdminAppointments;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminAppointmentsService {
	@Autowired
	private AdminAppointmentsRepository adminAppointmentsRepository;
	private Logger logger = LoggerFactory.getLogger(AdminAppointmentsService.class);

	public Map<String, Object> getAppointments(int category){
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", adminAppointmentsRepository.getAppointments(category));
			m.put("flag", true);
			m.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching admin appointments data");
			logger.error("Error, fetching admin appointments data");
		}
		return m;
	}
}
