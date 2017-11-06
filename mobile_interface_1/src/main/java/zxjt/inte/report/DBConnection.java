package zxjt.inte.report;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;

import zxjt.inte.util.FolderTypes;
import zxjt.inte.util.GetFolderPath;

public abstract class DBConnection {
	private static Connection conn;

	public static Connection getConnection() throws SQLException, IOException, ClassNotFoundException {
		if (conn == null) {
			System.out.println("db is close");
			//String vFilePath = LightContext.getFolderPath(FolderTypes.REPORT) + "cache.db";
			//String vFilePath =System.getProperty("user.dir")+ "/report/cache.db";
			String vFilePath = GetFolderPath.getFolderPath(FolderTypes.REPORT)+ "cache.db";
			File vFile = new File(vFilePath);			
			if (!vFile.exists()) {				
				FileUtils.copyInputStreamToFile(DBConnection.class.getResourceAsStream("template.db"), vFile);
				
			}
			Class.forName("org.sqlite.JDBC");
			String vConnStr = "jdbc:sqlite://" + vFilePath;
			conn = DriverManager.getConnection(vConnStr);
			if(conn == null)
			{
				System.out.println("conn is null");
			}
//			else
//			{
//				System.out.println(conn.toString());
//			}

			// 开启外键
			Statement vStmt = conn.createStatement();
			vStmt.execute("PRAGMA foreign_keys = ON;");
			vStmt.close();
		}
		return conn;
	}

	public static void close() {
		if (conn != null) {
			System.out.println("close db");
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}

}
