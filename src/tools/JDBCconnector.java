package tools;

import java.awt.Label;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
					+ patent.getPatentAbstract() + "','" + patent.getClaim() + "','" + patent.getPatentText() + "','"
					+patent.getTitleSeg()+"','"+patent.getPatentTextSeg()+"','"+patent.getPatentIpc()+"','"
					+patent.getKeyWords()+"','"+patent.getQuote()+"','"+patent.getBeQuote()+"')";
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
	
	public static boolean SaveSql(String sql){
		int updateRoll = 0;
		try {
			Class.forName(name);
			conn = DriverManager.getConnection(url, user, password);
			stmt = conn.createStatement();
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
	
	public static List<String> ExecuteQuery(String sql){
		List<String> reList = new ArrayList<>();
		try {
			Class.forName(name);
			conn = DriverManager.getConnection(url, user, password);
			stmt = conn.createStatement();
			Statement stmt = conn.createStatement();  
			ResultSet rs = stmt.executeQuery(sql);
		    while (rs.next()) {
			  reList.add(rs.getString(1));
		    }
		    rs.close();
		    stmt.close();
		    conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return reList;
	}
	
	public static List<List> ExecuteQuerySeg(String sql){
		List<List> llList = new ArrayList<>();
		try {
			Class.forName(name);
			conn = DriverManager.getConnection(url, user, password);
			stmt = conn.createStatement();
			Statement stmt = conn.createStatement();  
			ResultSet rs = stmt.executeQuery(sql);
		    while (rs.next()) {
		    	List<String> sList = new ArrayList<>();
		    	sList.add(rs.getString(1));
		    	sList.add(rs.getString(2));
		    	sList.add(rs.getString(3));
		    	llList.add(sList);
		    }
		    rs.close();
		    stmt.close();
		    conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return llList;
	}
	
	public static int ExecuteQueryCount(String sql){
		int count = 0;
		try {
			Class.forName(name);
			conn = DriverManager.getConnection(url, user, password);
			stmt = conn.createStatement();
			Statement stmt = conn.createStatement();  
			ResultSet rs = stmt.executeQuery(sql);
		    if (rs.next()) {
			  count = rs.getInt(1);
		    }
		    rs.close();
		    stmt.close();
		    conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return count;
	}
	
	public static void ExecuteUpdate(String sql){
		try {
			Class.forName(name);
			conn = DriverManager.getConnection(url, user, password);
			stmt = conn.createStatement();
			System.out.println(sql);
			Statement stmt = conn.createStatement();  
			int status = stmt.executeUpdate(sql);
			stmt.close();
			conn.close();
			System.out.println(status);
		} catch (Exception e) {
			e.printStackTrace();
		} 
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
