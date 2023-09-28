package com.example.TeleMedicine.DoctorAppointments;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorAppointmentService {
	@Autowired
	private DoctorAppointmentRepository appointmentRepository;
	private Logger logger = LoggerFactory.getLogger(DoctorAppointmentService.class);

	public synchronized Map<String, Object> getAppointmentsHistory(int doctorId) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", appointmentRepository.getAppointmentsHistory(doctorId));
			m.put("flag", true);
			m.put("status", 1);
			logger.debug(doctorId + " appointments history fetched");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching appointments history");
			logger.error("Error fetching " + doctorId + " appointments history");
		}
		return m;
	}

	public synchronized Map<String, Object> getUpcomingAppointments(int doctorId) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", appointmentRepository.getUpcomingAppointments(doctorId));
			m.put("flag", true);
			m.put("status", 1);
			logger.debug(doctorId + " appointments history fetched");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching upcoming appointments");
			logger.error("Error fetching " + doctorId + " upcoming appointments");
		}
		return m;
	}

	public synchronized Map<String, Object> uploadCaseSheet(Map<String, Object> caseSheets) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", appointmentRepository.uploadCaseSheet(caseSheets));
			m.put("flag", true);
			m.put("message", "Case Sheets Uploaded Successful");
			logger.debug("Case Sheets Uploaded Successful");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, uploading case sheets");
			logger.error("Error, uploading case sheets");
		}
		return m;
	}

	public synchronized Map<String, Object> postponeAppointment(int appointmentId, String dateTime) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", appointmentRepository.postponeAppointment(appointmentId, dateTime));
			m.put("flag", true);
			m.put("message", "Appointment Rescheduled Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, Rescheduling appoinmetnt");
		}
		return m;
	}
}
