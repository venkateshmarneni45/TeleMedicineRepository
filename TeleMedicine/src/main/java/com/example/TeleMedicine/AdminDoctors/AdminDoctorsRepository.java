package com.example.TeleMedicine.AdminDoctors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AdminDoctorsRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	@Value("${DoctorPics}")
	private String longPath;
	@Value("${DoctorImgPath}")
	private String shortPath;
	private Logger logger = LoggerFactory.getLogger(AdminDoctorsRepository.class);

	public List<Map<String, Object>> getDoctorsList(Map<String, Object> list) {
		StringBuffer query = new StringBuffer();
		query.append(
				" select a.doctor_id,concat('Dr.',a.name) doctor_name,c.dept_name,count(distinct(b.patient_id)) patients,");
		query.append(
				" count(b.appointment_id) appointments,a.state,a.city,if(a.status=1001,'Active','Inactive') status");
		query.append(" from telemedicine_doctors.tbl_doctor_registration a ");
		query.append(" left join telemedicine_master.tbl_appointments b on a.doctor_id=b.doctor_id ");
		query.append(" left join telemedicine_master.tbl_medical_departments c on a.dept_id=c.dept_id ");

		if ((int) list.get("category") == 0 && list.get("state") == "" && list.get("city") == "") {
			query.append(" group by a.doctor_id ");
		} else if ((int) list.get("category") != 0 && list.get("state") == "" && list.get("city") == "") {
			query.append(" where a.dept_id=" + list.get("category"));
			query.append(" group by a.doctor_id ");
		} else if ((int) list.get("category") == 0 && list.get("state") != "" && list.get("city") == "") {
			query.append(" where a.state='" + list.get("state"));
			query.append("' group by a.doctor_id ");
		} else if ((int) list.get("category") == 0 && list.get("state") != "" && list.get("city") != "") {
			query.append(" where a.state='" + list.get("state") + "' and city='" + list.get("city"));
			query.append("' group by a.doctor_id ");
		} else if ((int) list.get("category") != 0 && list.get("state") != "" && list.get("city") != "") {
			query.append(" where a.dept_id=" + list.get("category") + " and a.state='" + list.get("state")
					+ "' and city='" + list.get("city"));
			query.append("' group by a.doctor_id ");
		} else if ((int) list.get("category") != 0 && list.get("state") != "" && list.get("city") == "") {
			query.append(" where a.dept_id=" + list.get("category") + " and a.state='" + list.get("state"));
			query.append("' group by a.doctor_id ");
		}
		return jdbcTemplate.queryForList(query.toString());
	}

	public Map<String, Object> getDoctorProfile(int doctorId) {
		Map<String, Object> doctorProfile = new HashMap<>();
		doctorProfile.put("overview", getOverview(doctorId));
		doctorProfile.put("reviews", getReviews(doctorId));
		doctorProfile.put("timeTable", getTimeTable(doctorId));

		return doctorProfile;
	}

	public Map<String, Object> getOverview(int doctorId) {
		StringBuffer query = new StringBuffer();
		query.append(
				" select a.doctor_id id,a.name,a.mobile,a.email,a.license,b.dept_name,c.about,a.experience,a.cost, ");
		query.append(
				" a.state,a.city,if(a.img_path=null,null,concat('/TeleMedicineDocuments/DoctorPics/',a.img_path)) img,group_concat(distinct(shortname)) specializations ");
		query.append(" from telemedicine_doctors.tbl_doctor_registration a ");
		query.append(" left join telemedicine_master.tbl_medical_departments b on a.dept_id=b.dept_id ");
		query.append(" left join telemedicine_doctors.tbl_about_doctor c on a.doctor_id=c.doctor_id ");
		query.append(" left join telemedicine_doctors.tbl_doctor_specializations d on d.doctor_id=a.doctor_id ");
		query.append(
				" left join telemedicine_master.tbl_medical_specializations e on e.specialization_id=d.specialization_id ");
		query.append(" where a.doctor_id=" + doctorId);

		return jdbcTemplate.queryForMap(query.toString());
	}

	public Map<String, Object> getReviews(int doctorId) {
		StringBuffer review = new StringBuffer();
		review.append(
				" select b.name patient_name,a.rating,a.comments,date_format(date(a.lup_date),'%M %y') review_date ");
		review.append(" from telemedicine_master.tbl_appointment_rating a ");
		review.append(" join telemedicine_patients.tbl_patient_registration b on a.patient_id=b.patient_id ");
		review.append(" where a.doctor_id=? ");

		StringBuffer count = new StringBuffer();
		count.append(" select count(*) reviews_count,avg(rating) avg_rating ");
		count.append(" from telemedicine_master.tbl_appointment_rating ");
		count.append(" where doctor_id=? ");

		Map<String, Object> map = new HashMap<>();
		map.put("reviews", jdbcTemplate.queryForList(review.toString(), new Object[] { doctorId }));
		map.put("count", jdbcTemplate.queryForMap(count.toString(), new Object[] { doctorId }));

		return map;
	}

	public List<TimeSchedule> getTimeTable(int doctorId) {
		StringBuffer query = new StringBuffer();
		query.append(" select doctor_id ID,from_time 'FROM',to_time 'TO',session_time TIME ");
		query.append(" from telemedicine_doctors.tbl_doctor_daily_timings ");
		query.append(" where doctor_id=?");

		return jdbcTemplate.query(query.toString(), (rs, rowNum) -> {
			TimeSchedule t = new TimeSchedule();
			t.setId(rs.getString("id"));
			t.setFrom(rs.getString("from"));
			t.setTo(rs.getString("to"));
			t.setSessionTime(rs.getInt("time"));
			return t;
		}, new Object[] { doctorId });
	}

	@Transactional(rollbackFor = Throwable.class)
	public int profileUpdate(Map<String, Object> doctorProfile) {
		StringBuffer query = new StringBuffer();
		query.append(
				" update telemedicine_doctors.tbl_doctor_registration a join telemedicine_doctors.tbl_about_doctor b on a.doctor_id=b.doctor_id ");
		query.append(" set a.name=?,a.experience=?,a.email=?,b.about=? where a.doctor_id=? ");
		int rowsUp = jdbcTemplate.update(query.toString(),
				new Object[] { doctorProfile.get("name"), doctorProfile.get("experience"), doctorProfile.get("email"),
						doctorProfile.get("about"), doctorProfile.get("id") });
		if (rowsUp != 2) {
			throw new RuntimeException("Error during profile updation");
		}
		return rowsUp;
	}

	public int addSpecialization(int doctorId, int[] specialization) {
		if (specialization.length > 0) {
			String deptsQuery = "INSERT INTO telemedicine_doctors.tbl_doctor_specializations(DOCTOR_ID,SPECIALIZATION_ID) VALUES (?, ?)";

			int[] updateCounts = jdbcTemplate.execute((Connection con) -> {
				PreparedStatement stmt = con.prepareStatement(deptsQuery);
				for (int spec : specialization) {
					stmt.setInt(1, doctorId);
					stmt.setInt(2, spec);
					stmt.addBatch();
				}
				return stmt.executeBatch();
			});

			int totalRowsUpdated = 0;
			for (int updateCount : updateCounts) {
				totalRowsUpdated += updateCount;
			}

			if (totalRowsUpdated != specialization.length) {
				throw new RuntimeException("Failed to insert all specializations");
			}
		} else {
			logger.debug("No specializations for " + doctorId);
		}
		return specialization.length;
	}

	public int profileInactivate(int doctorId) {
		String query = "Update telemedicine_doctors.tbl_doctor_registration set status=1002 where doctor_id=?";
		int rowUp = jdbcTemplate.update(query, new Object[] { doctorId });
		if (rowUp != 1) {
			throw new RuntimeException("Failed to inactivate doctor");
		}
		return rowUp;
	}

	public int profileActivate(int doctorId) {
		String query = "Update telemedicine_doctors.tbl_doctor_registration set status=1001 where doctor_id=?";
		int rowUp = jdbcTemplate.update(query, new Object[] { doctorId });
		if (rowUp != 1) {
			throw new RuntimeException("Failed to activate doctor");
		}
		return rowUp;
	}

	public int profileStatus(int doctorId) {
		String query = "select status from telemedicine_doctors.tbl_doctor_registration where doctor_id=?";
		return jdbcTemplate.queryForObject(query, Integer.class, new Object[] { doctorId });
	}
}
