package com.example.TeleMedicine.PatientCategories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PatientCategoriesRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Map<String, Object> getDoctorInfo(int doctorId) {
		StringBuffer query = new StringBuffer();
		query.append(
                " select a.doctor_id,a.name doctor_name,if(a.img_path=null,null,concat('/TeleMedicineDocuments/DoctorPics/',a.img_path)) img,d.gender_name gender,a.experience,a.cost,c.dept_name, ");
        query.append(" count(distinct(b.patient_id)) patients,e.about,group_concat(distinct(g.shortname)) specializations ");
        query.append(" from telemedicine_doctors.tbl_doctor_registration a ");
        query.append(" left join telemedicine_master.tbl_appointments b on a.doctor_id=b.doctor_id and b.status in ('A','C','RS')");
        query.append(" left join telemedicine_master.tbl_medical_departments c on a.dept_id=c.dept_id ");
        query.append(" left join telemedicine_master.tbl_gender d on a.gender_id=d.gender_id ");
        query.append(" left join telemedicine_doctors.tbl_about_doctor e on a.doctor_id=e.doctor_id ");
        query.append(" left join telemedicine_doctors.tbl_doctor_specializations f on a.doctor_id=f.doctor_id ");
        query.append(" left join telemedicine_master.tbl_medical_specializations g on g.specialization_id=f.specialization_id ");
        query.append(" where a.doctor_id=" + doctorId);

		return jdbcTemplate.queryForMap(query.toString());
	}

	public Map<String, Object> getDoctorReviews(int doctorId) {
		Map<String, Object> reviews = new HashMap<>();
		List<Map<String, Object>> review1 = doctorReviews1(doctorId);
		Map<String, Object> review2 = doctorReviews2(doctorId);

		reviews.put("reviews", review1);
		reviews.put("patients", review2.get("reviews_count"));
		reviews.put("rating", review2.get("avg_rating"));
		return reviews;
	}

	public List<Map<String, Object>> doctorReviews1(int doctorId) {
		StringBuffer query = new StringBuffer();
		query.append(
				" select b.patient_id,b.name patient_name,a.rating,a.comments,date_format(date(a.lup_date),'%d %M, %Y') review_date ");
		query.append(" from telemedicine_master.tbl_appointment_rating a ");
		query.append(" join telemedicine_patients.tbl_patient_registration b on a.patient_id=b.patient_id ");
		query.append(" where a.doctor_id=? ");

		return jdbcTemplate.queryForList(query.toString(), new Object[] { doctorId });
	}

	public Map<String, Object> doctorReviews2(int doctorId) {
		StringBuffer query = new StringBuffer();
		query.append(" select count(*) reviews_count,avg(rating) avg_rating ");
		query.append(" from telemedicine_master.tbl_appointment_rating ");
		query.append(" where doctor_id=? ");

		return jdbcTemplate.queryForMap(query.toString(), new Object[] { doctorId });
	}

	@Transactional(rollbackFor = Throwable.class)
	public int saveFeedBack(Map<String, Object> feedback) {
		int rowInserted = jdbcTemplate.update(
				"INSERT INTO telemedicine_master.tbl_appointment_rating(doctor_id,patient_id,appointment_id,rating,comments) values(?,?,?,?,?)",
				new Object[] { feedback.get("doctorId"), feedback.get("patientId"), feedback.get("appointmentId"), feedback.get("rating"),
						feedback.get("comments") });
		if (rowInserted != 1) {
			throw new RuntimeException();
		}
		return rowInserted;
	}

	public List<Map<String, Object>> getDoctorsList(int category) {
		StringBuffer query = new StringBuffer();
		query.append(
                " select a.doctor_id,a.name doctor_name,if(a.img_path=null,null,concat('/TeleMedicineDocuments/DoctorPics/',a.img_path)) img,c.dept_name,a.experience,a.cost,avg(b.rating) rating ");
        query.append(" from telemedicine_doctors.tbl_doctor_registration a ");
        query.append(" left join telemedicine_master.tbl_appointment_rating b on a.doctor_id=b.doctor_id ");
        query.append(" left join telemedicine_master.tbl_medical_departments c on a.dept_id=c.dept_id ");
        query.append(" where a.status=1001 and a.dept_id=? ");
        query.append(" group by a.doctor_id ");

		return jdbcTemplate.queryForList(query.toString(), new Object[] { category });
	}
}
