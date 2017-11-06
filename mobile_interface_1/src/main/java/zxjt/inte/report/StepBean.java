package zxjt.inte.report;

import java.util.Date;

public class StepBean {
	private int id;
	private int testId;
	private int status = -1;
	private Date timeStamp;
	private String param;
	private String result;
	private String stackTrace;
	private String screenshot;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public String getScreenshot() {
		return screenshot;
	}

	public void setScreenshot(String screenshot) {
		this.screenshot = screenshot;
	}

	@Override
	public String toString() {
		return "StepBean [id=" + id + ", testId=" + testId + ", status=" + status + ", timeStamp=" + timeStamp
				+ ", param=" + param + ", result=" + result + ", stackTrace=" + stackTrace + ", screenshot="
				+ screenshot + "]";
	}

}
