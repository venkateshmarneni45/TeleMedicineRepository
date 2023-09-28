package com.example.TeleMedicine.PatientDashBoard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TeleMedicine.CommonServices.SpareRepository;

@Service
public class PatientDashBoardService {
	@Autowired
	private PatientDashBoardRepository dashBoardRepository;
	@Autowired
	private SpareRepository spareRepository;
	private Logger logger = LoggerFactory.getLogger(PatientDashBoardService.class);

	public synchronized Map<String, Object> getCompleteData(int patientId) {
		Map<String, Object> data = new HashMap<>();
		try {
			List<Map<String, Object>> departments = spareRepository.getDepartments();
			List<Map<String, Object>> comingAppointments = dashBoardRepository.getUpcomingAppointments(patientId);
			List<Map<String, Object>> completedAppointments = dashBoardRepository.getAppointmentsHistory(patientId);

			data.put("departments",departments);
			data.put("comingAppointments", comingAppointments);
			data.put("completedAppointments", completedAppointments);
			data.put("flag", true);
			logger.debug("Dash board data fetched for patient " + patientId);
			data.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			data.put("flag", false);
			data.put("status", 0);
			data.put("message", "Error, fetching dashboard data");
			logger.debug("Error, fetching dashboard data for patient " + patientId);
		}
		return data;
	}
}
