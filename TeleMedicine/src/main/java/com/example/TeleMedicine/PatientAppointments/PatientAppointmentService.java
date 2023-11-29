package com.example.TeleMedicine.PatientAppointments;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientAppointmentService {
	@Autowired
	private PatientAppointmentRepository appointmentRepository;
	private Logger logger = LoggerFactory.getLogger(PatientAppointmentService.class);

	public synchronized Map<String, Object> bookAppointment(Map<String, Object> book) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			int status = appointmentRepository.bookAppointment(book);
			if (status == 0) {
				m.put("status", 0);
				m.put("message", "Doctor is Inactive");
				m.put("flag", false);
			} else {
				m.put("status", 1);
				m.put("message", "Appointment booked");
				m.put("flag", true);
				logger.error(book.get("id") + " Appointment booking success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, booking appointment for " + book.get("id"));
			logger.error("Error, booking appointment for " + book.get("id"));
		}
		return m;
	}

	public synchronized Map<String, Object> getAppointmentsHistory(int patientId) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", appointmentRepository.getAppointmentsHistory(patientId));
			m.put("flag", true);
			m.put("status", 1);
			logger.debug(patientId + " appointments history fetched");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching appointments history");
			logger.error("Error fetching " + patientId + " appointments history");
		}
		return m;
	}

	public synchronized Map<String, Object> getUpcomingAppointments(int patientId) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", appointmentRepository.getUpcomingAppointments(patientId));
			m.put("flag", true);
			m.put("status", 1);
			logger.debug(patientId + " appointments history fetched");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching upcoming appointments");
			logger.error("Error fetching " + patientId + " upcoming appointments");
		}
		return m;
	}

	public synchronized Map<String, Object> uploadMedicalReport(Map<String, Object> medicalReports) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", appointmentRepository.uploadMedicalReport(medicalReports));
			m.put("flag", true);
			m.put("message", "Medical Reports Uploaded Successful");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, uploading Medical Reports");
		}
		return m;
	}

	public synchronized Map<String, Object> fetchBookingTimes(int doctorId, String from, String to) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", appointmentRepository.fetchBookingTimes(doctorId, from, to));
			m.put("flag", true);
			m.put("message", "Booking Timings fetched successfull");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching booking timings");
		}
		return m;
	}
}
