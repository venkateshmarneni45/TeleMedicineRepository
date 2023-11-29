package com.example.TeleMedicine.UserLogin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserLoginRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Map<String, Object> login(LoginPojo loginPojo) {
		String query = "select count(*) from hclhrm_prod.tbl_employee_login where employeecode="
				+ loginPojo.getUsername() + " and password=md5('" + loginPojo.getPassword() + "') and status=1001";
		int count = jdbcTemplate.queryForObject(query, Integer.class);
		Map<String, Object> map = new HashMap<>();
		if (count == 1) {
			StringBuffer profileQuery = new StringBuffer();
			profileQuery.append(
					" select a.employeesequenceno EmployeeCode,a.callname EmpName,c.name Designation,d.name Department ");
			profileQuery.append(" from hclhrm_prod.tbl_employee_primary a ");
			profileQuery
					.append(" left join hclhrm_prod.tbl_employee_professional_details b on a.employeeid=b.employeeid ");
			profileQuery.append(" left join hcladm_prod.tbl_designation c on b.designationid=c.designationid ");
			profileQuery.append(" left join hcladm_prod.tbl_department d on b.departmentid=d.departmentid ");
			profileQuery.append(" where a.employeesequenceno=? ");
			Map<String, Object> profileMap = jdbcTemplate.queryForMap(profileQuery.toString(),
					new Object[] { loginPojo.getUsername() });
			map.put("profiledata", profileMap);
			map.put("flag", true);
		} else {
			map.put("flag", false);
		}
		return map;
	}
}
