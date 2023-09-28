package com.example.TeleMedicine.CommonServices;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.poi.util.IOUtils;
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
public class SpareRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private SmsService smsService;
	private Logger logger = LoggerFactory.getLogger(SpareRepository.class);

	public List<Map<String, Object>> getDepartments() {
		List<Map<String, Object>> departments = jdbcTemplate
				.queryForList("SELECT * FROM telemedicine_master.tbl_medical_departments");
		return departments.stream().map(dept -> {
			Map<String, Object> m = dept;
			m.get("img_path").toString();
			m.put("img_path", "/TeleMedicineDocuments/DeptPics/" + m.get("img_path").toString());
			return m;
		}).collect(Collectors.toList());
	}

	public List<Map<String, Object>> getMedicalTests() {
		return jdbcTemplate.queryForList("SELECT test_id,test_name FROM telemedicine_master.tbl_medical_tests");
	}

	public List<Map<String, Object>> getStates() {
		return jdbcTemplate.queryForList(
				"SELECT locationid,locationname FROM telemedicine_master.tbl_location where locationtype=1 and parent=0 and status=1001");
	}

	public List<Map<String, Object>> getCities(int state) {
		return jdbcTemplate.queryForList(
				"SELECT locationid,locationname FROM telemedicine_master.tbl_location where locationtype=2 and parent=? and status=1001",
				new Object[] { state });
	}

	public List<Map<String, Object>> getComplaints() {
		return jdbcTemplate
				.queryForList("SELECT complaint_id,complaint_name FROM telemedicine_master.tbl_medical_complaints ");
	}

	public List<Map<String, Object>> getSpecializations() {
		return jdbcTemplate.queryForList(
				"SELECT specialization_id,fullname,shortname FROM telemedicine_master.tbl_medical_specializations");
	}

	public List<Map<String, Object>> downloadCaseSheets(int appointmentId) {
		String query = "select appointment_id,sheet_name,sheet_path from telemedicine_reports.tbl_case_sheets where appointment_id=? group by appointment_id,sheet_name";
		List<Map<String,Object>> sheets = jdbcTemplate.queryForList(query, new Object[] { appointmentId });
		List<Map<String, Object>> caseSheets = new ArrayList<>();
		sheets.parallelStream().forEach(sheet -> {
			File file = new File((String)sheet.get("sheet_path"));
			FileInputStream fileInputStream;
			try {
				Map<String, Object> m = new HashMap<>();
				fileInputStream = new FileInputStream(file);
				m.put("filename", sheet.get("sheet_name"));
				m.put("filedata", Base64.getEncoder().encodeToString(IOUtils.toByteArray(fileInputStream)));
				caseSheets.add(m);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		});
		return caseSheets;
	}

	public List<Map<String, Object>> downloadMedicalReports(int appointmentId) {
		StringBuffer query = new StringBuffer();
		query.append(" select a.appointment_id,b.test_name,a.report_name,a.report_path ");
		query.append(" from telemedicine_reports.tbl_medical_reports a ");
		query.append(" left join telemedicine_master.tbl_medical_tests b on a.test_id=b.test_id ");
		query.append(" where a.appointment_id=? ");
		query.append(" group by a.appointment_id,a.test_id,a.report_name ");

		List<Map<String, Object>> reports = jdbcTemplate.queryForList(query.toString(), new Object[] { appointmentId });
		List<Map<String, Object>> medicalReports = new ArrayList<>();
		reports.parallelStream().forEach(report -> {
			File file = new File((String)report.get("report_path"));
			FileInputStream fileInputStream;
			try {
				Map<String, Object> m = new HashMap<>();
				fileInputStream = new FileInputStream(file);
				m.put("appointmentid", report.get("appointment_id"));
				m.put("testname", report.get("test_name"));
				m.put("filename", report.get("report_name"));
				m.put("filedata", Base64.getEncoder().encodeToString(IOUtils.toByteArray(fileInputStream)));
				medicalReports.add(m);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		});
		return medicalReports;
	}

	@Transactional(rollbackFor = Throwable.class)
	public Map<String, Object> otpRequest(String mobile) throws ParseException {
		Map<String, Object> m = new HashMap<>();
		if (mobile.length() == 10) {
			Map<String, Object> doctor = jdbcTemplate.queryForMap(
					"SELECT STATUS,COUNT(*) COUNT FROM telemedicine_doctors.tbl_doctor_registration WHERE MOBILE="
							+ mobile);
			if ((long) doctor.get("COUNT") == 0) {
				StringBuffer otp = new StringBuffer();
				Random random = new Random();
				for (int i = 0; i < 4; i++) {
					otp.append(String.valueOf(random.nextInt(9)));
				}
				logger.debug("Registration OTP for " + mobile + " is " + otp);
				int otpUp = jdbcTemplate.update(
						"INSERT INTO telemedicine_doctors.tbl_doctor_registration_otp(MOBILE,OTP) values(?,?)",
						new Object[] { mobile, otp });
				if (otpUp != 1) {
					throw new RuntimeException("Error inserting registration otp");
				}
				logger.debug(mobile + " Register OTP inserted to database at " + LocalDateTime.now());
				m = smsService.send(otp.toString(), mobile, "DoctorRegister");
				m.put("otp", otp.toString());
			} else {
				if ((int) doctor.get("status") == 1001) {
					m.put("status", 0);
				} else {
					m.put("status", 3);
				}
			}
		} else {
			m.put("status", 2);
		}
		return m;
	}

	public Map<String, Object> verifyOtp(Map<String, Object> otpVerify) {
		String query = "select otp,if(TIME_TO_SEC(timediff(current_timestamp,lup_date))>300,0,1) validity from telemedicine_doctors.tbl_doctor_registration_otp where mobile=? order by lup_date desc limit 1";
		return jdbcTemplate.queryForMap(query, new Object[] { otpVerify.get("mobile") });
	}

	@Transactional(rollbackFor = Throwable.class)
	public int registerDoctor(DoctorProfile doctorProfile) {
		String query1 = "INSERT INTO telemedicine_doctors.tbl_doctor_registration(name, mobile, email, gender_id, cost, license, experience, state, city,dept_id, register_date) VALUES (?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?)";
		String query2 = "INSERT INTO telemedicine_doctors.tbl_about_doctor(doctor_id, about) VALUES (?, ?)";

		KeyHolder doctorKeyHolder = new GeneratedKeyHolder();
		int rowUp1 = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, doctorProfile.getName());
			ps.setString(2, doctorProfile.getMobile());
			ps.setString(3, doctorProfile.getEmail());
			ps.setInt(4, doctorProfile.getGenderId());
			ps.setDouble(5, doctorProfile.getCost());
			ps.setString(6, doctorProfile.getLicense());
			ps.setInt(7, doctorProfile.getExperience());
			ps.setString(8, doctorProfile.getState());
			ps.setString(9, doctorProfile.getCity());
			ps.setInt(10, doctorProfile.getDeptId());
			ps.setString(11, LocalDate.now().toString());
			return ps;
		}, doctorKeyHolder);

		if (rowUp1 != 1) {
			throw new RuntimeException("Failed to register doctor");
		}
		int doctorId = doctorKeyHolder.getKey().intValue();

		int rowUp2 = jdbcTemplate.update(query2, doctorId, doctorProfile.getAbout());
		if (rowUp2 != 1) {
			throw new RuntimeException("Failed to register doctor");
		}

		int[] specializations = doctorProfile.getSpecialization();
		if (specializations.length > 0) {
			String deptsQuery = "INSERT INTO telemedicine_doctors.tbl_doctor_specializations(DOCTOR_ID,SPECIALIZATION_ID) VALUES (?, ?)";

			int[] updateCounts = jdbcTemplate.execute((Connection con) -> {
				PreparedStatement stmt = con.prepareStatement(deptsQuery);
				for (int spec : specializations) {
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

			if (totalRowsUpdated != specializations.length) {
				throw new RuntimeException("Failed to insert all specializations");
			}
		} else {
			logger.debug("No specializations for " + doctorId);
		}

		String timingsQuery = "INSERT INTO telemedicine_doctors.tbl_doctor_daily_timings(doctor_id,from_time,to_time,session_time) VALUES (?, ?,?,?)";
		int rowUp3 = jdbcTemplate.update(timingsQuery, new Object[] { doctorId, doctorProfile.getFromTime(),
				doctorProfile.getToTime(), doctorProfile.getSessionTime() });
		if (rowUp3 != 1) {
			throw new RuntimeException("Failed to register doctor");
		}

		return rowUp1;
	}

}
