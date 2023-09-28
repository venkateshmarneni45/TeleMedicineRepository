package com.example.TeleMedicine.AdminAppointments;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdminAppointmentsRepository {
	@Autowired
	private JdbcTemplate JdbcTemplate;
	
	public List<Map<String, Object>> getAppointments(int category) {
		StringBuffer query = new StringBuffer();
		query.append(" select a.appointment_id,b.patient_id patientid,b.Name patientname,concat(b.age,', ',e.gender_name) 'agegender',b.mobile,c.doctor_id doctorid,concat('Dr.',c.Name) doctorname, ");
		query.append(" d.Dept_Name,c.state,c.city,date_format(date(appointment_session),'%d %M,%Y') date,time_format(time(appointment_session),'%h:%i %p') time, ");
		query.append(" if(a.status='A','Active',if(a.status='C','Completed',if(a.status='RS','Rescheduled',if(a.status='SC','Cancelled',a.status)))) Status ");
		query.append(" from telemedicine_master.tbl_appointments a ");
		query.append(" left join telemedicine_patients.tbl_patient_registration b on a.patient_id=b.patient_id ");
		query.append(" left join telemedicine_doctors.tbl_doctor_registration c on a.doctor_id=c.doctor_id ");
		query.append(" left join telemedicine_master.tbl_medical_departments d on c.dept_id=d.dept_id ");
		query.append(" left join telemedicine_master.tbl_gender e on b.gender_id=e.gender_id ");
		
		if(category!=0) {
			query.append(" where c.dept_id="+category);
		}
		return JdbcTemplate.queryForList(query.toString());
	}
}
