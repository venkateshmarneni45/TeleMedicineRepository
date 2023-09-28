package com.example.TeleMedicine.PatientAppointments;

public class Timings {
	private String date;
	private String fromTime;
	private String toTime;
	private int sessionTime;
	private String dayType;

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	public int getSessionTime() {
		return sessionTime;
	}
	public void setSessionTime(int sessionTime) {
		this.sessionTime = sessionTime;
	}
	public String getDayType() {
		return dayType;
	}
	public void setDayType(String dayType) {
		this.dayType = dayType;
	}
	@Override
	public String toString() {
		return "Timings [date=" + date + ", fromTime=" + fromTime + ", toTime=" + toTime + ", sessionTime="
				+ sessionTime + ", dayType=" + dayType + "]";
	}
}
