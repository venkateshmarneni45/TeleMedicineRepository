package com.example.TeleMedicine.DoctorProfile;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DoctorProfileService {
	@Autowired
	private DoctorProfileRepository doctorProfileRepository;
	private Logger logger = LoggerFactory.getLogger(DoctorProfileService.class);

	public synchronized Map<String, Object> getProfileData(String mobile) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", doctorProfileRepository.getProfileData(mobile));
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

	public synchronized Map<String, Object> profileUpdate(Map<String, Object> profile) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			doctorProfileRepository.profileUpdate(profile);
			m.put("status", 1);
			m.put("flag", true);
			m.put("message", "Profile Updated Successfully");
			logger.debug(profile.get("id") + " updation successfull");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("message", "Error, while updating profile");
			m.put("status", 0);
			logger.debug(profile.get("id") + " updation failed");
		}
		return m;
	}

	public synchronized Map<String, Object> profilePicUpdate(int doctorId, MultipartFile img) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", doctorProfileRepository.profilePicUpdate(doctorId, img));
			m.put("flag", true);
			m.put("message", "Profile Pic Updated Successfully");
			logger.debug(doctorId + " pic updation successfull");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("message", "Error, while updating profile pic");
			m.put("status", 0);
			logger.debug(doctorId + " pic updation failed");
		}
		return m;
	}

	public synchronized Map<String, Object> profilePicRemove(int doctorId) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", doctorProfileRepository.profilePicRemove(doctorId));
			m.put("flag", true);
			m.put("message", "Profile Pic Removed Successfully");
			logger.debug(doctorId + " pic removed successfull");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("message", "Error, while removing profile pic");
			m.put("status", 0);
			logger.debug(doctorId + " pic removal failed");
		}
		return m;
	}

	public synchronized Map<String, Object> profileHistory(int doctorId) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			Map<String, Object> data = doctorProfileRepository.profileHistory(doctorId);
			m.put("flag", true);
			m.put("message", data);
			m.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("message", "Error, while fetching profile history");
			m.put("status", 0);
		}
		return m;
	}

	public synchronized Map<String, Object> timeChangeForDate(Map<String, Object> change) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			doctorProfileRepository.timeChangeForDate(change);
			m.put("status", 1);
			m.put("flag", true);
			m.put("message", "Timings changed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("message", "Error, while changing timings");
			m.put("status", 0);
		}
		return m;
	}

	public synchronized Map<String, Object> doctorHolidays(Map<String, Object> holidays) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			doctorProfileRepository.doctorHolidays(holidays);
			m.put("status", 1);
			m.put("flag", true);
			m.put("message", "Doctor applied holidays successful");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("message", "Error, while applying holidays by doctor");
			m.put("status", 0);
		}
		return m;
	}
}
