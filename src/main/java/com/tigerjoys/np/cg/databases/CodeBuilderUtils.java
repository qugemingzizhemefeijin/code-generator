package com.tigerjoys.np.cg.databases;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CodeBuilderUtils {
	
	/**
	 * 关闭数据库连接
	 * @param rs - 返回的数据集
	 * @param st - sql语句执行对象
	 * @param co - 数据库连接
	 */
	public static void closeDBA(ResultSet rs , Statement st , Connection co){
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(st!=null) {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(co!=null) {
			try {
				co.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
