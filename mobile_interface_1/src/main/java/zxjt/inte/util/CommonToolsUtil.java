package zxjt.inte.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Predicate;

import net.minidev.json.JSONArray;
import zxjt.inte.entity.CommonInfo;

public class CommonToolsUtil {
	// 查询接口还灭有校验呢
	/**
	 * 去除入参数据中包含的非传入数据，例如“测试点”、“类型”等等
	 * 
	 * @param param
	 * @return
	 */
	public static Map<String, String> getRParam(Map<String, String> param) {
		Map<String, String> RParam = new HashMap<>();
		RParam.putAll(param);
		RParam.remove("isExcute");
		RParam.remove("type");
		RParam.remove("expectMsg");
		RParam.remove("testPoint");
		RParam.remove("url");
		RParam.remove("row");
		return RParam;
	}

	/**
	 * 返回校验列的集合，用来校验入参是否与数据库一致的
	 * 
	 * @param content
	 *            从数据中取出来的content
	 * @return 返回存储了method和入参params的map；params用来校验，method用来判断本次请求是哪种方式（post、get等）
	 */
	public static Map<String, List<String>> getValidateParam(String content) {
		Map<String, List<String>> mapParam = new HashMap<String, List<String>>();
		List<String> param = new ArrayList<>();
		List<String> method = new ArrayList<>();
		Object o = JsonPath.read(content, "$.params", new Predicate[0]);
		JSONArray ja = (JSONArray) o;
		for (int i = 0; i < ja.size(); i++) {
			Map<String, String> map = (Map<String, String>) ja.get(i);
			param.add(map.get("name"));
		}
		mapParam.put(ParamConstant.VALIDATEPARAM, param);
		JSONArray jsMethod = (JSONArray) (JsonPath.read(content, "$.methods", new Predicate[0]));
		String strMethod = "";
		if (jsMethod.size() > 0) {
			strMethod = (String) jsMethod.get(0);
		}
		method.add(strMethod);
		mapParam.put(ParamConstant.MEHTOD, method);
		return mapParam;
	}

