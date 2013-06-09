package com.rincliu.library.entity;

import java.io.Serializable;

public class RLLocationInfo implements Serializable{
	private static final long serialVersionUID = -1459176470637220623L;
	
	private double altitude;
	private double latitude;
	private double longitude;
	private float radius;
	private float speed;
	private String province;
	private String district;
	private String city;
	private String street;
	private String address;
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public double getAltitude() {
		return altitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public float getRadius() {
		return radius;
	}
	public void setRadius(float radius) {
		this.radius = radius;
	}
	public float getSpeed() {
		return speed;
	}
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}