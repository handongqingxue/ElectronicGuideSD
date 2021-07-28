package com.electronicGuideSD.entity;

public class ScenicPlace {

	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Float getX() {
		return x;
	}
	public void setX(Float x) {
		this.x = x;
	}
	public Float getY() {
		return y;
	}
	public void setY(Float y) {
		this.y = y;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public Integer getPicWidth() {
		return picWidth;
	}
	public void setPicWidth(Integer picWidth) {
		this.picWidth = picWidth;
	}
	public Integer getPicHeight() {
		return picHeight;
	}
	public void setPicHeight(Integer picHeight) {
		this.picHeight = picHeight;
	}
	public String getSimpleIntro() {
		return simpleIntro;
	}
	public void setSimpleIntro(String simpleIntro) {
		this.simpleIntro = simpleIntro;
	}
	public String getDetailIntro() {
		return detailIntro;
	}
	public void setDetailIntro(String detailIntro) {
		this.detailIntro = detailIntro;
	}
	public String getSimpleIntroVoiceUrl() {
		return simpleIntroVoiceUrl;
	}
	public void setSimpleIntroVoiceUrl(String simpleIntroVoiceUrl) {
		this.simpleIntroVoiceUrl = simpleIntroVoiceUrl;
	}
	public String getDetailIntroVoiceUrl() {
		return detailIntroVoiceUrl;
	}
	public void setDetailIntroVoiceUrl(String detailIntroVoiceUrl) {
		this.detailIntroVoiceUrl = detailIntroVoiceUrl;
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
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	private String name;
	private Float x;
	private Float y;
	private String picUrl;
	private Integer picWidth;
	private Integer picHeight;
	private String simpleIntro;
	private String detailIntro;
	private String simpleIntroVoiceUrl;
	private String detailIntroVoiceUrl;
	private String createTime;
	private String modifyTime;
	private Integer sort;
}
