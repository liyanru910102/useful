package zxjt.inte.report;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public abstract class BaseDao {
	protected Connection mConn;

	public BaseDao(Connection conn) {
		mConn = conn;
	}

	public Timestamp convertTime(java.util.Date date) {
		if (date == null) {
			return null;
		}
		return new Timestamp(date.getTime());
	}

	public void safeClose(ResultSet result, Statement stmt) {
		try {
			if (result != null)
				result.close();
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void safeClose(Statement stmt) {
		safeClose(null, stmt);
	}

}
