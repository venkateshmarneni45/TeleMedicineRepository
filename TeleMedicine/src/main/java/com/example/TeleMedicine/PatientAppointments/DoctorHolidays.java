package com.example.TeleMedicine.PatientAppointments;

public class DoctorHolidays {
	private String holidayDate;
	private String comment;
	
	public String getHolidayDate() {
		return holidayDate;
	}
	public DoctorHolidays setHolidayDate(String holidayDate) {
		this.holidayDate = holidayDate;
		return this;
	}
	public String getComment() {
		return comment;
	}
	public DoctorHolidays setComment(String comment) {
		this.comment = comment;
		return this;
	}
	@Override
	public String toString() {
		return "DoctorHolidays [date=" + holidayDate + ", comment=" + comment + "]";
	}

}
