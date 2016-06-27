package tools;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import po.Patent;

public class JDBCconnector {
	public static final String url = "jdbc:mysql://localhost:3306/PatentData?useUnicode=true&characterEncoding=utf-8";
	public static final String name = "com.mysql.jdbc.Driver";
	public static final String user = "root";
	public static final String password = "";

	public static Connection conn = null;
	public static Statement stmt = null;

	public static boolean save(Patent patent) {
		int updateRoll = 0;
		try {
			Class.forName(name);
			conn = DriverManager.getConnection(url, user, password);
			stmt = conn.createStatement();
			String sql = "insert into Patent values('" + patent.getPatentId() + "','" + patent.getApplyId() + "','"
					+ patent.getTitle() + "','" + patent.getProposer() + "','" + patent.getAuthor() + "','"
					+ patent.getPatentAbstract() + "','" + patent.getClaim() + "','" + patent.getPatentText() + "')";
			//String tsql = new String(sql.getBytes("gb2312"),"UTF-8");
			System.out.println(sql);
			updateRoll = stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (updateRoll == 1)
			return true;
		else
			return false;
	}
	
//	public static void main(String[] args) {
//		Patent p= new Patent();
//		try {
//			p.setTitle(new String("我帅不帅".getBytes(), "utf-8"));
//			//p.setTitle("我帅不帅");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		save(p);
//	}
	
}
