package zxjt.inte.report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class StepDao extends BaseDao {

	public StepDao(Connection conn) {
		super(conn);
	}

	public void add(StepBean bean) {
		String vSql = "insert into t_step (test_id, status, timestamp, param, result, stack_trace, screenshot) values (?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement vStmt = null;
		ResultSet vResult = null;
		try {
			vStmt = mConn.prepareStatement(vSql);
			int i = 0;
			vStmt.setInt(++i, bean.getTestId());
			vStmt.setInt(++i, bean.getStatus());
			vStmt.setTimestamp(++i, convertTime(bean.getTimeStamp()));
			vStmt.setString(++i, bean.getParam());
			vStmt.setString(++i, bean.getResult());
			vStmt.setString(++i, bean.getStackTrace());
			vStmt.setString(++i, bean.getScreenshot());
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

	public List<StepBean> getAll(int testId) {
		String vSql = "select * from t_step where test_id=?";
		PreparedStatement vStmt = null;
		ResultSet vResult = null;
		List<StepBean> vRet = new ArrayList<>();
		try {
			vStmt = mConn.prepareStatement(vSql);
			vStmt.setInt(1, testId);
			vResult = vStmt.executeQuery();
			while (vResult.next()) {
				StepBean vBean = new StepBean();
				vBean.setId(vResult.getInt("id"));
				vBean.setTestId(testId);
				vBean.setStatus(vResult.getInt("status"));
				vBean.setTimeStamp(vResult.getTimestamp("timestamp"));
				vBean.setParam(vResult.getString("param"));
				vBean.setResult(vResult.getString("result"));
				vBean.setStackTrace(vResult.getString("stack_trace"));
				vBean.setScreenshot(vResult.getString("screenshot"));
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
