package com.electronicGuideSD.entity;

public class ToSpRoute {

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
	public String getRoadIds() {
		return roadIds;
	}
	public void setRoadIds(String roadIds) {
		this.roadIds = roadIds;
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
	public Integer getScePlaId() {
		return scePlaId;
	}
	public void setScePlaId(Integer scePlaId) {
		this.scePlaId = scePlaId;
	}
	public String getSpName() {
		return spName;
	}
	public void setSpName(String spName) {
		this.spName = spName;
	}
	private String name;
	private String roadIds;
	private String createTime;
	private String modifyTime;
	private Integer sort;
	private Integer scePlaId;
	private String spName;
}
