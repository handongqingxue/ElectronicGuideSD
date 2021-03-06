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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Float getCrossX() {
		return crossX;
	}
	public void setCrossX(Float crossX) {
		this.crossX = crossX;
	}
	public Float getCrossY() {
		return crossY;
	}
	public void setCrossY(Float crossY) {
		this.crossY = crossY;
	}
	public Float getDistance() {
		return distance;
	}
	public void setDistance(Float distance) {
		this.distance = distance;
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
	public String getFrontCrossRSNames() {
		return frontCrossRSNames;
	}
	public void setFrontCrossRSNames(String frontCrossRSNames) {
		this.frontCrossRSNames = frontCrossRSNames;
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
	public String getBackCrossRSNames() {
		return backCrossRSNames;
	}
	public void setBackCrossRSNames(String backCrossRSNames) {
		this.backCrossRSNames = backCrossRSNames;
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
	public String getRoadName() {
		return roadName;
	}
	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}
	public Integer getOldRoadId() {
		return oldRoadId;
	}
	public void setOldRoadId(Integer oldRoadId) {
		this.oldRoadId = oldRoadId;
	}
	public String getPwcBfFlag() {
		return pwcBfFlag;
	}
	public void setPwcBfFlag(String pwcBfFlag) {
		this.pwcBfFlag = pwcBfFlag;
	}
	public String getCwpBfFlag() {
		return cwpBfFlag;
	}
	public void setCwpBfFlag(String cwpBfFlag) {
		this.cwpBfFlag = cwpBfFlag;
	}
	private String name;
	private Float frontX;
	private Float frontY;
	private Float backX;
	private Float backY;
	private Float crossX;//????????????????x????
	private Float crossY;//????????????????y????
	private Float distance;
	private Boolean frontThrough;//??????????
	private Boolean backThrough;//??????????
	private Boolean frontIsCross;//????????????????
	private String frontCrossRSIds;//??????????id????????????????????
	private String frontCrossRSNames;
	private Boolean backIsCross;//????????????????
	private String backCrossRSIds;
	private String backCrossRSNames;
	private String createTime;
	private String modifyTime;
	private Integer sort;
	private Integer roadId;
	private String roadName;
	private Integer oldRoadId;
	private String pwcBfFlag;//????????????????????????????(??????????????????)
	private String cwpBfFlag;//????????????????????????????(??????????????????????)
}