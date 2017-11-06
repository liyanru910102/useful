package zxjt.inte.report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;




public class TestDao extends BaseDao {

	public TestDao(Connection conn) {
		super(conn);
	}

	public void add(TestBean bean) {
		String vSql = "insert into t_test (name, start_time, end_time) values (?, ?, ?);";
		PreparedStatement vStmt = null;
		ResultSet vResult = null;
		try {
			vStmt = mConn.prepareStatement(vSql);
			vStmt.setString(1, bean.getName());
			vStmt.setTimestamp(2, convertTime(bean.getStartTime()));
			vStmt.setTimestamp(3, convertTime(bean.getEndTime()));
			vStmt.executeUpdate();
			vResult = vStmt.getGeneratedKeys();
			//Assert.isTrue(vResult.next(), "Can't get generated key");
			bean.setId(vResult.getInt(1));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			safeClose(vResult, vStmt);
		}
	}

	public void update(TestBean bean) {
		String vSql = "update t_test set name=?, start_time=?, end_time=? where id=?";
		PreparedStatement vStmt = null;
		try {
			vStmt = mConn.prepareStatement(vSql);
			vStmt.setString(1, bean.getName());
			vStmt.setTimestamp(2, convertTime(bean.getStartTime()));
			vStmt.setTimestamp(3, convertTime(bean.getEndTime()));
			vStmt.setInt(4, bean.getId());
			vStmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			safeClose(vStmt);
		}
	}

	public List<TestBean> getAll() {
		String vSql = "select * from t_test;";
		Statement vStmt = null;
		ResultSet vResult = null;
		List<TestBean> vRet = new ArrayList<>();
		try {
			vStmt = mConn.createStatement();
			vResult = vStmt.executeQuery(vSql);
			while (vResult.next()) {
				TestBean vBean = new TestBean();
				vBean.setId(vResult.getInt("id"));
				vBean.setName(vResult.getString("name"));
				vBean.setStartTime(vResult.getTimestamp("start_time"));
				vBean.setEndTime(vResult.getTimestamp("end_time"));
				vRet.add(vBean);
				//System.out.println(vBean);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			safeClose(vResult, vStmt);
		}
		return vRet;
	}

}
