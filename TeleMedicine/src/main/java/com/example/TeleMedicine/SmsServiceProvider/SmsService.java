package com.example.TeleMedicine.SmsServiceProvider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SmsService {
	private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

	public synchronized Map<String, Object> send(String otp, String mobile, String service) {
		Map<String, Object> msg = new HashMap<>();
		try {
			String data = "";
			data += "APIKey=" + URLEncoder.encode("xcX1hfrrMkCD3Eu7bq6F5w", "UTF-8");
			data += "&senderid=" + URLEncoder.encode("HETERO", "UTF-8");
			data += "&channel=" + URLEncoder.encode("OTP", "UTF-8");
			data += "&DCS=" + URLEncoder.encode("0", "UTF-8");
			data += "&route=" + URLEncoder.encode("1", "UTF-8");
			data += "&flashsms=" + URLEncoder.encode("0", "UTF-8");
			data += "&number=" + URLEncoder.encode("" + mobile + "", "UTF-8");

			if (service.equalsIgnoreCase("PatientRegister")) {
				data += "&text=" + URLEncoder.encode("" + otp
						+ " is OTP for password change in your iConnect. OTP is valid for 15 mins. Please do not share with anyone. Hetero",
						"UTF-8");
			} else if (service.equalsIgnoreCase("PatientLogin")) {
				data += "&text=" + URLEncoder.encode("" + otp
						+ " is OTP for password change in your iConnect. OTP is valid for 15 mins. Please do not share with anyone. Hetero",
						"UTF-8");
			} else if (service.equalsIgnoreCase("DoctorRegister")) {
				data += "&text=" + URLEncoder.encode("" + otp
						+ " is OTP for password change in your iConnect. OTP is valid for 15 mins. Please do not share with anyone. Hetero",
						"UTF-8");
			} else if (service.equalsIgnoreCase("DoctorLogin")) {
				data += "&text=" + URLEncoder.encode("" + otp
						+ " is OTP for password change in your iConnect. OTP is valid for 15 mins. Please do not share with anyone. Hetero",
						"UTF-8");
			}

			URL url = new URL("http://login.smsgatewayhub.com/api/mt/SendSMS?" + data);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Accept", "application/json");
			conn.setUseCaches(false);
			conn.connect();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			StringBuffer buffer = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				buffer.append(line).append("\n");
			}
			logger.debug(buffer.toString());
			rd.close();
			conn.disconnect();
			JSONObject jsonObject = new JSONObject(buffer.toString());
			String error = jsonObject.get("ErrorCode").toString();
			String message = jsonObject.get("ErrorMessage").toString();
			msg.put("error", error);
			msg.put("message", message);
			msg.put("status", 1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
}
