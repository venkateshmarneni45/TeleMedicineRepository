package com.example.TeleMedicine.AdminPatients;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdminPatientsRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Value("${CaseSheets}")
	private String caseSheetPath;

	public List<Map<String, Object>> getPatientsList() {
		StringBuffer query = new StringBuffer();
		query.append(" select a.patient_id,a.name patient_name,a.mobile,a.age,a.height,a.weight ");
		query.append(
				" ,d.blood_group_name blood_group,if(a.img_path=null,null,concat('/TeleMedicineDocuments/PatientPics/',a.img_path)) img ");
		query.append(" ,c.gender_name gender,a.state,a.city,a.register_date date,count(*) appointments ");
		query.append(" from telemedicine_patients.tbl_patient_registration a ");
		query.append(" left join telemedicine_master.tbl_appointments b on a.patient_id=b.patient_id ");
		query.append(" left join telemedicine_master.tbl_gender c on a.gender_id=c.gender_id ");
		query.append(" left join telemedicine_master.tbl_blood_group d on a.blood_group_id=d.blood_group_id ");
		query.append(" group by a.patient_id ");

		return jdbcTemplate.queryForList(query.toString());
	}

	public Map<String, Object> getPatientProfile(int patientId) {
		StringBuffer profileQuery = new StringBuffer();
		profileQuery.append(" select a.patient_id,a.name patient_name,a.mobile,a.age,b.gender_name gender, ");
		profileQuery.append(
				" a.height,a.weight,a.state,a.city,c.blood_group_name blood_group,if(a.img_path=null,null,concat('/TeleMedicineDocuments/PatientPics/',a.img_path)) img ");
		profileQuery.append(" from telemedicine_patients.tbl_patient_registration a ");
		profileQuery.append(" left join telemedicine_master.tbl_gender b on a.gender_id=b.gender_id ");
		profileQuery.append(" left join telemedicine_master.tbl_blood_group c on a.blood_group_id=c.blood_group_id ");
		profileQuery.append(" where patient_id=" + patientId);

		StringBuffer appointmentsHistoryQuery = new StringBuffer();
		appointmentsHistoryQuery.append(
				" select a.appointment_id,DATE_FORMAT(a.appointment_session, '%d %M, %Y  %h:%i %p') appointment_session,a.status appointment_status,c.doctor_id,c.name doctor_name, ");
		appointmentsHistoryQuery
				.append(" if(c.img_path=null,null,concat('/TeleMedicineDocuments/DoctorPics/',c.img_path)) img ");
		appointmentsHistoryQuery.append(" from telemedicine_master.tbl_appointments a ");
		appointmentsHistoryQuery
				.append(" left join telemedicine_patients.tbl_patient_registration b on a.patient_id=b.patient_id ");
		appointmentsHistoryQuery
				.append(" left join telemedicine_doctors.tbl_doctor_registration c on a.doctor_id=c.doctor_id ");
		appointmentsHistoryQuery.append(" where a.patient_id=? and a.appointment_session<now() ");

		StringBuffer medicalHistoryQuery = new StringBuffer();
		medicalHistoryQuery.append(" select b.complaint_name");
		medicalHistoryQuery.append(" from telemedicine_patients.tbl_patient_medical_history a ");
		medicalHistoryQuery
				.append(" left join telemedicine_master.tbl_medical_complaints b on a.complaint_id=b.complaint_id ");
		medicalHistoryQuery.append(" where a.patient_id=? ");

		List<Map<String, Object>> appointmentsHistory = jdbcTemplate.queryForList(appointmentsHistoryQuery.toString(),
				new Object[] { patientId });

		List<String> medicalHistory = jdbcTemplate.queryForList(medicalHistoryQuery.toString(), String.class,
				new Object[] { patientId });

		Map<String, Object> profile = jdbcTemplate.queryForMap(profileQuery.toString());

		Map<String, Object> patientProfile = new LinkedHashMap<>();
		patientProfile.put("profile", profile);
		patientProfile.put("appointmentsHistory", appointmentsHistory);
		patientProfile.put("medicalHistory", medicalHistory);

		return patientProfile;
	}
}
