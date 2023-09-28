package com.example.TeleMedicine.AdminPatients;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminPatientsService {
	@Autowired
	private AdminPatientsRepository adminPatientsRepository;
	private Logger logger = LoggerFactory.getLogger(AdminPatientsService.class);

	public synchronized Map<String, Object> getPatientsList() {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", 1);
			m.put("flag", true);
			m.put("message", adminPatientsRepository.getPatientsList());
			logger.debug("Patients list fetched successful");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("message", "Error, fetching patients list");
			m.put("status", 0);
			logger.debug("Error, fetching patients list");
		}
		return m;
	}
	
	public synchronized Map<String, Object> getPatientProfile(int patientId) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", 1);
			m.put("flag", true);
			m.put("message", adminPatientsRepository.getPatientProfile(patientId));
			logger.debug("Patient profile fetched successful");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("message", "Error, fetching patient profile");
			m.put("status", 0);
			logger.debug("Error, fetching patient profile");
		}
		return m;
	}
}
