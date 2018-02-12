package com.tigerjoys.np.cg.databases.generator;

import java.io.Serializable;
import java.util.List;

public class TableIndexBean implements Serializable {

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
	 * 是否是唯一索引
	 */
	private int unique;
	
	/**
	 * 唯一索引名称
	 */
	private String index_name;
	
	/**
	 * 字段类型
	 */
	private List<TableColumnBean> columnBeanList;

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

	public int getUnique() {
		return unique;
	}

	public void setUnique(int unique) {
		this.unique = unique;
	}

	public String getIndex_name() {
		return index_name;
	}

	public void setIndex_name(String index_name) {
		this.index_name = index_name;
	}

	public List<TableColumnBean> getColumnBeanList() {
		return columnBeanList;
	}

	public void setColumnBeanList(List<TableColumnBean> columnBeanList) {
		this.columnBeanList = columnBeanList;
	}

}
