package com.example.TeleMedicine.PatientDashBoard;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PatientDashBoardRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> getAppointmentsHistory(int patientId) {
		StringBuffer query = new StringBuffer();
		query.append(
				" select b.appointment_id,c.doctor_id,c.name doctor_name,d.dept_name,b.appointment_session session_time,b.status appointment_status ");
		query.append(" from telemedicine_patients.tbl_patient_registration a ");
		query.append(" join telemedicine_master.tbl_appointments b on a.patient_id=b.patient_id and b.status IN ('C','SC') ");
		query.append(" left join telemedicine_doctors.tbl_doctor_registration c on c.doctor_id=b.doctor_id ");
		query.append(" left join telemedicine_master.tbl_medical_departments d on c.dept_id=d.dept_id ");
		query.append(" where a.patient_id=? ");
		return jdbcTemplate.queryForList(query.toString(), new Object[] { patientId });
	}

	public List<Map<String, Object>> getUpcomingAppointments(int patientId) {
		StringBuffer query = new StringBuffer();
		query.append(
				" select b.appointment_id,c.doctor_id,c.name doctor_name,d.dept_name,b.appointment_session session_time,b.status appointment_status ");
		query.append(" from telemedicine_patients.tbl_patient_registration a ");
		query.append(" join telemedicine_master.tbl_appointments b on a.patient_id=b.patient_id and b.status IN ('A','RS') ");
		query.append(" left join telemedicine_doctors.tbl_doctor_registration c on c.doctor_id=b.doctor_id ");
		query.append(" left join telemedicine_master.tbl_medical_departments d on c.dept_id=d.dept_id ");
		query.append(" where a.patient_id=? ");
		return jdbcTemplate.queryForList(query.toString(), new Object[] { patientId });
	}
}
