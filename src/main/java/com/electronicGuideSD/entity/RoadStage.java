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
	private Float crossX;//除两端之外的交点x坐标
	private Float crossY;//除两端之外的交点y坐标
	private Float distance;
	private Boolean frontThrough;//前方是否通
	private Boolean backThrough;//后方是否通
	private Boolean frontIsCross;//前方是否是交叉点
	private String frontCrossRSIds;//前方交叉点id，可能包含多个交叉点
	private String frontCrossRSNames;
	private Boolean backIsCross;//后方是否是交叉点
	private String backCrossRSIds;
	private String backCrossRSNames;
	private String createTime;
	private String modifyTime;
	private Integer sort;
	private Integer roadId;
	private String roadName;
	private Integer oldRoadId;
	private String pwcBfFlag;//上一个路段与该路段的交点位置(该路段的后方或前方)
	private String cwpBfFlag;//该路段与上一个路段的交点位置(上一个路段的后方或前方)
}