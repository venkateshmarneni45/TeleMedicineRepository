package com.example.TeleMedicine.FieldDoctorsList;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorsListRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String,Object>> getDoctorsList() {
		StringBuffer query = new StringBuffer();
		query.append(
				" select a.doctor_id,a.name doctor_name,a.mobile,a.email,if(a.img_path=null,null,concat('/TeleMedicineDocuments/DoctorPics/',a.img_path)) img,a.state,a.city, ");
		query.append(
				" d.gender_name gender,a.license,a.cost,b.dept_name,a.experience, c.about,a.status, ");
		query.append(
				" e.from_time,e.to_time ,e.session_time,group_concat(g.shortname) specilaization ");
		query.append(" from telemedicine_doctors.tbl_doctor_registration a ");
		query.append(" left join telemedicine_master.tbl_medical_departments b on a.dept_id=b.dept_id ");
		query.append(" left join telemedicine_doctors.tbl_about_doctor c on a.doctor_id=c.doctor_id ");
		query.append(" left join telemedicine_master.tbl_gender d on a.gender_id=d.gender_id ");
		query.append(" left join telemedicine_doctors.tbl_doctor_daily_timings e on a.doctor_id=e.doctor_id ");
		query.append(" left join telemedicine_doctors.tbl_doctor_specializations f on a.doctor_id=f.doctor_id ");
		query.append(" left join telemedicine_master.tbl_medical_specializations g on g.specialization_id=f.specialization_id ");
		query.append(" group by a.doctor_id ");

		return jdbcTemplate.queryForList(query.toString());
	}
}
