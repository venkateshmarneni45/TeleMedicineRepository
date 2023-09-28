package com.example.TeleMedicine.DoctorAppointments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DoctorAppointmentRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Value("${CaseSheets}")
	private String caseSheetPath;

	public List<Map<String, Object>> getAppointmentsHistory(int doctorId) {
		StringBuffer query = new StringBuffer();
		query.append(
				" select b.appointment_id,c.patient_id,c.name patient_name,c.age,d.gender_name gender,b.appointment_session session_time,b.status appointment_status ");
		query.append(" from telemedicine_doctors.tbl_doctor_registration a ");
		query.append(
				" join telemedicine_master.tbl_appointments b on a.doctor_id=b.doctor_id and b.status IN ('C','SC') ");
		query.append(" left join telemedicine_patients.tbl_patient_registration c on b.patient_id=c.patient_id ");
		query.append(" left join telemedicine_master.tbl_gender d on c.gender_id=d.gender_id ");
		query.append(" where a.doctor_id=? ");
		return jdbcTemplate.queryForList(query.toString(), new Object[] { doctorId });
	}

	public List<Map<String, Object>> getUpcomingAppointments(int doctorId) {
		StringBuffer query = new StringBuffer();
		query.append(
				" select b.appointment_id,c.patient_id,c.name patient_name,c.age,d.gender_name gender,b.appointment_session session_time,b.status appointment_status ");
		query.append(" from telemedicine_doctors.tbl_doctor_registration a ");
		query.append(
				" join telemedicine_master.tbl_appointments b on a.doctor_id=b.doctor_id and b.status IN ('A','RS') ");
		query.append(" left join telemedicine_patients.tbl_patient_registration c on b.patient_id=c.patient_id ");
		query.append(" left join telemedicine_master.tbl_gender d on c.gender_id=d.gender_id ");
		query.append(" where a.doctor_id=? ");
		return jdbcTemplate.queryForList(query.toString(), new Object[] { doctorId });
	}

	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Throwable.class)
	public int uploadCaseSheet(Map<String, Object> caseSheets) {
		int appointmentId = (int) caseSheets.get("appointmentId");
		List<Map<String, Object>> sheets = (List<Map<String, Object>>) caseSheets.get("casesheets");

		String query = "Insert into telemedicine_reports.tbl_case_sheets(appointment_id,sheet_name,sheet_path) values(?,?,?)";
		int[] updateCounts = jdbcTemplate.execute((Connection con) -> {
			PreparedStatement stmt = con.prepareStatement(query);
			for (Map<String, Object> sheet : sheets) {
				String reportPath=caseSheetPath+appointmentId+"//"+sheet.get("name");
				stmt.setInt(1, appointmentId);
				stmt.setString(2, (String) sheet.get("name"));
				stmt.setString(3, reportPath);
				stmt.addBatch();
			}
			return stmt.executeBatch();
		});

		int totalRowsUpdated = 0;
		for (int updateCount : updateCounts) {
			totalRowsUpdated += updateCount;
		}

		if (totalRowsUpdated != sheets.size()) {
			throw new RuntimeException("Failed to insert all case sheets");
		}

		sheets.parallelStream().forEach(sheet -> {
			try {
				String filePath = caseSheetPath+appointmentId+"//"+sheet.get("name");
				byte[] decodedBytes = Base64.getDecoder().decode((String) sheet.get("file"));
				File parentDirectory = new File(filePath).getParentFile();
				if (parentDirectory != null && !parentDirectory.exists()) {
					parentDirectory.mkdirs();
				}
				try (FileOutputStream fos = new FileOutputStream(filePath)) {
					fos.write(decodedBytes);
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		});

		return updateCounts.length;
	}

	@Transactional(rollbackFor = Throwable.class)
	public int postponeAppointment(int appointmentId, String dateTime) {
		int rowUp = jdbcTemplate.update(" update telemedicine_master.tbl_appointments set appointment_session='"
				+ Timestamp.valueOf(dateTime) + "' , status='RS'" + " where appointment_id=" + appointmentId);
		if (rowUp != 1) {
			throw new RuntimeException();
		}
		return rowUp;
	}
}
