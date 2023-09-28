package com.example.TeleMedicine.DoctorSpare;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorSpareRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Map<String, Object> getPatientData(int patientId) {
		StringBuffer query=new StringBuffer();
		query.append(" select a.patient_id,a.name patient_name,a.mobile,a.age,b.gender_name gender,c.blood_group_name blood_group,a.height,a.weight, ");
		query.append(" if(a.img_path=null,null,concat('/TeleMedicineDocuments/PatientPics/',a.img_path)) img ");
		query.append(" from telemedicine_patients.tbl_patient_registration a ");
		query.append(" left join telemedicine_master.tbl_gender b on a.gender_id=b.gender_id ");
		query.append(" left join telemedicine_master.tbl_blood_group c on a.blood_group_id=c.blood_group_id ");
		query.append(" where patient_id=? ");
		return jdbcTemplate.queryForMap(query.toString(),new Object[] {patientId});
	}

	public List<Map<String,Object>> getAppointments(int patientId) {
		StringBuffer query=new StringBuffer();
		query.append(" select a.appointment_id,b.name doctor_name,c.dept_name,group_concat(e.shortname) specializations, ");
		query.append(" if(b.img_path=null,null,concat('/TeleMedicineDocuments/DoctorPics/',b.img_path)) img ");
		query.append(" from telemedicine_master.tbl_appointments a ");
		query.append(" join telemedicine_doctors.tbl_doctor_registration b on a.doctor_id=b.doctor_id ");
		query.append(" left join telemedicine_master.tbl_medical_departments c on b.dept_id=c.dept_id ");
		query.append(" left join telemedicine_doctors.tbl_doctor_specializations d on b.doctor_id=d.doctor_id ");
		query.append(" left join telemedicine_master.tbl_medical_specializations e on d.specialization_id=e.specialization_id ");
		query.append(" where a.patient_id=? and a.status in ('C') ");
		query.append(" group by appointment_id ");
		return jdbcTemplate.queryForList(query.toString(),new Object[] {patientId});
	}
}
