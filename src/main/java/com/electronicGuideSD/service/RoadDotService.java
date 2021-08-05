package com.electronicGuideSD.service;

import java.util.List;

import com.electronicGuideSD.entity.*;

public interface RoadDotService {

	List<RoadDot> getShortRoadLine(Float meX, Float meY, Float scenicPlaceX, Float scenicPlaceY);

}
