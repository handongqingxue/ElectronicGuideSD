package com.electronicGuideSD.entity;

public class BusNosStop {

	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBusStopId() {
		return busStopId;
	}
	public void setBusStopId(Integer busStopId) {
		this.busStopId = busStopId;
	}
	public String getBsName() {
		return bsName;
	}
	public void setBsName(String bsName) {
		this.bsName = bsName;
	}
	public Integer getBusNoId() {
		return busNoId;
	}
	public void setBusNoId(Integer busNoId) {
		this.busNoId = busNoId;
	}
	public String getBnName() {
		return bnName;
	}
	public void setBnName(String bnName) {
		this.bnName = bnName;
	}
	public String getBusNoIds() {
		return busNoIds;
	}
	public void setBusNoIds(String busNoIds) {
		this.busNoIds = busNoIds;
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
	public String getPreBsName() {
		return preBsName;
	}
	public void setPreBsName(String preBsName) {
		this.preBsName = preBsName;
	}
	public Integer getNextBsId() {
		return nextBsId;
	}
	public void setNextBsId(Integer nextBsId) {
		this.nextBsId = nextBsId;
	}
	public String getNextBsName() {
		return nextBsName;
	}
	public void setNextBsName(String nextBsName) {
		this.nextBsName = nextBsName;
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
	private Integer busStopId;
	private String bsName;
	private Integer busNoId;
	private String bnName;
	private String busNoIds;
	private Boolean isStart;
	private Boolean isEnd;
	private Integer preBsId;
	private String preBsName;
	private Integer nextBsId;
	private String nextBsName;
	private String createTime;
	private String modifyTime;
	private Integer sort;
}
