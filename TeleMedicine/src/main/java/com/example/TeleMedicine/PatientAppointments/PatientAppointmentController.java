package com.example.TeleMedicine.PatientAppointments;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patientappointment")
public class PatientAppointmentController {
	@Autowired
	private PatientAppointmentService appointmentService;

	@PostMapping(value = "/bookappointment", consumes = "application/json", produces = "application/json")
	public Map<String, Object> bookAppointment(@RequestBody Map<String, Object> book) {
		return appointmentService.bookAppointment(book);
	}

	@PostMapping(value = "/appointmentshistory", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> getAppointmentsHistory(@RequestParam int patientId) {
		return appointmentService.getAppointmentsHistory(patientId);
	}

	@PostMapping(value = "/upcomingappointments", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> getUpcomingAppointments(@RequestParam int patientId) {
		return appointmentService.getUpcomingAppointments(patientId);
	}

	@PostMapping(value = "/uploadmedicalreport", consumes = "application/json", produces = "application/json")
	public Map<String, Object> uploadMedicalReport(@RequestBody  Map<String, Object> medicalReports) {
		return appointmentService.uploadMedicalReport(medicalReports);
	}
	
	@PostMapping(value = "/fetchBookingTimings", consumes ="multipart/form-data", produces = "application/json")
	public Map<String, Object> fetchBookingTimes(@RequestParam int doctorId,@RequestParam String from,@RequestParam String to) {
		return appointmentService.fetchBookingTimes(doctorId,from,to);
	}
}
