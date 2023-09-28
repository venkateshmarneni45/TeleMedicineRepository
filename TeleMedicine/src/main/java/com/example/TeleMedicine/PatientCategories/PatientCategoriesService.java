package com.example.TeleMedicine.PatientCategories;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientCategoriesService {
	@Autowired
	private PatientCategoriesRepository categoriesRepository;
	private Logger logger = LoggerFactory.getLogger(PatientCategoriesService.class);

	public synchronized Map<String, Object> getDoctorInfoReviews(int id) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			Map<String, Object> doctorInfo = categoriesRepository.getDoctorInfo(id);
			Map<String, Object> reviews = categoriesRepository.getDoctorReviews(id);
			m.put("flag", true);
			m.put("status", 1);
			m.put("doctorinfo", doctorInfo);
			m.put("reviews", reviews);
			logger.debug(id + " doctor info fetched");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching doctor info ");
			logger.error("Error fetching " + id + " info");
		}
		return m;
	}

	public synchronized Map<String, Object> saveFeedBack(Map<String, Object> feedback) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", categoriesRepository.saveFeedBack(feedback));
			m.put("flag", true);
			m.put("message", "Feedback uploaded success");
			logger.debug("Feedback uploaded success for appointment " + feedback.get("aid"));
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Feedback uploaded failed");
			logger.error("Feedback uploaded failed for appointment " + feedback.get("aid"));
		}
		return m;
	}

	public synchronized Map<String, Object> getDoctorsList(int category) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", categoriesRepository.getDoctorsList(category));
			m.put("flag", true);
			m.put("status", 1);
			logger.debug("doctors list fetching success");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching doctors list");
			logger.error("doctors list fetching failed");
		}
		return m;
	}

}
