package com.electronicGuideSD.entity;

public class BusNosStop {

	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBsId() {
		return bsId;
	}
	public void setBsId(Integer bsId) {
		this.bsId = bsId;
	}
	public Integer getBusNoId() {
		return busNoId;
	}
	public void setBusNoId(Integer busNoId) {
		this.busNoId = busNoId;
	}
	public Boolean getIsStart() {
		return isStart;
	}
	public void setIsStart(Boolean isStart) {
		this.isStart = isStart;
	}
	public Boolean getIsEnd() {
		return isEnd;
	}
	public void setIsEnd(Boolean isEnd) {
		this.isEnd = isEnd;
	}
	public Integer getPreBsId() {
		return preBsId;
	}
	public void setPreBsId(Integer preBsId) {
		this.preBsId = preBsId;
	}
	public Integer getNextBsId() {
		return nextBsId;
	}
	public void setNextBsId(Integer nextBsId) {
		this.nextBsId = nextBsId;
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
	private Integer bsId;
	private Integer busNoId;
	private Boolean isStart;
	private Boolean isEnd;
	private Integer preBsId;
	private Integer nextBsId;
	private String createTime;
	private String modifyTime;
	private Integer sort;
}
