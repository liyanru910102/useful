package zxjt.inte.controller;

import org.testng.annotations.DataProvider;

import zxjt.inte.service.A01GPMMService;
import zxjt.inte.util.BeforeClassUse;
import zxjt.inte.util.ParamConstant;

public class A01GPMMController {

	// *自己的别名
	@DataProvider(name = "gpmm")
	//*自己的方法名
	public static Object[][] gpmmTest() {

		A01GPMMService gpmmService = (A01GPMMService) BeforeClassUse.getDPInfo();
		
		//*自己的方法
		Object[][] obj = gpmmService.getParamsInfo(ParamConstant.GPMM_ID);
		
		return obj;
	}
}
