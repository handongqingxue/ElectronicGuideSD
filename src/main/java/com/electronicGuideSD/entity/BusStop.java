package com.electronicGuideSD.entity;

public class BusStop {

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
	public String getBusNoIds() {
		return busNoIds;
	}
	public void setBusNoIds(String busNoIds) {
		this.busNoIds = busNoIds;
	}
	public String getBusNoNames() {
		return busNoNames;
	}
	public void setBusNoNames(String busNoNames) {
		this.busNoNames = busNoNames;
	}
	private String name;
	private Float x;
	private Float y;
	private String createTime;
	private String modifyTime;
	private Integer sort;
	private String busNoIds;
	private String busNoNames;
}
