package com.example.TeleMedicine.PatientProfile;

public class Patient_Updation {
	private int id;
	private String name;
	private int age;
	private String height;
	private String weight;
	private int bloodGroupId;
	
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
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
	public int getBloodGroupId() {
		return bloodGroupId;
	}
	public void setBloodGroupId(int bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}
	@Override
	public String toString() {
		return "Patient_Updation [id=" + id + ", name=" + name + ", age=" + age + ", height=" + height + ", weight="
				+ weight + ", bloodGroupId=" + bloodGroupId + "]";
	}
}
