package com.example.TeleMedicine.PatientAppointments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PatientAppointmentRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Value("${MedicalReports}")
	private String medicalReportsPath;

	@Transactional(rollbackFor = Throwable.class)
	public int bookAppointment(Map<String, Object> book) {
		String query="select status from telemedicine_doctors.tbl_doctor_registration where doctor_id="+book.get("doctorId");
		int status=jdbcTemplate.queryForObject(query,Integer.class);
		if(status==1001) {
			int rowInserted = jdbcTemplate.update(
					"INSERT INTO telemedicine_master.tbl_appointments(patient_id,doctor_id,booked_date,appointment_session) values(?,?,?,?)",
					new Object[] { book.get("patientId"), book.get("doctorId"), LocalDate.now(), book.get("session") });
			if (rowInserted != 1) {
				throw new RuntimeException();
			}
			return rowInserted;
		}
		return 0;
	}

	public List<Map<String, Object>> getAppointmentsHistory(int patientId) {
		StringBuffer query = new StringBuffer();
		query.append(
				" select b.appointment_id,c.doctor_id,c.name doctor_name,d.dept_name,b.appointment_session session_time,b.status appointment_status ");
		query.append(" from telemedicine_patients.tbl_patient_registration a ");
		query.append(
				" join telemedicine_master.tbl_appointments b on a.patient_id=b.patient_id and b.status IN ('C','SC') ");
		query.append(" left join telemedicine_doctors.tbl_doctor_registration c on c.doctor_id=b.doctor_id ");
		query.append(" left join telemedicine_master.tbl_medical_departments d on c.dept_id=d.dept_id ");
		query.append(" where a.patient_id=? ");
		return jdbcTemplate.queryForList(query.toString(), new Object[] { patientId });
	}

	public List<Map<String, Object>> getUpcomingAppointments(int patientId) {
		StringBuffer query = new StringBuffer();
		query.append(
				" select b.appointment_id,c.doctor_id,c.name doctor_name,d.dept_name,b.appointment_session session_time,b.status appointment_status ");
		query.append(" from telemedicine_patients.tbl_patient_registration a ");
		query.append(
				" join telemedicine_master.tbl_appointments b on a.patient_id=b.patient_id and b.status IN ('A','RS') ");
		query.append(" left join telemedicine_doctors.tbl_doctor_registration c on c.doctor_id=b.doctor_id ");
		query.append(" left join telemedicine_master.tbl_medical_departments d on c.dept_id=d.dept_id ");
		query.append(" where a.patient_id=? ");
		return jdbcTemplate.queryForList(query.toString(), new Object[] { patientId });
	}

	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Throwable.class)
	public int uploadMedicalReport(Map<String, Object> medicalReports) {
		int appointmentId = (int) medicalReports.get("appointmentId");
		List<Map<String, Object>> reports = (List<Map<String, Object>>) medicalReports.get("medicalreports");
		
		String query = "Insert into telemedicine_reports.tbl_medical_reports(appointment_id,test_id,report_name,report_path) values(?,?,?,?)";
		int[] updateCounts = jdbcTemplate.execute((Connection con) -> {
			PreparedStatement stmt = con.prepareStatement(query);
			for (Map<String, Object> report : reports) {
				String reportPath=medicalReportsPath+appointmentId+"//"+report.get("name");
				stmt.setInt(1, appointmentId);
				stmt.setInt(2, (int) report.get("id"));
				stmt.setString(3, (String) report.get("name"));
				stmt.setString(4, reportPath);
				stmt.addBatch();
			}
			return stmt.executeBatch();
		});

		int totalRowsUpdated = 0;
		for (int updateCount : updateCounts) {
			totalRowsUpdated += updateCount;
		}
		if (totalRowsUpdated != reports.size()) {
			throw new RuntimeException("Failed to insert all medical reports");
		}

		reports.parallelStream().forEach(report -> {
			try {
				String filePath=medicalReportsPath+appointmentId+"//"+report.get("name");
				byte[] decodedBytes = Base64.getDecoder().decode((String) report.get("file"));
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

	public List<Timings> fetchBookingTimes(int doctorId, String from, String to) {
		String holidays = "SELECT holiday_date from telemedicine_doctors.tbl_doctor_holidays where doctor_id=? and holiday_date between ? and ?";
		List<String> doctorHolidays = jdbcTemplate.queryForList(holidays, String.class,
				new Object[] { doctorId, from, to });

		List<String> timeChangeDates = new ArrayList<>();

		String changedDates = "SELECT * from telemedicine_doctors.tbl_doctor_timing_changes where doctor_id=? and time_change_date between ? and ?";
		List<Timings> changedTimings = jdbcTemplate.query(changedDates, (rs, rowNum) -> {
			Timings dh = new Timings();
			String date = rs.getString("time_change_date");
			timeChangeDates.add(date);
			dh.setDate(date);
			dh.setFromTime(rs.getString("from_time"));
			dh.setToTime(rs.getString("to_time"));
			dh.setSessionTime(rs.getInt("session_time"));
			return dh;
		}, new Object[] { doctorId, from, to });

		Map<String, Timings> timingsMap = new LinkedHashMap<>();
		for (Timings timing : changedTimings) {
			timingsMap.put(timing.getDate(), timing);
		}

		String casualDates = "SELECT * from telemedicine_doctors.tbl_doctor_daily_timings where doctor_id=?";
		Timings normalTimings = jdbcTemplate.queryForObject(casualDates, (rs, rowNum) -> {
			Timings dh = new Timings();
			dh.setFromTime(rs.getString("from_time"));
			dh.setToTime(rs.getString("to_time"));
			dh.setSessionTime(rs.getInt("session_time"));
			return dh;
		}, new Object[] { doctorId });

		LocalDate fromDate = LocalDate.parse(from);
		LocalDate toDate = LocalDate.parse(to);
		List<String> dates = new ArrayList<String>();

		while (!fromDate.isAfter(toDate)) {
			dates.add(fromDate.toString());
			fromDate = fromDate.plusDays(1);
		}

		List<Timings> finalDates = new ArrayList<>();
		dates.stream().forEach(date -> {
			Timings t = new Timings();
			t.setDate(date);
			if (doctorHolidays.contains(date)) {
				t.setDayType("HD");
			} else if (timeChangeDates.contains(date)) {
				Timings timeChangePojo = timingsMap.get(date);
				t.setFromTime(timeChangePojo.getFromTime());
				t.setToTime(timeChangePojo.getToTime());
				t.setSessionTime(timeChangePojo.getSessionTime());
			} else {
				t.setFromTime(normalTimings.getFromTime());
				t.setToTime(normalTimings.getToTime());
				t.setSessionTime(normalTimings.getSessionTime());
			}
			finalDates.add(t);
		});

		return finalDates;
	}
}
