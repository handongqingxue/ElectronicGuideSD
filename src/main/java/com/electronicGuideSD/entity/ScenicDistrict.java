package com.electronicGuideSD.entity;

public class ScenicDistrict {
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getQrcodeUrl() {
		return qrcodeUrl;
	}
	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
	}
	public String getMapUrl() {
		return mapUrl;
	}
	public void setMapUrl(String mapUrl) {
		this.mapUrl = mapUrl;
	}
	public Float getMapWidth() {
		return mapWidth;
	}
	public void setMapWidth(Float mapWidth) {
		this.mapWidth = mapWidth;
	}
	public Float getMapHeight() {
		return mapHeight;
	}
	public void setMapHeight(Float mapHeight) {
		this.mapHeight = mapHeight;
	}
	public Float getPicWidth() {
		return picWidth;
	}
	public void setPicWidth(Float picWidth) {
		this.picWidth = picWidth;
	}
	public Float getPicHeight() {
		return picHeight;
	}
	public void setPicHeight(Float picHeight) {
		this.picHeight = picHeight;
	}
	public Float getLongitudeStart() {
		return longitudeStart;
	}
	public void setLongitudeStart(Float longitudeStart) {
		this.longitudeStart = longitudeStart;
	}
	public Float getLongitudeEnd() {
		return longitudeEnd;
	}
	public void setLongitudeEnd(Float longitudeEnd) {
		this.longitudeEnd = longitudeEnd;
	}
	public Float getLatitudeStart() {
		return latitudeStart;
	}
	public void setLatitudeStart(Float latitudeStart) {
		this.latitudeStart = latitudeStart;
	}
	public Float getLatitudeEnd() {
		return latitudeEnd;
	}
	public void setLatitudeEnd(Float latitudeEnd) {
		this.latitudeEnd = latitudeEnd;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getServerPort() {
		return serverPort;
	}
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	private String name;
	private String address;
	private String qrcodeUrl;
	private String mapUrl;
	private Float mapWidth;
	private Float mapHeight;
	private Float picWidth;
	private Float picHeight;
	private Float longitudeStart;//经度开始
	private Float longitudeEnd;//经度结束
	private Float latitudeStart;//纬度开始
	private Float latitudeEnd;//纬度结束
	private String longitude;
	private String latitude;
	private String introduce;
	private String serverName;
	private String serverPort;
	private String createTime;
	private String modifyTime;
}
