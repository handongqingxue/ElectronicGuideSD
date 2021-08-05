package com.electronicGuideSD.entity;

public class Road {

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
	private Boolean frontThrough;
	private Boolean backThrough;
	private String createTime;
	private String modifyTime;
	private Integer sort;
}
