package zxjt.inte.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelData2DB {

	// 1101:数据过滤功能还没测试；往数据库里插入excel数据的功能还没写完；明天把这个功能写完，然后把数据插入到数据库，在统一执行下
	public static void readFile(File file) {

		Connection con = null;
		PreparedStatement ps = null;
		String sql = "insert into t_row_ptyw_info(ID,cid,rid,cvalue) values(?,?,?,?)";
		int count = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/light?useUnicode=true&characterEncoding=utf8", "web", "123123");
			ps = con.prepareStatement(sql);

			// ps.setString(1, student.getNo());
			// ps.setString(2, student.getName());
			// ps.setString(3, student.getAge());
			// ps.setString(4, String.valueOf(student.getScore()));
			// boolean flag = ps.execute();
			Map<String,Integer> map = new HashMap<>();
			map.put("jysdm", 1);
			map.put("gddm", 2);
			map.put("mmlb", 3);
			map.put("zqdm", 4);
			map.put("wtsl", 5);
			map.put("wtjg", 6);
			map.put("wtlx", 7);
			map.put("isExcute", 8);
			map.put("type", 9);
			map.put("expectMsg", 10);
			map.put("testPoint", 11);
			
			
			
			
			InputStream is = new FileInputStream(file);
			XSSFWorkbook wb = new XSSFWorkbook(is);
			XSSFCell cell = null;
			// for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets();
			// sheetIndex++) {
			// XSSFSheet st = wb.getSheetAt(sheetIndex);
			int rowid = 925;
			XSSFSheet st = wb.getSheet("普通交易_普通业务_委托下单_ALL");
			for (int rowIndex = 1; rowIndex <= st.getLastRowNum(); rowIndex++) {

				XSSFRow row = st.getRow(rowIndex);
				int column = row.getPhysicalNumberOfCells();
				if(column!=map.size())
				{
					throw new RuntimeException("excel表格的列长度与数据库存储的列长度不一致！");
				}
				for (int j = 0; j < column; j++) {
					cell = row.getCell(j);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					String cellValue =  st.getRow(0).getCell(j).getStringCellValue();
					ps.setInt(1, 4885);
					int cid = map.get(cellValue);
					ps.setInt(2, cid);
					ps.setInt(3, rowid);
					ps.setString(4,cell.getStringCellValue());
					ps.addBatch();
					count +=1;
				}

				if (count > 1000) {
					ps.executeBatch();
					count = 0;
				}
				rowid+=1;
			}
			// }
			ps.executeBatch();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Logger log = Logger.getLogger("zxjtInterface"); 
         log.info("asdfsadfsadf"); 
//         log.debug("IIIIIIIIIIIIIIIIII"); 
		//readFile(new File("C:\\Users\\Administrator\\Desktop\\users1.xlsx"));

	}
}