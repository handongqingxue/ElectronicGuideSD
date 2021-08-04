package com.electronicGuideSD.entity;

public class RoadDot {

	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Boolean getIsCrossDot() {
		return isCrossDot;
	}
	public void setIsCrossDot(Boolean isCrossDot) {
		this.isCrossDot = isCrossDot;
	}
	public Integer getCrossRoadId() {
		return crossRoadId;
	}
	public void setCrossRoadId(Integer crossRoadId) {
		this.crossRoadId = crossRoadId;
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
	public Integer getRoadId() {
		return roadId;
	}
	public void setRoadId(Integer roadId) {
		this.roadId = roadId;
	}
	private Float x;
	private Float y;
	private Boolean isCrossDot;
	private Integer crossRoadId;
	private String createTime;
	private String modifyTime;
	private Integer sort;
	private Integer roadId;
}