package com.example.TeleMedicine.CommonServices;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpareService {
	@Autowired
	private SpareRepository spareRepository;
	private Logger logger = LoggerFactory.getLogger(SpareService.class);

	public synchronized Map<String, Object> getDepartments() {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", spareRepository.getDepartments());
			m.put("flag", true);
			m.put("status", 1);
			logger.debug("Departments fetched successful");

		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching departments");
			logger.error("departments fetching failed");
		}
		return m;
	}

	public synchronized Map<String, Object> getMedicalTests() {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", spareRepository.getMedicalTests());
			m.put("flag", true);
			m.put("status", 1);
			logger.debug("Medical Tests fetched successful");

		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching medical tests");
			logger.error("medical tests fetching failed");
		}
		return m;
	}

	public synchronized Map<String, Object> getStates() {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", spareRepository.getStates());
			m.put("flag", true);
			m.put("status", 1);
			logger.debug("States fetching successful");

		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching states");
			logger.error("states fetching failed");
		}
		return m;
	}

	public synchronized Map<String, Object> getCities(int state) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", spareRepository.getCities(state));
			m.put("flag", true);
			m.put("status", 1);
			logger.debug("Cities fetching successful");

		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching cities");
			logger.error("Cities fetching failed");
		}
		return m;
	}

	public synchronized Map<String, Object> getComplaints() {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", spareRepository.getComplaints());
			m.put("flag", true);
			m.put("status", 1);
			logger.debug("Complaints fetching successful");

		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching complaints");
			logger.error("Complaints fetching failed");
		}
		return m;
	}

	public synchronized Map<String, Object> getSpecializations() {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", spareRepository.getSpecializations());
			m.put("flag", true);
			m.put("status", 1);
			logger.debug("Specializations fetching successful");

		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching specializations");
			logger.error("Specializations fetching failed");
		}
		return m;
	}

	public synchronized Map<String, Object> downloadCaseSheets(int appointmentId) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", spareRepository.downloadCaseSheets(appointmentId));
			m.put("flag", true);
			m.put("status", 1);
			logger.debug(appointmentId + " case sheets fetched");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching case sheets");
		}
		return m;
	}

	public synchronized Map<String, Object> downloadMedicalReports(int appointmentId) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("message", spareRepository.downloadMedicalReports(appointmentId));
			m.put("flag", true);
			m.put("status", 1);
			logger.debug(appointmentId + " medical reports fetched");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("status", 0);
			m.put("message", "Error, fetching medical reports");
		}
		return m;
	}

	public synchronized Map<String, Object> otpRequest(String mobile) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			Map<String, Object> stat = spareRepository.otpRequest(mobile);
			String status = stat.get("status").toString();

			if (status.equalsIgnoreCase("0")) {
				m.put("message", "Doctor Exists, Please Login");
				m.put("flag", false);
				m.put("status", 0);
			} else if (status.equalsIgnoreCase("1")) {
				String error = stat.get("error").toString();

				if (error.equalsIgnoreCase("000")) {
					m.put("message", "OTP Sent Successfully");
					m.put("flag", true);
					m.put("status", 1);
					m.put("time", 5);
					logger.debug("Registration OTP sent to " + mobile);
				} else if (error.equalsIgnoreCase("13")) {
					m.put("message", "Mobile Number Does'nt Exists");
					m.put("flag", false);
					m.put("status", 0);
				} else {
					m.put("message", "Error, while sending OTP");
					m.put("flag", false);
					m.put("status", 0);
				}
			} else if (status.equalsIgnoreCase("2")) {
				m.put("message", "Mobile Number is Not Valid");
				m.put("flag", false);
				m.put("status", 0);
			} else if (status.equalsIgnoreCase("3")) {
				m.put("message", "Doctor is Inactive, Please activate");
				m.put("flag", false);
				m.put("status", 0);
			} else {
				m.put("message", "Error, while sending OTP");
				m.put("flag", false);
				m.put("status", 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("message", "Error, while sending OTP");
			m.put("status", 0);
			logger.error("Error sending register OTP to " + mobile);
		}
		return m;
	}

	public synchronized Map<String, Object> verifyOtp(Map<String, Object> otpVerify) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			Map<String, Object> otp = spareRepository.verifyOtp(otpVerify);
			String storedOtp = otp.get("otp").toString();
			String inputOtp = otpVerify.get("otp").toString();

			if (otp.get("validity").toString().equalsIgnoreCase("1")) {
				if (storedOtp.equals(inputOtp)) {
					m.put("message", "OTP Verification Successful");
					m.put("flag", true);
					m.put("status", 1);
				} else {
					m.put("message", "Incorrect OTP");
					m.put("flag", true);
					m.put("status", 2);
				}
			} else {
				if (storedOtp.equals(inputOtp)) {
					m.put("message", "OTP Expired");
					m.put("flag", true);
					m.put("status", 3);
				} else {
					m.put("message", "Incorrect OTP");
					m.put("flag", true);
					m.put("status", 4);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			m.put("message", "Error, during otp verification");
			m.put("flag", false);
			m.put("status", 0);
		}
		return m;
	}

	public synchronized Map<String, Object> registerDoctor(DoctorProfile doctorProfile) {
		Map<String, Object> m = new LinkedHashMap<>();
		try {
			m.put("status", spareRepository.registerDoctor(doctorProfile));
			m.put("flag", true);
			m.put("message", "doctor registration Successful");
			logger.debug(doctorProfile.getMobile() + " registration successful");
		} catch (Exception e) {
			e.printStackTrace();
			m.put("flag", false);
			m.put("message", "Error, while registering doctor");
			m.put("status", 0);
			logger.debug(doctorProfile.getMobile() + " registration failed");
		}
		return m;
	}
}
