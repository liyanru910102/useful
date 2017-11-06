package zxjt.inte.entity;

public class B00Login {
	private String url;
	private String khbz;
	private String jymm;
	private String khbzlx;
	private String lhxx;
	private String sessionid;
	private String token;
	private String yybdm;
	private String rzfs;
	private String expectmsg;
	private String testpoint;

	public String getExpectmsg() {
		return expectmsg;
	}

	public void setExpectmsg(String expectmsg) {
		this.expectmsg = expectmsg;
	}

	public String getTestpoint() {
		return testpoint;
	}

	public void setTestpoint(String testpoint) {
		this.testpoint = testpoint;
	}

	public String getJymm() {
		return jymm;
	}

	public void setJymm(String jymm) {
		this.jymm = jymm;
	}

	public String getKhbzlx() {
		return khbzlx;
	}

	public void setKhbzlx(String khbzlx) {
		this.khbzlx = khbzlx;
	}

	public String getLhxx() {
		return lhxx;
	}

	public void setLhxx(String lhxx) {
		this.lhxx = lhxx;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getYybdm() {
		return yybdm;
	}

	public void setYybdm(String yybdm) {
		this.yybdm = yybdm;
	}

	public String getRzfs() {
		return rzfs;
	}

	public void setRzfs(String rzfs) {
		this.rzfs = rzfs;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKhbz() {
		return khbz;
	}

	public void setKhbz(String khbz) {
		this.khbz = khbz;
	}

	@Override
	public String toString() {
		return "A00Login [url=" + url + ", khbz=" + khbz + ", jymm=" + jymm + ", khbzlx=" + khbzlx + ", lhxx=" + lhxx
				+ ", sessionid=" + sessionid + ", token=" + token + ", yybdm=" + yybdm + ", rzfs=" + rzfs
				+ ", expectmsg=" + expectmsg + ", testpoint=" + testpoint + "]";
	}



}
