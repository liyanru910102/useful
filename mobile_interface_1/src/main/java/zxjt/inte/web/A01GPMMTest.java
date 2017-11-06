package zxjt.inte.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import zxjt.inte.controller.A01GPMMController;
import zxjt.inte.controller.BaseController;
import zxjt.inte.service.A01GPMMService;
import zxjt.inte.util.BeforeClassUse;
import zxjt.inte.util.CommonToolsUtil;
import zxjt.inte.util.HttpUtil_All;
import zxjt.inte.util.JsonAssertUtil;
import zxjt.inte.util.ParamConstant;

//自己的类名
public class A01GPMMTest extends BaseController {
	Logger log = Logger.getLogger(ParamConstant.LOGGER); 
	// *自己的Service名称
	@Resource
	private A01GPMMService gpmmService;

	@BeforeClass
	public void setup() {
		BeforeClassUse.setDPInfo(gpmmService);
	}

	// *自己的controller和别名
	@Test(dataProvider = "gpmm", dataProviderClass = A01GPMMController.class)

	// *自己的方法名
	public void A01GPMM(Map<String, String> param, Map<String, String> DependenceParam) {

		Map<String, String> map = CommonToolsUtil.getRParam(param);

		if ("0".equals(map.get("wtlx"))) {
			DependenceParam.put("zqdm", map.get("zqdm"));

		} else {
			DependenceParam.put("zqdm", map.get("zqdm"));
			DependenceParam.put("wtlx", map.get("wtlx"));
		}
		System.out.println(DependenceParam.toString());
		log.info(DependenceParam.toString());
		String cxResponse = CommonToolsUtil.getResponseInfo(DependenceParam);
		System.out.println(cxResponse.toString());
		log.info(cxResponse.toString());
		if ("0".equals(map.get("wtlx"))) {

			JsonAssertUtil.checkDependenceResponse(cxResponse, null, ParamConstant.PTYW, ParamConstant.A01_1_SCHEMA_ZL);
		} else {
			JsonAssertUtil.checkDependenceResponse(cxResponse, null, ParamConstant.PTYW, ParamConstant.A01_2_SCHEMA_ZL);
		}

		String price = CommonToolsUtil.getPrice(map.get("wtjg"), cxResponse);
		String wtsl = CommonToolsUtil.getOverBSQty(map.get("mmlb"), map.get("wtsl"), cxResponse);
		String jysdm = JsonAssertUtil.getValue(cxResponse, "$.kmmxx[0].jysdm");
		String gddm = JsonAssertUtil.getValue(cxResponse, "$.kmmxx[0].gddm");
		map.put("wtjg", price);
		map.put("wtsl", wtsl);
		map.put("jysdm", jysdm);
		map.put("gddm", gddm);

		// 发请求
		System.out.println(param.toString());
		System.out.println(map.toString());
		log.info(param.toString());
		log.info(map.toString());
		String response = HttpUtil_All.doPostSSL(param.get("url"), map);
		System.out.println(response.toString());
		log.info(response.toString());

		JsonAssertUtil.checkNull(response);
		Map<String, String> valMap = new HashMap<>();
		valMap.put("message", JsonAssertUtil.getMsgRex(param.get("expectMsg")));
		String strjsonSchema = "";
		try {
			if (ParamConstant.ZL.equalsIgnoreCase(param.get("type"))) {
				Map<String, String> regexMap = JsonAssertUtil.getRegex(valMap, ParamConstant.PTYW,
						ParamConstant.A01_SCHEMA_ZL);
				strjsonSchema = JsonAssertUtil.editSchemaInfo(ParamConstant.A01_SCHEMA_ZL, regexMap);
			} else if (ParamConstant.FL.equalsIgnoreCase(param.get("type"))) {
				Map<String, String> regexMap = JsonAssertUtil.getRegex(valMap, ParamConstant.PTYW,
						ParamConstant.A01_SCHEMA_FL);
				strjsonSchema = JsonAssertUtil.editSchemaInfo(ParamConstant.A01_SCHEMA_FL, regexMap);
			} else {
				throw new RuntimeException("测试数据类型缺失，请查证后再执行！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		// 验证 *自己的schema名称
		JsonAssertUtil.JsonAssert(response, strjsonSchema);
	}
}
