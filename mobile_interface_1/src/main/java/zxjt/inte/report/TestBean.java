package zxjt.inte.report;

import java.util.Date;

public class TestBean {
	
	private int id;
	private String name;
	private Date startTime;
	private Date endTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@Override
	public String toString() {
		return "TestBean [id=" + id + ", name=" + name + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}

}
