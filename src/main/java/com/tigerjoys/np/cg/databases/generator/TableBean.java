package com.tigerjoys.np.cg.databases.generator;

import java.io.Serializable;
import java.util.Date;

public class TableBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 数据库名
	 */
	private String table_schema;
	
	/**
	 * 表名
	 */
	private String table_name;
	
	/**
	 * 表引擎
	 */
	private String engine;
	
	/**
	 * 表行数
	 */
	private long table_rows;
	
	/**
	 * 平均数据大小
	 */
	private long avg_row_length;
	
	/**
	 * 自增步数，0是非自增
	 */
	private long auto_increment;
	
	/**
	 * 表创建时间
	 */
	private Date create_time;
	
	/**
	 * 表的字符集
	 */
	private String table_collation;
	
	/**
	 * 表备注
	 */
	private String table_comment;

	public String getTable_schema() {
		return table_schema;
	}

	public void setTable_schema(String table_schema) {
		this.table_schema = table_schema;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public long getTable_rows() {
		return table_rows;
	}

	public void setTable_rows(long table_rows) {
		this.table_rows = table_rows;
	}

	public long getAvg_row_length() {
		return avg_row_length;
	}

	public void setAvg_row_length(long avg_row_length) {
		this.avg_row_length = avg_row_length;
	}

	public long getAuto_increment() {
		return auto_increment;
	}

	public void setAuto_increment(long auto_increment) {
		this.auto_increment = auto_increment;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getTable_collation() {
		return table_collation;
	}

	public void setTable_collation(String table_collation) {
		this.table_collation = table_collation;
	}

	public String getTable_comment() {
		return table_comment;
	}

	public void setTable_comment(String table_comment) {
		this.table_comment = table_comment;
	}

	@Override
	public String toString() {
		return "TableBean [table_schema=" + table_schema + ", table_name="
				+ table_name + ", engine=" + engine + ", table_rows="
				+ table_rows + ", avg_row_length=" + avg_row_length
				+ ", auto_increment=" + auto_increment + ", create_time="
				+ create_time + ", table_collation=" + table_collation
				+ ", table_comment=" + table_comment + "]";
	}

}
