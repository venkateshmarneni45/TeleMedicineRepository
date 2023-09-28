package com.example.TeleMedicine.PatientRegistration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.TeleMedicine.SmsServiceProvider.SmsService;

@Repository
public class PatientRegisterRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private SmsService smsService;
	private Logger logger = LoggerFactory.getLogger(PatientRegisterRepository.class);

	@Transactional(rollbackFor = Throwable.class)
	public Map<String, Object> otpRequest(String mobile) throws ParseException {
		Map<String, Object> m = new HashMap<>();
		if (mobile.length() == 10) {
			int rowCount = jdbcTemplate
					.queryForObject("SELECT COUNT(*) FROM telemedicine_patients.tbl_patient_registration WHERE MOBILE=" + mobile, Integer.class);
			if (rowCount == 0) {
				StringBuffer otp = new StringBuffer();
				Random random = new Random();
				for (int i = 0; i < 4; i++) {
					otp.append(String.valueOf(random.nextInt(9)));
				}
				logger.debug("Registration OTP for " + mobile + " is " + otp);
				int otpUp = jdbcTemplate.update("INSERT INTO telemedicine_patients.tbl_patient_registration_otp(MOBILE,OTP) values(?,?)",
						new Object[] { mobile, otp });
				if (otpUp != 1) {
					throw new RuntimeException("Error inserting registration otp");
				}
				logger.debug(mobile + " Register OTP inserted to database at " + LocalDateTime.now());
				m = smsService.send(otp.toString(), mobile,"PatientRegister");
				m.put("otp", otp.toString());
			} else {
				m.put("status", 0);
			}
		} else {
			m.put("status", 2);
		}
		return m;
	}

	public Map<String, Object> verifyOtp(Map<String, Object> otpVerify) {
		String query = "select otp,if(TIME_TO_SEC(timediff(current_timestamp,lup_date))>300,0,1) validity from telemedicine_patients.tbl_patient_registration_otp where mobile=? order by lup_date desc limit 1";
		return jdbcTemplate.queryForMap(query, new Object[] { otpVerify.get("mobile") });
	}

	@Transactional(rollbackFor = Throwable.class)
	public int register(PatientRegister register) {
		String insertPatientQuery = "INSERT INTO telemedicine_patients.tbl_patient_registration(NAME, MOBILE, AGE, GENDER_ID, HEIGHT, WEIGHT,STATE,CITY, BLOOD_GROUP_ID, REGISTER_DATE) VALUES (?, ?, ?,?,?, ?, ?, ?, ?, ?)";
		KeyHolder patientKeyHolder = new GeneratedKeyHolder();
		int bloodGroup = register.getBloodGroupId();
		Object bloodGroupValue = (bloodGroup == 0) ? null : bloodGroup;
		int regUp = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(insertPatientQuery, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, register.getName());
			ps.setString(2, register.getMobile());
			ps.setInt(3, register.getAge());
			ps.setInt(4, register.getGenderId());
			ps.setString(5, register.getHeight());
			ps.setString(6, register.getWeight());
			ps.setString(7, register.getState());
			ps.setString(8, register.getCity());
			ps.setObject(9, bloodGroupValue);
			ps.setString(10, LocalDate.now().toString());
			return ps;
		}, patientKeyHolder);

		if (regUp != 1) {
			throw new RuntimeException("Failed to register patient");
		}
		int patientId = patientKeyHolder.getKey().intValue();
		
		int[] medicalHistory=register.getMedicalHistory();
		
		if (medicalHistory.length > 0) {
			String medicalHistoryQuery = "INSERT INTO telemedicine_patients.tbl_patient_medical_history(PATIENT_ID, COMPLAINT_ID) VALUES (?, ?)";

			int[] updateCounts = jdbcTemplate.execute((Connection con) -> {
				PreparedStatement stmt = con.prepareStatement(medicalHistoryQuery);
				for (int history : medicalHistory) {
					stmt.setInt(1, patientId);
					stmt.setInt(2, history);
					stmt.addBatch();
				}
				return stmt.executeBatch();
			});

			int totalRowsUpdated = 0;
			for (int updateCount : updateCounts) {
				totalRowsUpdated += updateCount;
			}

			if (totalRowsUpdated != medicalHistory.length) {
				throw new RuntimeException("Failed to insert all medical history");
			}
		} else {
			logger.debug("No Medical History for " + patientId);
		}
		return regUp;
	}
}
