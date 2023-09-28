package com.example.TeleMedicine.PatientRegistration;

import java.util.Arrays;

public class PatientRegister {
	private String name;
	private String mobile;
	private int age;
	private int genderId;
	private String height;
	private String weight;
	private String state;
	private String city;
	private String area;
	private int bloodGroupId;
	private int[] medicalHistory;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getGenderId() {
		return genderId;
	}
	public void setGenderId(int genderId) {
		this.genderId = genderId;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
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
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public int getBloodGroupId() {
		return bloodGroupId;
	}
	public void setBloodGroupId(int bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}
	public int[] getMedicalHistory() {
		return medicalHistory;
	}
	public void setMedicalHistory(int[] medicalHistory) {
		this.medicalHistory = medicalHistory;
	}
	@Override
	public String toString() {
		return "PatientRegister [name=" + name + ", mobile=" + mobile + ", age=" + age + ", genderId=" + genderId
				+ ", height=" + height + ", weight=" + weight + ", state=" + state + ", city=" + city + ", area=" + area
				+ ", bloodGroupId=" + bloodGroupId + ", medicalHistory=" + Arrays.toString(medicalHistory) + "]";
	}
}
