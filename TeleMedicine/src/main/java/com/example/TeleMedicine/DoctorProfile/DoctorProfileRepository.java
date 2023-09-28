package com.example.TeleMedicine.DoctorProfile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;

@Repository
public class DoctorProfileRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Value("${DoctorPics}")
	private String longPath;
	@Value("${DoctorImgPath}")
	private String shortPath;
	private Logger logger = LoggerFactory.getLogger(DoctorProfileRepository.class);

	public Doctor_Details getProfileData(String mobile) {
		StringBuffer query = new StringBuffer();
		query.append(
				" select a.doctor_id,a.name,d.gender_name,a.email,a.mobile,if(a.img_path=null,null,concat('/TeleMedicineDocuments/DoctorPics/',a.img_path)) img,a.license,b.dept_name,c.about,group_concat(f.shortname) specialization ");
		query.append(" from telemedicine_doctors.tbl_doctor_registration a ");
		query.append(" left join telemedicine_master.tbl_medical_departments b on a.dept_id=b.dept_id ");
		query.append(" left join telemedicine_doctors.tbl_about_doctor c on a.doctor_id=c.doctor_id ");
		query.append(" left join telemedicine_master.tbl_gender d on d.gender_id=a.gender_id ");
		query.append(" left join telemedicine_doctors.tbl_doctor_specializations e on a.doctor_id=e.doctor_id ");
		query.append(
				" left join telemedicine_master.tbl_medical_specializations f on e.specialization_id=f.specialization_id ");
		query.append(" where mobile=" + mobile);

		return jdbcTemplate.queryForObject(query.toString(), (rs, rowNum) -> {
			Doctor_Details pd = new Doctor_Details();
			pd.setId(rs.getInt("doctor_id"));
			pd.setName(rs.getString("name"));
			pd.setMobile(rs.getString("mobile"));
			pd.setGender(rs.getString("gender_name"));
			pd.setEmail(rs.getString("email"));
			pd.setDept(rs.getString("dept_name"));
			pd.setAbout(rs.getString("about"));
			pd.setLicense(rs.getString("license"));
			pd.setSpecializations(rs.getString("specialization"));
			pd.setImg(rs.getString("img"));
			return pd;
		});
	}

	@Transactional(rollbackFor = Throwable.class)
	public int profileUpdate(Map<String, Object> profile) {
		StringBuffer query = new StringBuffer();
		query.append(
				" update telemedicine_doctors.tbl_doctor_registration a join telemedicine_doctors.tbl_about_doctor b on a.doctor_id=b.doctor_id ");
		query.append(" set a.name=?,b.about=?,a.email=? where a.doctor_id=? ");
		int rowsUp = jdbcTemplate.update(query.toString(),
				new Object[] { profile.get("name"), profile.get("about"), profile.get("email"), profile.get("id") });
		if (rowsUp != 2) {
			throw new RuntimeException("Error during profile updation");
		}
		return rowsUp;
	}

	@Transactional(rollbackFor = Throwable.class)
	public String profilePicUpdate(int doctorId, MultipartFile img) throws IllegalStateException, IOException {
		if (profilePicRemove(doctorId) == 1) {
			logger.debug("Existing image deleted successfully");
		} else {
			logger.debug("Failed to delete the existing image");
			throw new RuntimeException("Failed to delete existing img of doctor " + doctorId);
		}

		String name = img.getOriginalFilename();
		String[] a = name.split("\\.");
		String fileName = doctorId + "." + a[a.length - 1];
		String filePath = longPath + fileName;
		String query = "UPDATE telemedicine_doctors.tbl_doctor_registration SET IMG_PATH=? WHERE DOCTOR_ID=?";
		int rowsUpdated = jdbcTemplate.update(query, new Object[] { fileName, doctorId });
		if (rowsUpdated != 1) {
			throw new RuntimeException("Error during profile pic updation");
		}
		File parentDirectory = new File(filePath).getParentFile();
		if (parentDirectory != null && !parentDirectory.exists()) {
			parentDirectory.mkdirs();
		}
		Path path = Paths.get(filePath);
		Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		return shortPath + fileName;
	}

	@Transactional(rollbackFor = Throwable.class)
	public int profilePicRemove(int doctorId) {
		String query = "UPDATE telemedicine_doctors.tbl_doctor_registration SET IMG_PATH=NULL WHERE DOCTOR_ID=?";
		String imgPathQuery = "select img_path from telemedicine_doctors.tbl_doctor_registration where doctor_id=?";
		String imgPath = jdbcTemplate.queryForObject(imgPathQuery, String.class, new Object[] { doctorId });

		int rowsUpdated = jdbcTemplate.update(query, doctorId);
		if (rowsUpdated != 1) {
			throw new RuntimeException(
					"Error removing profile pic. Expected 1 row to be updated, but found " + rowsUpdated);
		}

		if (imgPath != null) {
			String filePath = longPath + imgPath;
			File file = new File(filePath);
			if (file.delete()) {
				logger.debug("File deleted successfully");
			} else {
				logger.debug("Failed to delete the file");
				throw new RuntimeException("Failed to delete existing img of doctor " + doctorId);
			}
		}

		return rowsUpdated;
	}

	public Map<String, Object> profileHistory(int doctorId) {
		Map<String, Object> completeData = new HashMap<>();

		Map<String, Object> profileData = getDoctorData(doctorId);
		Map<String, Object> RatingReviews = RatingReviews(doctorId);
		List<Map<String, Object>> patientReviews = patientReviews(doctorId);

		completeData.put("profileData", profileData);
		completeData.put("RatingReviews", RatingReviews);
		completeData.put("patientReviews", patientReviews);

		return completeData;
	}

	public Map<String, Object> getDoctorData(int doctorId) {
		StringBuffer query = new StringBuffer();
		query.append(
				" select a.doctor_id,a.name doctor_name,a.experience,a.cost,d.dept_name,count(distinct(b.patient_id)) patients,avg(c.rating) rating,if(a.img_path=null,null,concat('/TeleMedicineDocuments/DoctorPics/',a.img_path)) img ");
		query.append(" ,group_concat(distinct(f.shortname)) specialization ");
		query.append(" from telemedicine_doctors.tbl_doctor_registration a ");
		query.append(
				" join telemedicine_master.tbl_appointments b on b.doctor_id=a.doctor_id and b.status in ('A','C','RS')");
		query.append(" left join telemedicine_master.tbl_appointment_rating c on c.doctor_id=a.doctor_id ");
		query.append(" left join telemedicine_master.tbl_medical_departments d on a.dept_id=d.dept_id ");
		query.append(" left join telemedicine_doctors.tbl_doctor_specializations e on e.doctor_id=a.doctor_id ");
		query.append(
				" left join telemedicine_master.tbl_medical_specializations f on e.specialization_id=f.specialization_id ");
		query.append(" where a.doctor_id=" + doctorId);

		return jdbcTemplate.queryForMap(query.toString());
	}

	public Map<String, Object> RatingReviews(int doctorId) {
		StringBuffer query = new StringBuffer();
		query.append(
				" select count(distinct(b.patient_id)) total_patients,avg(c.rating) avg_rating,count(c.rating) reviews_count ");
		query.append(" from telemedicine_doctors.tbl_doctor_registration a ");
		query.append(
				" left join telemedicine_master.tbl_appointments b on a.doctor_id=b.doctor_id and b.status in ('A','C','RS') ");
		query.append(" left join telemedicine_master.tbl_appointment_rating c on b.appointment_id=c.appointment_id ");
		query.append(" where a.doctor_id=" + doctorId);

		return jdbcTemplate.queryForMap(query.toString());
	}

	public List<Map<String, Object>> patientReviews(int doctorId) {
		StringBuffer query = new StringBuffer();
		query.append(
				" select b.patient_id,b.name patient_name,a.rating,a.comments,if(date(a.lup_date)='0000-00-00','',date(a.lup_date)) date ");
		query.append(" from telemedicine_master.tbl_appointment_rating a ");
		query.append(" left join telemedicine_patients.tbl_patient_registration b on a.patient_id=b.patient_id ");
		query.append(" where a.doctor_id=" + doctorId);

		return jdbcTemplate.queryForList(query.toString());
	}

	@Transactional(rollbackFor = Throwable.class)
	public int timeChangeForDate(Map<String, Object> change) {
		String query = " INSERT into telemedicine_doctors.tbl_doctor_timing_changes(doctor_id,time_change_date,from_time,to_time,session_time) values(?,?,?,?,?) ";
		int rowUp = jdbcTemplate.update(query, new Object[] { change.get("doctorId"), change.get("date"),
				change.get("from"), change.get("to"), change.get("time") });
		if (rowUp != 1) {
			throw new RuntimeException();
		}
		return rowUp;
	}

	@Transactional(rollbackFor = Throwable.class)
	public int doctorHolidays(Map<String, Object> holidays) {
		String query = " INSERT into telemedicine_doctors.tbl_doctor_holidays(doctor_id,holiday_date,from_date,to_date,comment) values(?,?,?,?,?) ";

		String fromDate = holidays.get("fromDate").toString();
		String toDate = holidays.get("toDate").toString();
		LocalDate from = LocalDate.parse(fromDate);
		LocalDate to = LocalDate.parse(toDate);
		List<String> holiDates = new ArrayList<String>();

		while (!from.isAfter(to)) {
			holiDates.add(from.toString());
			from = from.plusDays(1);
		}

		int[] updateCounts = jdbcTemplate.execute((Connection con) -> {
			PreparedStatement stmt = con.prepareStatement(query);
			for (String holidate : holiDates) {
				stmt.setInt(1, (int) holidays.get("doctorId"));
				stmt.setString(2, holidate);
				stmt.setString(3, fromDate);
				stmt.setString(4, toDate);
				stmt.setString(5, holidays.get("comment").toString());
				stmt.addBatch();
			}
			return stmt.executeBatch();
		});

		int totalRowsUpdated = 0;
		for (int updateCount : updateCounts) {
			totalRowsUpdated += updateCount;
		}

		if (totalRowsUpdated != holiDates.size()) {
			throw new RuntimeException("Failed to insert all holidates");
		}

		return updateCounts.length;
	}

}
