package com.electronicGuideSD.entity;

public class RoadStage {

	public static final String BACK_FLAG="back";
	public static final String FRONT_FLAG="front";
	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Float getFrontX() {
		return frontX;
	}
	public void setFrontX(Float frontX) {
		this.frontX = frontX;
	}
	public Float getFrontY() {
		return frontY;
	}
	public void setFrontY(Float frontY) {
		this.frontY = frontY;
	}
	public Float getBackX() {
		return backX;
	}
	public void setBackX(Float backX) {
		this.backX = backX;
	}
	public Float getBackY() {
		return backY;
	}
	public void setBackY(Float backY) {
		this.backY = backY;
	}
	public Boolean getFrontThrough() {
		return frontThrough;
	}
	public void setFrontThrough(Boolean frontThrough) {
		this.frontThrough = frontThrough;
	}
	public Boolean getBackThrough() {
		return backThrough;
	}
	public void setBackThrough(Boolean backThrough) {
		this.backThrough = backThrough;
	}
	public Boolean getFrontIsCross() {
		return frontIsCross;
	}
	public void setFrontIsCross(Boolean frontIsCross) {
		this.frontIsCross = frontIsCross;
	}
	public String getFrontCrossRSIds() {
		return frontCrossRSIds;
	}
	public void setFrontCrossRSIds(String frontCrossRSIds) {
		this.frontCrossRSIds = frontCrossRSIds;
	}
	public Boolean getBackIsCross() {
		return backIsCross;
	}
	public void setBackIsCross(Boolean backIsCross) {
		this.backIsCross = backIsCross;
	}
	public String getBackCrossRSIds() {
		return backCrossRSIds;
	}
	public void setBackCrossRSIds(String backCrossRSIds) {
		this.backCrossRSIds = backCrossRSIds;
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
	public String getBfFlag() {
		return bfFlag;
	}
	public void setBfFlag(String bfFlag) {
		this.bfFlag = bfFlag;
	}
	private Float frontX;
	private Float frontY;
	private Float backX;
	private Float backY;
	private Boolean frontThrough;//前方是否通
	private Boolean backThrough;//后方是否通
	private Boolean frontIsCross;//前方是否是交叉点
	private String frontCrossRSIds;//前方交叉点id，可能包含多个交叉点
	private Boolean backIsCross;//后方是否是交叉点
	private String backCrossRSIds;
	private String createTime;
	private String modifyTime;
	private Integer sort;
	private Integer roadId;
	private String bfFlag;
}