package com.example.TeleMedicine.DoctorProfile;

public class Doctor_Details {
	private int id;
	private String name;
	private String mobile;
	private String gender;
	private String email;
	private String dept;
	private String about;
	private String license;
	private String specializations;
	private String img;
	
	public int getId() {
		return id;
	}
	public Doctor_Details setId(int id) {
		this.id = id;
		return this;
	}
	public String getName() {
		return name;
	}
	public Doctor_Details setName(String name) {
		this.name = name;
		return this;
	}
	public String getMobile() {
		return mobile;
	}
	public Doctor_Details setMobile(String mobile) {
		this.mobile = mobile;
		return this;
	}
	public String getGender() {
		return gender;
	}
	public Doctor_Details setGender(String gender) {
		this.gender = gender;
		return this;
	}
	public String getEmail() {
		return email;
	}
	public Doctor_Details setEmail(String email) {
		this.email = email;
		return this;
	}
	public String getDept() {
		return dept;
	}
	public Doctor_Details setDept(String dept) {
		this.dept = dept;
		return this;
	}
	public String getAbout() {
		return about;
	}
	public Doctor_Details setAbout(String about) {
		this.about = about;
		return this;
	}
	
	public String getLicense() {
		return license;
	}
	public Doctor_Details setLicense(String license) {
		this.license = license;
		return this;
	}
	public String getSpecializations() {
		return specializations;
	}
	public Doctor_Details setSpecializations(String specializations) {
		this.specializations = specializations;
		return this;
	}
	public String getImg() {
		return img;
	}
	public Doctor_Details setImg(String img) {
		this.img = img;
		return this;
	}
	@Override
	public String toString() {
		return "Doctor_Details [id=" + id + ", name=" + name + ", mobile=" + mobile + ", gender=" + gender + ", email="
				+ email + ", dept=" + dept + ", about=" + about + ", license=" + license + ", specializations="
				+ specializations + ", img="+img +"]";
	}
}
