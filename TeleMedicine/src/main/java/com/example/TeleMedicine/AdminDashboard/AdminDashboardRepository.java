package com.example.TeleMedicine.AdminDashboard;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDashboardRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Map<String, Object> dashBoardData(Map<String, Object> data) {
		Map<String, Object> m = new LinkedHashMap<>();
		int doctorsCount = jdbcTemplate
				.queryForObject("SELECT count(*) from telemedicine_doctors.tbl_doctor_registration", Integer.class);
		int patientsCount = jdbcTemplate
				.queryForObject("SELECT count(*) from telemedicine_patients.tbl_patient_registration", Integer.class);
		int appointmentsCount = jdbcTemplate.queryForObject("SELECT count(*) from telemedicine_master.tbl_appointments",
				Integer.class);

		StringBuffer query = new StringBuffer();
		query.append(
				" select b.patient_id,b.Name PatientName,c.doctor_id,c.Name DoctorName,d.dept_name Department,c.State, ");
		query.append(" c.City,time_format(time(appointment_session),'%h:%i %p') time, ");
		query.append(
				" if(a.status='A','Active',if(a.status='C','Completed',if(a.status='RS','Rescheduled',if(a.status='SC','Cancelled',a.status)))) Status ");
		query.append(" from telemedicine_master.tbl_appointments a ");
		query.append(" left join telemedicine_patients.tbl_patient_registration b on a.patient_id=b.patient_id ");
		query.append(" left join telemedicine_doctors.tbl_doctor_registration c on a.doctor_id=c.doctor_id ");
		query.append(" left join telemedicine_master.tbl_medical_departments d on c.dept_id=d.dept_id ");

		if ((int) data.get("category") == 0 && data.get("state") == "" && data.get("city") == "") {
			query.append(" where  date(a.appointment_session)=CURRENT_DATE ");
		} else if ((int) data.get("category") != 0 && data.get("state") == "" && data.get("city") == "") {
			query.append(" where  date(a.appointment_session)=CURRENT_DATE  and c.dept_id=" + data.get("category"));
		} else if ((int) data.get("category") == 0 && data.get("state") != "" && data.get("city") != "") {
			query.append(" where  date(a.appointment_session)=CURRENT_DATE and c.state='" + data.get("state")
					+ "' and c.city='" + data.get("city") + "'");
		} else if ((int) data.get("category") != 0 && data.get("state") != "" && data.get("city") != "") {
			query.append(" where  date(a.appointment_session)=CURRENT_DATE and c.dept_id=" + data.get("category")
					+ " and c.state='" + data.get("state") + "' and c.city='" + data.get("city") + "'");
		} else if ((int) data.get("category") == 0 && data.get("state") != "" && data.get("city") == "") {
			query.append(" where  date(a.appointment_session)=CURRENT_DATE and c.state='" + data.get("state") + "'");
		} else if ((int) data.get("category") != 0 && data.get("state") != "" && data.get("city") == "") {
			query.append(" where  date(a.appointment_session)=CURRENT_DATE and c.dept_id=" + data.get("category")
					+ " and c.state='" + data.get("state") + "'");
		}

		List<Map<String, Object>> appointmentsList = jdbcTemplate.queryForList(query.toString());

		m.put("doctorsCount", doctorsCount);
		m.put("patientsCount", patientsCount);
		m.put("appointmentsCount", appointmentsCount);
		m.put("appointmentsList", appointmentsList);

		return m;
	}
}
