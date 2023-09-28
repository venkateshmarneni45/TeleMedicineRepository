package com.example.TeleMedicine.AdminDoctors;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminDoctorsService {
	@Autowired
	private AdminDoctorsRepository adminDoctorsRepository;
	private Logger logger = LoggerFactory.getLogger(AdminDoctorsService.class);

	public synchronized Map<String, Object> getDoctorsList(Map<String,Object> list) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", adminDoctorsRepository.getDoctorsList(list));
			m.put("flag", true);
			m.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching doctors list");
			logger.error("Error, fetching doctors list");
		}
		return m;
	}

	public synchronized Map<String, Object> getDoctorProfile(int doctorId) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", adminDoctorsRepository.getDoctorProfile(doctorId));
			m.put("flag", true);
			m.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching doctor profile");
			logger.error("Error, fetching doctor profile");
		}
		return m;
	}

	public synchronized Map<String, Object> profileUpdate(Map<String,Object> doctorProfile) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", adminDoctorsRepository.profileUpdate(doctorProfile));
			m.put("flag", true);
			m.put("message", "Profile Updated Successfully");
			logger.debug(doctorProfile.get("id") + " profile updation successfull");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("message", "Error, while updating profile");
			m.put("status", 0);
			logger.debug(doctorProfile.get("id") + " profile updation failed");
		}
		return m;
	}

	public synchronized Map<String, Object> addSpecialization(int doctorId, int[] specialization) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", adminDoctorsRepository.addSpecialization(doctorId, specialization));
			m.put("message", "Specialization added to doctor");
			m.put("flag", true);
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, adding specialization to doctor");
			logger.error("Error, adding specialization to doctor");
		}
		return m;
	}
	
	public synchronized Map<String, Object> profileInactivate(int doctorId) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", adminDoctorsRepository.profileInactivate(doctorId));
			m.put("message", "Doctor profile is Inactivated");
			m.put("flag", true);
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, Inactivating doctor profile");
			logger.error("Error, Inactivating doctor profile");
		}
		return m;
	}
	
	public synchronized Map<String, Object> profileActivate(int doctorId) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", adminDoctorsRepository.profileActivate(doctorId));
			m.put("message", "Doctor profile is Activated");
			m.put("flag", true);
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, Activating doctor profile");
			logger.error("Error, Activating doctor profile");
		}
		return m;
	}
	

	public synchronized Map<String, Object> profileStatus(int doctorId) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", adminDoctorsRepository.profileStatus(doctorId));
			m.put("message", "Doctor status fetched successfull");
			m.put("flag", true);
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching profile status");
			logger.error("Error, fetching profile status");
		}
		return m;
	}
}
