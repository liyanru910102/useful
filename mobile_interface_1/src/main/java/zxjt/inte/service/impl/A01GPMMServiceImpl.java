package zxjt.inte.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import zxjt.inte.dao.A01GPMMDao;
import zxjt.inte.dao.A01_1KMMSLCXDao;
import zxjt.inte.dao.A01_2SJKMMSLCXDao;
import zxjt.inte.dao.CommonInfoDao;
import zxjt.inte.entity.A01GPMM;
import zxjt.inte.entity.A01_1KMMSLCX;
import zxjt.inte.entity.A01_2SJKMMSLCX;
import zxjt.inte.entity.CommonInfo;
import zxjt.inte.service.A01GPMMService;
import zxjt.inte.util.CommonToolsUtil;
import zxjt.inte.util.ParamConstant;

@Service
public class A01GPMMServiceImpl implements A01GPMMService {

	@Resource
	private A01GPMMDao gpmmDao;

	@Resource
	private A01_1KMMSLCXDao kmmxxDao;

	@Resource
	private A01_2SJKMMSLCXDao sjkmmxxDao;

	@Resource
	private CommonInfoDao commonDao;

	public Object[][] getParamsInfo(int id) {

		// 公共参数操作
		List<CommonInfo> lisag = commonDao.getCommonsInfo();
		Map<String, String> commonParam = CommonToolsUtil.getCommonParam(lisag);

		// 股票买卖数据操作
		List<A01GPMM> lis = gpmmDao.getParamsInfo(id);
		List<Map<String, String>> lisTemp = CommonToolsUtil.getDependencyParamsInfo(lis, commonParam);

		// 限价可买卖信息查询数据操作
		List<A01_1KMMSLCX> listA01 = kmmxxDao.getParamsInfo(ParamConstant.KMMXXCX_ID);
		Map<String, String> MapDepenParam = CommonToolsUtil.getDependencyParamInfo(listA01, commonParam);

		// 市价可买卖信息查询数据操作
		List<A01_2SJKMMSLCX> listA02 = sjkmmxxDao.getParamsInfo(ParamConstant.SJKMMXXCX_ID);
		Map<String, String> MapDepenSjParam = CommonToolsUtil.getDependencyParamInfo(listA02, commonParam);

		Object[][] obj = new Object[lisTemp.size()][2];
		for (int j = 0; j < obj.length; j++) {

			obj[j][0] = lisTemp.get(j);
			Map<String, String> mps = new HashMap<String, String>();
			if ("0".equals(lisTemp.get(j).get("wtlx"))) {
				
				mps.putAll(MapDepenParam);
				
			} else {
				mps.putAll(MapDepenSjParam);
			}
			obj[j][1] = mps;
		}
		return obj;
	}

}