	/**
	 * 将被依赖接口从数据库中取出来的值组合成入参的map
	 * 
	 * @param lisMap
	 *            数据库查询出来入参值
	 * @param commonParam
	 *            从公共数据表中查询出来的公共入参
	 * @return 拼接后指定url需要的入参值
	 */
	public static Map<String, String> getDependencyParamInfo(Object map, Map<String, String> commonParam) {

		List<Object> lisMap = (List<Object>) map;
		Map<String, String> mapParam = new HashMap<>();
		int rowIndex = 1;
		String paramUrl = "";
		Class clazz = lisMap.get(0).getClass();
		try {

			Method mUrl = clazz.getDeclaredMethod("getUrl");
			Method mContent = clazz.getDeclaredMethod("getContent");

			paramUrl = (String) mUrl.invoke(lisMap.get(0));
			String url = commonParam.get("url") + paramUrl;

			for (int i = 0; i < lisMap.size(); i++) {
				Class evCla = lisMap.get(i).getClass();
				Method mRowIndex = evCla.getDeclaredMethod("getRowindex");
				Method mCname = evCla.getDeclaredMethod("getCname");
				Method mCvalue = evCla.getDeclaredMethod("getCvalue");
				if (rowIndex != ((Integer) mRowIndex.invoke(lisMap.get(i)))) {
					throw new RuntimeException("依赖接口不能存在多条数据，请查证！");
				}

				mapParam.put((String) mCname.invoke(lisMap.get(i)), (String) mCvalue.invoke(lisMap.get(i)));

			}
			String content = (String) mContent.invoke(lisMap.get(0));
			mapParam.putAll(commonParam);
			mapParam.put("url", url);

			// 校验
			validateParamIsMatch(content, mapParam);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return mapParam;
	}

	/**
	 * 将测试接口从数据库中取出来的值组合成入参的map集合
	 * 
	 * @param lisMap
	 *            数据库查询出来入参值
	 * @param commonParam
	 *            从公共数据表中查询出来的公共入参
	 * @return 拼接后指定url需要的入参值
	 */
	public static List<Map<String, String>> getDependencyParamsInfo(Object map, Map<String, String> commonParam) {

		List<Object> lisMap = (List<Object>) map;
		Map<String, String> mapParam = new HashMap<>();
		List<Map<String, String>> lisTemp = new ArrayList<>();
		int rowIndex = 0;
		String paramUrl = "";
		Class clazz = lisMap.get(0).getClass();
		try {

			Method mUrl = clazz.getDeclaredMethod("getUrl");
			Method mContent = clazz.getDeclaredMethod("getContent");

			paramUrl = (String) mUrl.invoke(lisMap.get(0));

			for (int i = 0; i < lisMap.size(); i++) {
				Class evCla = lisMap.get(i).getClass();
				Method mRowIndex = evCla.getDeclaredMethod("getRowindex");
				Method mCname = evCla.getDeclaredMethod("getCname");
				Method mCvalue = evCla.getDeclaredMethod("getCvalue");

				if (rowIndex != ((Integer) mRowIndex.invoke(lisMap.get(i)))) {
					rowIndex = (Integer) mRowIndex.invoke(lisMap.get(i));
					lisTemp.add(mapParam);
					mapParam = new HashMap<>();
					mapParam.putAll(commonParam);
					String url = commonParam.get("url") + paramUrl;
					mapParam.put("url", url);
					mapParam.put("row", String.valueOf(rowIndex));
				}
				mapParam.put((String) mCname.invoke(lisMap.get(i)), (String) mCvalue.invoke(lisMap.get(i)));
			}
			lisTemp.add(mapParam);
			lisTemp.remove(0);

			// 校验
			String content = (String) mContent.invoke(lisMap.get(0));
			validateParamIsMatch(content, lisTemp.get(1));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lisTemp;
	}

	public static List<Map<String, String>> filterOfIsExcute(List<Map<String, String>> lisParams) {
		List<Map<String, String>> lis = new ArrayList<>();
		lis.addAll(lisParams);
		for (int i = 0; i < lis.size(); i++) {
			Map<String, String> map = lis.get(i);
			if (!Boolean.valueOf(map.get("type"))) {
				lis.remove(map);
			}
		}
		return lis;
	}

	/**
	 * 
	 * @param mapParam
	 *            发送请求所需的入参
	 * @return 响应内容
	 */
	public static String getResponseInfo(Map<String, String> mapParam) {

		Map<String, String> map = CommonToolsUtil.getRParam(mapParam);

		// 发请求
		String response = HttpUtil_All.doPostSSL(mapParam.get("url"), map);
		return response;
	}

	/**
	 * 
	 * @param content
	 *            数据库搜索出来的content
	 * @param mapTemp
	 *            要校验的每行数据的map
	 */
	public static void validateParamIsMatch(String content, Map<String, String> mapTemp) {
		try {
			Map<String, List<String>> mapStr = CommonToolsUtil.getValidateParam(content);
			List<String> lStr = mapStr.get(ParamConstant.VALIDATEPARAM);
			Map<String, String> m = CommonToolsUtil.getRParam(mapTemp);
			List<String> list2 = new ArrayList<String>();
			List<String> list3 = new ArrayList<String>();
			list2.addAll(m.keySet());
			if (!list2.containsAll(lStr) || !lStr.containsAll(list2)) {
				throw new RuntimeException("该接口入参已更新，请修正代码以匹配数据库的更改再执行！");
			} else {
				System.out.println("入参校验通过！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static String getPrice(String pricetype, String response) {

		String wtjg = "";
		try {
			switch (pricetype) {
			case "{PRICE}":
			case "市价":
				wtjg = String.valueOf(JsonPath.read(response, "$.kmmxx[0].jrkpj", new Predicate[0]));
				break;
			case "{LOW}":
				String dtjg = (String) JsonPath.read(response, "$.kmmxx[0].dtjg", new Predicate[0]);
				if ("".equals(dtjg) || null == dtjg) {
					throw new RuntimeException("查询接口返回字段异常：dtjg字段为空值");
				}
				wtjg = String.format("%.2f", Double.valueOf(dtjg) - 0.1);
				break;
			case "{HIGH}":
				String ztjg = (String) JsonPath.read(response, "$.kmmxx[0].ztjg", new Predicate[0]);
				if ("".equals(ztjg) || null == ztjg) {
					throw new RuntimeException("查询接口返回字段异常：ztjg字段为空值");
				}
				wtjg = String.format("%.2f", Double.valueOf(ztjg) + 0.1);
				break;
			case "{latestDealPrice}":
				String jg = (String) JsonPath.read(response, "$.kmmxx[0].wtjg", new Predicate[0]);
				if ("".equals(jg) || null == jg) {
					throw new RuntimeException("查询接口返回字段异常：wtjg字段为空值");
				}
				wtjg = String.format("%.2f", Double.valueOf(jg));
				break;
			case "{DealConfirmPrice}":
				String DealConfirmPrice = (String) JsonPath.read(response, "$.htxx[0].wtjg", new Predicate[0]);
				if ("".equals(DealConfirmPrice) || null == DealConfirmPrice) {
					throw new RuntimeException("查询接口返回字段异常：htxx[0].wtjg字段为空值");
				}
				wtjg = String.format("%.2f", Double.valueOf(DealConfirmPrice));
				break;
			case "{NULL}":
				wtjg = "";
				break;
			default:
				wtjg = pricetype;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return wtjg;

	}

	/**
	 * 获取查询接口的最大可买数量，赋值给下单接口报文中
	 * 
	 * @param 获取的买卖类别
	 * @param 获取的数量的类型
	 * @param 从接口返回的数据的被查找的字符串
	 * @return 返回修改好的价格
	 */
	public static String getOverBSQty(String mmlb, String qtytype, String CXRespose) {
		String actualqty = "";
		String qtyflag = "";
		if ("B".equals(mmlb)) {
			qtyflag = "$.kmmxx[0].kmsl";
		}
		if ("S".equals(mmlb)) {
			qtyflag = "$.kmmxx[0].gfkys";
		}
		switch (qtytype) {
		case "{OverBSQty}":
			try {
				actualqty = String.valueOf(
						Integer.parseInt((String) JsonPath.read(CXRespose, qtyflag, new Predicate[0])) / 1000 * 1000
								+ 1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());// 123456
			}
			break;
		default:
			actualqty = qtytype;
		}
		return actualqty;
	}

	public static Map<String, String> getCommonParam(List<CommonInfo> lisCo) {
		Map<String, String> commonParam = new HashMap<>();
		for (CommonInfo a : lisCo) {
			commonParam.put(a.getCname(), a.getCvalue());
		}
		return commonParam;
	}

	public static Object[][] getObjParam(List<Map<String, String>> lisTemp) {
		Object[][] obj = new Object[lisTemp.size()][1];
		for (int j = 0; j < obj.length; j++) {
			obj[j][0] = lisTemp.get(j);
		}
		return obj;
	}

}
