package com.example.TeleMedicine.CommonServices;

import java.sql.Time;
import java.util.Arrays;

import org.springframework.web.multipart.MultipartFile;

public class DoctorProfile {
	private int id;
	private String name;
	private MultipartFile img;
	private String mobile;
	private String email;
	private int genderId;
	private int experience;
	private String license;
	private int cost;
	private String state;
	private String city;
	private int deptId;
	private int[] specialization;
	private Time fromTime;
	private Time toTime;
	private int sessionTime;	
	private String about;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public MultipartFile getImg() {
		return img;
	}
	public void setImg(MultipartFile img) {
		this.img = img;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getGenderId() {
		return genderId;
	}
	public void setGenderId(int genderId) {
		this.genderId = genderId;
	}
	public int getExperience() {
		return experience;
	}
	public void setExperience(int experience) {
		this.experience = experience;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public int[] getSpecialization() {
		return specialization;
	}
	public void setSpecialization(int[] specialization) {
		this.specialization = specialization;
	}
	public Time getFromTime() {
		return fromTime;
	}
	public void setFromTime(Time fromTime) {
		this.fromTime = fromTime;
	}
	public Time getToTime() {
		return toTime;
	}
	public void setToTime(Time toTime) {
		this.toTime = toTime;
	}
	public int getSessionTime() {
		return sessionTime;
	}
	public void setSessionTime(int sessionTime) {
		this.sessionTime = sessionTime;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	@Override
	public String toString() {
		return "DoctorProfile [id=" + id + ", name=" + name + ", img=" + img + ", mobile=" + mobile + ", email=" + email
				+ ", genderId=" + genderId + ", experience=" + experience + ", license=" + license + ", cost=" + cost
				+ ", state=" + state + ", city=" + city + ", deptId=" + deptId + ", specialization="
				+ Arrays.toString(specialization) + ", fromTime=" + fromTime + ", toTime=" + toTime + ", sessionTime="
				+ sessionTime + ", about=" + about + "]";
	}
}
