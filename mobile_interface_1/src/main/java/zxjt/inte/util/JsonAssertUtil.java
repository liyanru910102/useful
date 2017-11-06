package zxjt.inte.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonNodeReader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Predicate;

import net.minidev.json.JSONArray;

public class JsonAssertUtil {

	/**
	 * 姝ゆ柟娉曚娇鐢ㄤ簬鍦═est绫讳腑浣跨敤锛岄渶瑕乯sonSchema浜庢祴璇曠被鍚嶄竴鑷村苟鏀句簬json鏂囦欢澶瑰唴
	 * 
	 * @param jsonPath
	 *            浼犲叆琚娴嬬殑json瀛楃涓�
	 */
	/**
	 * 鏍￠獙杩斿洖鐨刯son鏍煎紡鏄惁姝ｇ‘
	 * 
	 * @param except
	 *            鏈熷緟缁撴灉
	 * @param jsonStr
	 *            寰呬紶鍏ョ殑琚祴json瀛楃涓�
	 * @param jsonSchemaName
	 *            schema鏍￠獙鏂囦欢鐨勬枃浠跺悕锛堟棤闇�鍔�.json锛�
	 */
	public static void JsonAssert(String jsonStr, String strjsonSchema) {
		JsonNode json = null;
		ProcessingReport report = null;
		InputStream isIn = null;
		try {

			isIn = new ByteArrayInputStream(strjsonSchema.getBytes("UTF-8"));

			JsonNode jsonSchema = readJsonFile(isIn);

			json = new ObjectMapper().readTree(jsonStr);

			report = JsonSchemaFactory.byDefault().getValidator().validate(jsonSchema, json, true);

		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ProcessingException e) {
			e.printStackTrace();

		} finally {
			try {
				isIn.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			if (report.toString().indexOf("error") != -1) {
				String re = report.toString();
				int start = re.indexOf("--- BEGIN MESSAGES ---") + "--- BEGIN MESSAGES ---".length() + 1;
				int end = re.indexOf("---  END MESSAGES  ---") - 1;
				re = (String) re.subSequence(start, end);
				if (re.indexOf("error") != -1) {
					throw new RuntimeException(re);
				}
			}
		}

	}

	/**
	 * 浠庢枃浠朵腑璇诲彇杞崲涓簀sonNode绫�
	 * 
	 * @param filePath鏂囦欢璺緞
	 * @return
	 */
	public static JsonNode readJsonFile(InputStream isIn) {
		JsonNode instance = null;
		try {
			instance = new JsonNodeReader().fromReader(new InputStreamReader(isIn, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return instance;
	}

	/**
	 * 
	 * @param vSchemaName
	 *            Schema的名称
	 * @param param
	 *            从data.xml中获取到的入参
	 * @param paramSchema
	 *            存储了要验证的合同序号类型（名称：正则）的map
	 */
	public static String editSchemaInfo(String vSchemaName, Map<String, String> paramSchema) {

		String strSchema = "";
		String line = "";
		StringBuilder sb = new StringBuilder();
		JsonAssertUtil jsu;
		BufferedReader br;
		InputStream isIn = null;
		try {
			jsu = JsonAssertUtil.class.newInstance();

			isIn = jsu.getClass().getResourceAsStream("/json/" + vSchemaName + ".json");

			br = new BufferedReader(new InputStreamReader(isIn, "UTF-8"));

			while ((line = br.readLine()) != null) {
				sb.append(line);

				strSchema = sb.toString();
			}

			for (String key : paramSchema.keySet()) {
				// String value = "\"" + paramSchema.get(key) + "\"";
				String value = paramSchema.get(key);
				strSchema = strPj(strSchema, key, "pattern", value);
			}
		} catch (FileNotFoundException e1) {
			throw new RuntimeException(e1);
		} catch (InstantiationException e2) {
			throw new RuntimeException(e2);
		} catch (IllegalAccessException e2) {
			throw new RuntimeException(e2);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				isIn.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return strSchema;
	}

	/**
	 * 
	 * @param strmodify
	 *            要修改的字符串
	 * @param key
	 *            字符串中的关键字(大项目）
	 * @param pattern
	 *            文本比较需要用到的keyword
	 * @param msg
	 *            keyword对应的文本内容或者正则（已拼接好的）
	 * @return 修改后的字符串
	 */

	private static String strPj(String strmodify, String key, String datatype, String msg) {
		JSONObject jb = null;
		String local[] = new String[2];
		JSONObject dataJson = new JSONObject(strmodify);
		if (key.contains("_")) {
			local = key.split("_");
			jb = dataJson.getJSONObject("properties").getJSONObject(local[0]).getJSONObject("items")
					.getJSONObject("properties").getJSONObject(local[1]);
		} else {
			switch (key) {
			case "code":
				jb = dataJson.getJSONObject("properties").getJSONObject("cljg").getJSONObject("items")
						.getJSONObject("properties").getJSONObject("code");
				break;
			case "message":
				jb = dataJson.getJSONObject("properties").getJSONObject("cljg").getJSONObject("items")
						.getJSONObject("properties").getJSONObject("message");
				break;
			}
		}
		jb.remove("enum");
		jb.put(datatype, msg);
		return dataJson.toString();

	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> getRegex(Map<String, String> paramSchema, String series, String schemaName) {
		String methodName = schemaName.substring(0, schemaName.lastIndexOf("_"));
		;
		JsonAssertUtil jau;
		Map<String, String> regexMap = null;
		InputStream isIn = null;
		try {
			jau = JsonAssertUtil.class.newInstance();

			isIn = jau.getClass().getResourceAsStream("/regexObjectLib/" + series + ".json");
			InputStreamReader reader;
			Gson gson = new Gson();
			// FileReader reader;

			reader = new InputStreamReader(isIn);
			JsonObject jsonRegex = gson.fromJson(reader, JsonObject.class);
			JSONArray regexArray = JsonPath.read(jsonRegex.toString(), "$." + methodName, new Predicate[0]);
			regexMap = (Map<String, String>) regexArray.get(0);

			if (regexMap == null || regexMap.size() <= 0) {
				throw new RuntimeException("/regexObjectLib/" + series + ".json" + "中无信息，请添加后再执行");
			}
			if (null != paramSchema) {
				regexMap.putAll(paramSchema);
			}
		} catch (InstantiationException e1) {

			e1.printStackTrace();
			throw new RuntimeException(e1);
		} catch (IllegalAccessException e1) {

			e1.printStackTrace();
			throw new RuntimeException(e1);
		} finally {
			try {
				isIn.close();
			} catch (IOException e) {

				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return regexMap;
	}

	public static String getMsgRex(String message) {
		String str = message;
		if (message.indexOf("(") > -1 || message.indexOf(")") > -1 || message.indexOf("[") > -1
				|| message.indexOf("]") > -1 || message.indexOf(".") > -1) {
			str = message.replace("(", "\\(").replace(")", "\\)").replace("[", "\\[").replace("]", "\\]").replace(".",
					"\\.");
		}
		return str;
	}

	/**
	 * 
	 * @param respose
	 *            服务器返回信息内容
	 */
	public static void checkNull(String respose) {

		try {
			if (respose == null) {
				throw new RuntimeException("response is null!");
			}
			LinkedHashMap<String, JSONArray> result = JsonPath.read(respose, "$", new Predicate[0]);
			for (String key : result.keySet()) {
				JSONArray js = result.get(key);
				if (js.size() < 1) {
					throw new RuntimeException("接口响应字符串中，节点：" + key + " 的内容为空");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param json
	 *            string类型的json
	 * @param path
	 *            指定路径如：$.a[0].b 一级路径下的a是一个数组，取第一组数据中的key值为b的键对的value
	 * @return 返回对应的value值
	 */
	public static String getValue(String json, String path) {
		String ret = "";
		Object o = JsonPath.read(json, path, new Predicate[0]);
		ret = String.valueOf(o);
		return ret;
	}

	/**
	 * 校验依赖的查询接口返回值
	 * 
	 * @param cxResponse
	 *            查询接口返回的json字符串
	 * @param map
	 *            需要拼接到正则表达式中的内容，没有则传递null
	 * @param schemaName
	 *            该接口的shemaname
	 */
	public static void checkDependenceResponse(String cxResponse, Map<String, String> map, String series,
			String schemaName) {
		JsonAssertUtil.checkNull(cxResponse);
		Map<String, String> regexMap = JsonAssertUtil.getRegex(map, series, schemaName);
		String strjsonSchema = JsonAssertUtil.editSchemaInfo(schemaName, regexMap);
		JsonAssertUtil.JsonAssert(cxResponse, strjsonSchema);
	}

	public static Map<String, String> parseJson2(String filepath) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		Gson json = new Gson();
		try {
			JsonAssertUtil cls = JsonAssertUtil.class.newInstance();
			InputStream isIn = cls.getClass().getResourceAsStream("/name.json");
			InputStreamReader reader = new InputStreamReader(isIn, "UTF-8");
			jsonMap = json.fromJson(reader, new TypeToken<Map<String, String>>() {
				private static final long serialVersionUID = -7318315516540777308L;
			}.getType());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReflectiveOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonMap;

	}
}
