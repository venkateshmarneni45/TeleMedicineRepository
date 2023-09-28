package com.example.TeleMedicine.PatientLogin;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.TeleMedicine.SmsServiceProvider.SmsService;

@Repository
public class PatientLoginRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private SmsService smsService;
	private Logger logger = LoggerFactory.getLogger(PatientLoginRepository.class);

	@Transactional(rollbackFor = Throwable.class)
	public Map<String, Object> otpRequest(String mobile) throws ParseException {
		Map<String, Object> m = new HashMap<>();
		if (mobile.length() == 10) {
			int rowCount = jdbcTemplate
					.queryForObject("SELECT COUNT(*) FROM telemedicine_patients.tbl_patient_registration WHERE MOBILE=" + mobile, Integer.class);
			if (rowCount == 1) {
				StringBuffer otp = new StringBuffer();
				Random random = new Random();
				for (int i = 0; i < 4; i++) {
					otp.append(String.valueOf(random.nextInt(9)));
				}
				logger.debug("Login OTP for " + mobile + " is " + otp);
				int otpUp = jdbcTemplate.update("INSERT INTO telemedicine_patients.tbl_patient_login_otp(MOBILE,OTP) values(?,?)",
						new Object[] { mobile, otp });
				if (otpUp != 1) {
					throw new RuntimeException("Error inserting login otp");
				}
				logger.debug(mobile + " Login OTP inserted to database at " + LocalDateTime.now());
				m = smsService.send(otp.toString(), mobile,"PatientLogin");
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
		String query = "select otp,if(TIME_TO_SEC(timediff(current_timestamp,lup_date))>300,0,1) validity from telemedicine_patients.tbl_patient_login_otp where mobile=? order by lup_date desc limit 1";
		return jdbcTemplate.queryForMap(query, new Object[] { otpVerify.get("mobile") });
	}
}
