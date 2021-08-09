package com.electronicGuideSD.entity;

public class User {

	public User(String userName,String password) {
		this.userName=userName;
		this.password=password;
	}
	
	public User() {
		super();
	}
	
	private Integer id;//用户id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	public String getSceDisName() {
		return sceDisName;
	}
	public void setSceDisName(String sceDisName) {
		this.sceDisName = sceDisName;
	}
	public String getSceDisAddress() {
		return sceDisAddress;
	}
	public void setSceDisAddress(String sceDisAddress) {
		this.sceDisAddress = sceDisAddress;
	}
	public ScenicDistrict getScenicDistrict() {
		return scenicDistrict;
	}
	public void setScenicDistrict(ScenicDistrict scenicDistrict) {
		this.scenicDistrict = scenicDistrict;
	}
	public Integer getCheck() {
		return check;
	}
	public void setCheck(Integer check) {
		this.check = check;
	}
	private String userName;//用户账号
	private String password;//密码
	private String nickName;
	private String headImgUrl;
	private String createTime;
	private Integer role;
	private String sceDisName;
	private String sceDisAddress;
	private ScenicDistrict scenicDistrict;
	private Integer check;
}
