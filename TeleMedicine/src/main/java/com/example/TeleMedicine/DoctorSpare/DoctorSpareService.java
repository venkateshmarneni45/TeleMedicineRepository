package com.example.TeleMedicine.DoctorSpare;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorSpareService {
	@Autowired
	private DoctorSpareRepository doctorSpareRepository;
	private Logger logger = LoggerFactory.getLogger(DoctorSpareService.class);

	public synchronized Map<String, Object> getPatientProfile(int patientId) {

		Map<String, Object> m = new LinkedHashMap<>();
		try {
			Map<String, Object> m1 = doctorSpareRepository.getPatientData(patientId);
			List<Map<String, Object>> m2 = doctorSpareRepository.getAppointments(patientId);

			Map<String, Object> m3 = new HashMap<>();
			m3.put("PatientData", m1);
			m3.put("Appointments", m2);

			m.put("message", m3);
			m.put("flag", true);
			m.put("status", 1);
			logger.debug(patientId + " profile data fetched successfully");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("message", "Error, Fetching data");
			m.put("status", 0);
			logger.error("Error fetching " + patientId + " profile data");
		}
		return m;
	}

}
