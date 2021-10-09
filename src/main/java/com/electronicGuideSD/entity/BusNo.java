package com.electronicGuideSD.entity;

public class BusNo {

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
	public String getStartBsName() {
		return startBsName;
	}
	public void setStartBsName(String startBsName) {
		this.startBsName = startBsName;
	}
	public String getEndBsName() {
		return endBsName;
	}
	public void setEndBsName(String endBsName) {
		this.endBsName = endBsName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
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
	private String startBsName;
	private String endBsName;
	private String startTime;
	private String endTime;
	private String createTime;
	private String modifyTime;
	private Integer sort;
}
