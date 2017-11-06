package zxjt.inte.controller;

import org.testng.annotations.DataProvider;

import zxjt.inte.service.B00LoginService;
import zxjt.inte.util.BeforeClassUse;

public class B00LoginController {

	// *自己的别名
	@DataProvider(name = "test1")
	//*自己的方法名
	public static Object[][] loginTest() {
	
		B00LoginService loginService = (B00LoginService) BeforeClassUse.getDPInfo();
		
		//*自己的方法
		Object[][] obj = loginService.getParamInfo();
		
		return obj;
	}
}
