package com.tigerjoys.np.cg.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 抽象的数据库类
 * @author chengang
 *
 */
public abstract class AbstractDataBase {
	
	/**
	 * driver String 数据库驱动,在服务器启动的时候赋予初始值。
	 */
	private String driver;
	
	/**
	 * url String 数据库连接地址,在服务器启动的时候赋予初始值。
	 */
	private String url;
	
	/**
	 * 数据库名称
	 */
	private String database;
    
    /**
     * username String 数据库连接用户名,在服务器启动的时候赋予初始值。
     */
	private String username;
    
    /**
     * password String 数据库连接密码,在服务器启动的时候赋予初始值。
     */
	private String password;
	
	public AbstractDataBase(String database){
		this(database , "51xiu" , "51xiu2108" , "192.168.20.3" , 3306);
	}
	
	public AbstractDataBase(String database,String username,String pass){
		this(database,username,pass,"192.168.20.3",3306);
	}
	
	public AbstractDataBase(String database,String username,String pass , String ip , int port){
		this.driver = "com.mysql.jdbc.Driver";
		this.url = "jdbc:mysql://"+ip+":"+port+"/"+database+"?jdbcCompliantTruncation=false&amp;useUnicode=true&amp;characterEncoding=gbk&amp;autoConnect=true";
		this.database = database;
		this.username = username;
		this.password = pass;
	}
	
	/**
	 * 获得对应的包名
	 * @return String
	 */
	public abstract String getPackageName();
	
	/**
	 * 获得XML的目录
	 * @return String
	 */
	public abstract String getXmlFolder();
	
	/**
	 * 数据库连接
	 * @return Connection
	 */
	public Connection getConnection(){
		Connection con=null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url,username,password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	/**
	 * 提交数据库操作
	 * @param con Connection
	 */
	public void commit(Connection con){
		try {
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新数据回滚
	 * @param con Connection
	 */
	public void rollback(Connection con){
		try {
			con.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 给数据库操作设置事务
	 * @param con Connection
	 */
	public void autoCommit(Connection con){
		try {
			con.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
