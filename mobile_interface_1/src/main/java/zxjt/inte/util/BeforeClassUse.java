package zxjt.inte.util;

import java.util.HashMap;
import java.util.Map;

public class BeforeClassUse {

	public static void setDPInfo(Object aService) {
		String simpleName = Thread.currentThread().getStackTrace()[2].getClassName();
		String name = simpleName.substring(simpleName.lastIndexOf(".") + 1);
		String key = name.substring(0, 3);
		Map<String, Object> mapS = new HashMap<String, Object>();
		mapS.put(key, aService);
		DPContainer.setDpData(mapS);
	}

	public static Object getDPInfo() {
		String simpleName = Thread.currentThread().getStackTrace()[2].getClassName();
		String name = simpleName.substring(simpleName.lastIndexOf(".") + 1);
		String key = name.substring(0, 3);
		Map<String, Object> mapG = DPContainer.getDpData();
		Object aServiceG = mapG.get(key);
		return aServiceG;
	}
}
