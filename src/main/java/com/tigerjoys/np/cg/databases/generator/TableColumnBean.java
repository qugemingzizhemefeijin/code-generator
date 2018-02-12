package com.tigerjoys.np.cg.databases.generator;

import com.tigerjoys.np.cg.databases.util.JsonHelper;

import java.io.Serializable;

public class TableColumnBean implements Serializable {

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
	 * 字段名称
	 */
	private String column_name;
	
	/**
	 * 转驼峰属性名称
	 */
	private String profile_name;
	
	/**
	 * 序列，从1开始
	 */
	private int ordinal_position;
	
	/**
	 * 默认数据
	 */
	private String column_default;
	
	/**
	 * 是否允许null
	 */
	private boolean is_nullable;
	
	/**
	 * 数据类型,int varchar...
	 */
	private String data_type;
	
	/**
	 * 数据映射类型，如果DateTime为TIMESTAMP
	 */
	private String show_data_type;
	
	/**
	 * 对应java对象类型
	 */
	private String java_type;
	
	/**
	 * 对应java的基本类型
	 */
	private String java_basic_type;
	
	/**
	 * 字段类型，int(2) varchar(200)...
	 */
	private String column_type;
	
	/**
	 * 字段长度
	 */
	private int length;
	
	/**
	 * 字段的索引,PRI主键，MUL索引
	 */
	private String column_key;
	
	/**
	 * 字段备注
	 */
	private String column_comment;
	
	/**
	 * 其他，如auto_increment
	 */
	private String extra;
	
	/**
	 * 是否自增
	 */
	private boolean auto_increment;

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

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public int getOrdinal_position() {
		return ordinal_position;
	}

	public void setOrdinal_position(int ordinal_position) {
		this.ordinal_position = ordinal_position;
	}

	public String getColumn_default() {
		return column_default;
	}

	public void setColumn_default(String column_default) {
		this.column_default = column_default;
	}

	public boolean isIs_nullable() {
		return is_nullable;
	}

	public void setIs_nullable(boolean is_nullable) {
		this.is_nullable = is_nullable;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public String getColumn_type() {
		return column_type;
	}

	public void setColumn_type(String column_type) {
		this.column_type = column_type;
	}

	public String getColumn_key() {
		return column_key;
	}

	public void setColumn_key(String column_key) {
		this.column_key = column_key;
	}

	public String getColumn_comment() {
		return column_comment;
	}

	public void setColumn_comment(String column_comment) {
		this.column_comment = column_comment;
	}

	public String getJava_type() {
		return java_type;
	}

	public void setJava_type(String java_type) {
		this.java_type = java_type;
	}

	public String getShow_data_type() {
		return show_data_type;
	}

	public void setShow_data_type(String show_data_type) {
		this.show_data_type = show_data_type;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public boolean isAuto_increment() {
		return auto_increment;
	}

	public void setAuto_increment(boolean auto_increment) {
		this.auto_increment = auto_increment;
	}

	public String getJava_basic_type() {
		return java_basic_type;
	}

	public void setJava_basic_type(String java_basic_type) {
		this.java_basic_type = java_basic_type;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getProfile_name() {
		return profile_name;
	}

	public void setProfile_name(String profile_name) {
		this.profile_name = profile_name;
	}

	@Override
	public String toString() {
		return JsonHelper.toJson(this);
	}

}
