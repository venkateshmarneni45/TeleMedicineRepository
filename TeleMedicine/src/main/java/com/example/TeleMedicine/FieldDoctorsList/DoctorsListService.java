package com.example.TeleMedicine.FieldDoctorsList;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorsListService {
	@Autowired
	private DoctorsListRepository doctorsListRepository;
	private Logger logger = LoggerFactory.getLogger(DoctorsListService.class);
	
	public synchronized Map<String, Object> getDoctorsList() {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", doctorsListRepository.getDoctorsList());
			m.put("flag", true);
			m.put("message", "doctor registration Successful");
			logger.debug("Doctors list fetching successful");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("message", "Error, while registering doctor");
			m.put("status", 0);
			logger.debug("Doctors list fetching failed");
		}
		return m;
	}
}
