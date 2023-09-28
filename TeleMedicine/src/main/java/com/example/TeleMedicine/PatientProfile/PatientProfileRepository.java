package com.example.TeleMedicine.PatientProfile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class PatientProfileRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Value("${PatientPics}")
	private String longPath;
	@Value("${PatientImgPath}")
	private String shortPath;
	private Logger logger = LoggerFactory.getLogger(PatientProfileRepository.class);

	public Patient_Profile getPatientData(String mobile) {
		StringBuffer query = new StringBuffer();
		query.append(" select a.patient_id,a.name,a.mobile,a.age,b.gender_name gender, ");
		query.append(
				" a.height,a.weight,a.state,a.city,c.blood_group_name blood_group,if(a.img_path=null,null,concat('/TeleMedicineDocuments/PatientPics/',a.img_path)) img ");
		query.append(" from telemedicine_patients.tbl_patient_registration a ");
		query.append(" left join telemedicine_master.tbl_gender b on a.gender_id=b.gender_id ");
		query.append(" left join telemedicine_master.tbl_blood_group c on a.blood_group_id=c.blood_group_id ");
		query.append(" where mobile=" + mobile);

		return jdbcTemplate.queryForObject(query.toString(), (rs, rowNum) -> {
			Patient_Profile pd = new Patient_Profile();
			pd.setId(rs.getInt("patient_id"));
			pd.setName(rs.getString("name"));
			pd.setMobile(rs.getString("mobile"));
			pd.setAge(rs.getInt("age"));
			pd.setGender(rs.getString("gender"));
			pd.setHeight(rs.getString("height"));
			pd.setWeight(rs.getString("weight"));
			pd.setState(rs.getString("state"));
			pd.setCity(rs.getString("city"));
			pd.setBloodGroup(rs.getString("blood_group"));
			pd.setImg(rs.getString("img"));
			return pd;
		});
	}

	@Transactional(rollbackFor = Throwable.class)
	public int profileUpdate(Patient_Updation patientUpdation) {
		String query = "UPDATE telemedicine_patients.tbl_patient_registration SET NAME=?,AGE=?,HEIGHT=?,WEIGHT=?,BLOOD_GROUP_ID=? WHERE PATIENT_ID=?";
		int rowsUp = jdbcTemplate.update(query,
				new Object[] { patientUpdation.getName(), patientUpdation.getAge(), patientUpdation.getHeight(),
						patientUpdation.getWeight(), patientUpdation.getBloodGroupId(), patientUpdation.getId() });
		if (rowsUp != 1) {
			throw new RuntimeException("Error during profile updation");
		}
		return rowsUp;
	}

	@Transactional(rollbackFor = Throwable.class)
	public String profilePicUpdate(int patientId, MultipartFile img) throws IllegalStateException, IOException {
		if (profilePicRemove(patientId) == 1) {
			logger.debug("File deleted successfully");
		} else {
			logger.debug("Failed to delete the file");
			throw new RuntimeException("Failed to delete existing img of patient " + patientId);
		}

		String name = img.getOriginalFilename();
		String[] a = name.split("\\.");
		String fileName = patientId + "." + a[a.length - 1];
		String filePath = longPath + fileName;
		String query = "UPDATE telemedicine_patients.tbl_patient_registration SET IMG_PATH=? WHERE PATIENT_ID=?";
		int rowsUpdated = jdbcTemplate.update(query, new Object[] { fileName, patientId });
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
	public int profilePicRemove(int patientId) {
		String query = "UPDATE telemedicine_patients.tbl_patient_registration SET IMG_PATH=NULL WHERE PATIENT_ID=?";
		String imgPathQuery = "select img_path from telemedicine_patients.tbl_patient_registration where patient_id=?";
		String imgPath = jdbcTemplate.queryForObject(imgPathQuery, String.class, new Object[] { patientId });

		int rowsUpdated = jdbcTemplate.update(query, patientId);
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
				throw new RuntimeException("Failed to delete existing img of patient " + patientId);
			}
		}

		return rowsUpdated;
	}
}
