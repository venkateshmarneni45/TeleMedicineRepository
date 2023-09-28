package com.example.TeleMedicine.UserLogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserLoginRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int login(LoginPojo loginPojo) {
		String loginQuery = "select count(*) from telemedicine_master.tbl_user_login where username=? and password=?";
		return jdbcTemplate.queryForObject(loginQuery, Integer.class,
				new Object[] { loginPojo.getUsername(), loginPojo.getPassword() });
	}
}
