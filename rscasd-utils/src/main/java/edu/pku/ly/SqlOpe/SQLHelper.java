package edu.pku.ly.SqlOpe;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLHelper {
	
	private static Connection conn = null;
	private static PreparedStatement ps = null;
    private static ResultSet rs = null;
	
	private static void getConnectionByJDBC() 
	{
		try
		{
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/juddiv3_wlj","root","");
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void ExecuteNoneQuery(String sql) throws SQLException
	{
		if(conn == null)
			getConnectionByJDBC();
		
		try {
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
            if (ps != null)
            {
            	ps.close();
            }
        }
	}
	
	public static void ExecuteNoneQuery(String sql, Object[] params) throws UnsupportedEncodingException, SQLException
	{
		if(conn == null)
			getConnectionByJDBC();
		
		try {
			ps = conn.prepareStatement(sql);
			prepareCommand(ps, params);
			ps.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
            if (ps != null)
            {
            	ps.clearParameters();
            	ps.close();
            }
        }
	}
	
	public static int GetLastInsertID()
	{
		if(conn == null)
			getConnectionByJDBC();
		
		int newid = 0;
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select LAST_INSERT_ID() as newid;");
			while (rs.next()) 
			{
				newid = Integer.parseInt(rs.getString("newid"));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return newid;
	}
	
	public static int ExecuteQuery(String sql)
	{
		int id = 0;
		
		if(conn == null)
			getConnectionByJDBC();
		
		try 
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				id = Integer.parseInt(rs.getString("id"));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return id;
	}
	
	public static ResultSet ExecuteQueryRtnSet(String sql)
	{
		ResultSet rs = null;
		
		if(conn == null)
			getConnectionByJDBC();
		
		try 
		{
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public static ResultSet ExecuteQueryRtnSet(String sql, Object[] params)
	{
		ResultSet rs = null;
		
		if(conn == null)
			getConnectionByJDBC();
		
		try 
		{
			ps = conn.prepareStatement(sql);
			prepareCommand(ps, params);
			rs = ps.executeQuery();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	
	private static void prepareCommand(PreparedStatement pstmt, Object[] parms) throws SQLException,UnsupportedEncodingException 
	{
		if (parms != null && parms.length > 0) 
		{
			for (int i = 1; i < parms.length + 1; i++) 
			{
				Object item = parms[i - 1];
				String typeName = item.getClass().getSimpleName();
				if (typeName.equals("String")) 
				{
					pstmt.setString(i, item.toString());
				} 
				else if (typeName.equals("Integer")) 
				{
					pstmt.setInt(i, Integer.parseInt(item.toString()));
				} 
				else if (typeName.equals("Date")) 
				{
					pstmt.setDate(i, Date.valueOf(item.toString()));
				} 
				else 
				{
					pstmt.setObject(i, item);
				}
			}
		}
	}
	
	public static void test(String sql) {
		if(conn == null)
			getConnectionByJDBC();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				String id = rs.getString("id");
				String address_id = rs.getString("address_id");
				System.out.println(id + " " + address_id);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		} 
	}
}
