package com.example.TeleMedicine.AdminDoctors;

public class TimeSchedule {
	private String id;
	private String from;
	private String to;
	private int sessionTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public int getSessionTime() {
		return sessionTime;
	}
	public void setSessionTime(int sessionTime) {
		this.sessionTime = sessionTime;
	}
	@Override
	public String toString() {
		return "TimeSchedule [id=" + id + ", from=" + from + ", to=" + to + "]";
	}
}
