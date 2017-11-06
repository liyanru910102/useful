package zxjt.inte.controller;

import org.testng.annotations.DataProvider;

import zxjt.inte.service.A00LoginService;
import zxjt.inte.util.BeforeClassUse;

public class A00LoginController {

	// *自己的别名
	@DataProvider(name = "test1")
	//*自己的方法名
	public static Object[][] loginTest() {

		A00LoginService loginService = (A00LoginService) BeforeClassUse.getDPInfo();
		
		//*自己的方法
		Object[][] obj = loginService.getParamInfo();
		
		return obj;
	}
}
