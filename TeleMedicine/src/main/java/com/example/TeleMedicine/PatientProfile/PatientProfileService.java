package com.example.TeleMedicine.PatientProfile;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PatientProfileService {
	@Autowired
	private PatientProfileRepository profileRepository;
	private Logger logger = LoggerFactory.getLogger(PatientProfileService.class);

	public synchronized Map<String, Object> getPatientData(String mobile) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", profileRepository.getPatientData(mobile));
			m.put("flag", true);
			m.put("status", 1);
			logger.debug(mobile + " profile data fetched successfully");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("message", "Error, Fetching data");
			m.put("status", 0);
			logger.error("Error fetching " + mobile + " profile data");
		}
		return m;
	}

	public synchronized Map<String, Object> profileUpdate(Patient_Updation patientUpdation) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", profileRepository.profileUpdate(patientUpdation));
			m.put("flag", true);
			m.put("message", "Profile Updated Successfully");
			logger.debug(patientUpdation.getId() + " updation successfull");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("message", "Error, while updating profile");
			m.put("status", 0);
			logger.debug(patientUpdation.getId() + " updation failed");
		}
		return m;
	}

	public synchronized Map<String, Object> profilePicUpdate(int patientId, MultipartFile img) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", profileRepository.profilePicUpdate(patientId, img));
			m.put("flag", true);
			m.put("message", "Profile Pic Updated Successfully");
			logger.debug(patientId + " pic updation successfull");

		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("message", "Error, while updating profile pic");
			m.put("status", 0);
			logger.debug(patientId + " pic updation failed");
		}
		return m;
	}

	public synchronized Map<String, Object> profilePicRemove(int patientId) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", profileRepository.profilePicRemove(patientId));
			m.put("flag", true);
			m.put("message", "Profile Pic Removed Successfully");
			logger.debug(patientId + " pic removed successfull");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("message", "Error, while removing profile pic");
			m.put("status", 0);
			logger.debug(patientId + " pic removal failed");
		}
		return m;
	}
}
