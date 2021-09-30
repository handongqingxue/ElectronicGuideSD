package com.electronicGuideSD.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronicGuideSD.dao.*;
import com.electronicGuideSD.entity.*;
import com.electronicGuideSD.service.*;

@Service
public class BusNosStopServiceImpl implements BusNosStopService {

	@Autowired
	private BusNosStopMapper busNosStopDao;

	@Override
	public List<BusNosStop> selectList(String name, int busNoId) {
		// TODO Auto-generated method stub
		return busNosStopDao.selectList(name, busNoId);
	}

	@Override
	public int add(BusNosStop busNosStop) {
		// TODO Auto-generated method stub
		int count=busNosStopDao.add(busNosStop);
		int busNoId = busNosStop.getBusNoId();
		List<BusNosStop> newBnsList=new ArrayList<BusNosStop>();
		List<BusNosStop> bnsList=busNosStopDao.selectByBusNoId(busNoId);
		BusNosStop bns=bnsList.get(0);
		newBnsList.add(bns);
		bnsList.remove(bns);
		for (int i = 0; i < bnsList.size(); i++) {
			bns=bnsList.get(i);
			BusNosStop firstBns = newBnsList.get(0);
			if(firstBns.getPreBsId()==bns.getBusStopId()) {
				newBnsList.add(0, bns);
				bnsList.remove(bns);
			}
			BusNosStop lastBns = newBnsList.get(newBnsList.size()-1);
			if(lastBns.getNextBsId()==bns.getBusStopId()) {
				newBnsList.add(bns);
				bnsList.remove(bns);
			}
		}
		for (int i = 0; i < newBnsList.size(); i++) {
			BusNosStop newBns = newBnsList.get(i);
			newBns.setSort(i);
			if(i==0) {
				newBns.setIsStart(true);
				newBns.setIsEnd(false);
			}
			else if(i==newBnsList.size()-1) {
				newBns.setIsStart(false);
				newBns.setIsEnd(true);
			}
			else {
				newBns.setIsStart(false);
				newBns.setIsEnd(false);
			}
			count=busNosStopDao.edit(newBns);
		}
		return count;
	}
}
