package zxjt.inte.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import zxjt.inte.dao.B00LoginDao;
import zxjt.inte.entity.B00Login;
import zxjt.inte.service.B00LoginService;

@Service
public class B00LoginServiceImpl implements B00LoginService {

	@Resource
	private B00LoginDao loginDao;

	public Object[][] getParamInfo() {

		Object[][] obj = new Object[2][1];
		List<B00Login> lis = loginDao.getLoginInfo();
		for (int i = 0; i < lis.size(); i++) {
			Map<String,String> mapS = new HashMap<String,String>();
			mapS.put("khbz", lis.get(i).getKhbz());
			mapS.put("url", lis.get(i).getUrl());
			mapS.put("jymm",lis.get(i).getJymm());
			mapS.put("khbzlx",lis.get(i).getKhbzlx());
			mapS.put("lhxx",lis.get(i).getLhxx());
			mapS.put("rzfs",lis.get(i).getRzfs());
			mapS.put("sessionid",lis.get(i).getSessionid());
			mapS.put("token",lis.get(i).getToken());
			mapS.put("yybdm",lis.get(i).getYybdm());
			mapS.put("expectmsg",lis.get(i).getExpectmsg());
			mapS.put("testpoint",lis.get(i).getTestpoint());

			
			obj[i][0] = mapS;
		}
		return obj;
	}

}
