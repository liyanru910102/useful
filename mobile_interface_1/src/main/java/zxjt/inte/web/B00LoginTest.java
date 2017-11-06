package zxjt.inte.web;

import java.util.Map;

import javax.annotation.Resource;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import zxjt.inte.controller.B00LoginController;
import zxjt.inte.controller.BaseController;
import zxjt.inte.service.B00LoginService;
import zxjt.inte.util.BeforeClassUse;
import zxjt.inte.util.HttpUtil_All;
import zxjt.inte.util.JsonAssertUtil;

//自己的类名
public class B00LoginTest  extends BaseController{

	// *自己的Service名称
	@Resource
	private B00LoginService loginService;

	@BeforeClass
	public void setup() {
		BeforeClassUse.setDPInfo(loginService);
	}

	// *自己的controller和别名
	@Test(dataProvider = "test1", dataProviderClass = B00LoginController.class)

	// *自己的方法名
	public void B00LoginZL(Map<String,String> param) {
		
		// 发请求
		String response = HttpUtil_All.doPostSSL(param.get("url"), param);
		//System.out.println(response.toString());
		
		JsonAssertUtil.checkNull(response);
		
//		Map<String, String> regexMap = JsonAssertUtil.getRegex(param.get("expectmsg"),ParamConstant.A00_METHOD, ParamConstant.A00_SCHEMA_ZL);
//		String strjsonSchema = JsonAssertUtil.editSchemaInfo(ParamConstant.A00_SCHEMA_ZL, regexMap);
//		//验证  *自己的schema名称
//		JsonAssertUtil.JsonAssert(response, strjsonSchema);
	}
}
