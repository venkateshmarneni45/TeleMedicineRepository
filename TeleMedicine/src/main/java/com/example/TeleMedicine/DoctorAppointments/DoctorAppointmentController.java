package com.example.TeleMedicine.DoctorAppointments;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctorappointment")
public class DoctorAppointmentController {
	@Autowired
	private DoctorAppointmentService appointmentService;

	@PostMapping(value = "/completedappointments", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> getAppointmentsHistory(@RequestParam int doctorId) {
		return appointmentService.getAppointmentsHistory(doctorId);
	}

	@PostMapping(value = "/upcomingappointments", consumes = "multipart/form-data", produces = "application/json")
	public Map<String, Object> getUpcomingAppointments(@RequestParam int doctorId) {
		return appointmentService.getUpcomingAppointments(doctorId);
	}

	@PostMapping(value = "/uploadcasesheet", consumes = "application/json", produces = "application/json")
	public Map<String, Object> uploadCaseSheet(@RequestBody  Map<String, Object> caseSheets) {
		return appointmentService.uploadCaseSheet(caseSheets);
	}

	@PostMapping(value = "/appointmentpostpone", consumes = "application/json", produces = "application/json")
	public Map<String, Object> postponeAppointment(@RequestBody Map<String, Object> postpone) {
		return appointmentService.postponeAppointment((int) postpone.get("appointmentId"),
				postpone.get("dateTime").toString());
	}
}
