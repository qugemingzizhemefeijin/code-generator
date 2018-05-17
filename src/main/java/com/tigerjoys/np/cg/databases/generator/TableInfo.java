package com.tigerjoys.np.cg.databases.generator;

import java.util.List;

public class TableInfo {
	
	/**
	 * 表名
	 */
	private String table_name;
	
	/**
	 * bean名称
	 */
	private String beanName;
	
	/**
	 * 作者
	 */
	private String author;
	
	/**
	 * 操作目录
	 */
	private String directory;
	
	/**
	 * 字段列表
	 */
	private List<TableColumnBean> columnList;
	
	/**
	 * 索引列表
	 */
	private List<TableIndexBean> indexList;
	
	/**
	 * 唯一索引列表
	 */
	private List<TableIndexBean> indexUniqueList;
	
	/**
	 * TableBean
	 */
	private TableBean tableBean;
	
	/**
	 * java 包路径
	 */
	private String packageName;
	
	/**
	 * 基础的Entity类路径
	 */
	private String baseEntityClassName;
	
	/**
	 * 主键字段列表
	 */
	private List<TableColumnBean> primaryColumnList;

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public List<TableColumnBean> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<TableColumnBean> columnList) {
		this.columnList = columnList;
	}

	public TableBean getTableBean() {
		return tableBean;
	}

	public void setTableBean(TableBean tableBean) {
		this.tableBean = tableBean;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<TableColumnBean> getPrimaryColumnList() {
		return primaryColumnList;
	}

	public void setPrimaryColumnList(List<TableColumnBean> primaryColumnList) {
		this.primaryColumnList = primaryColumnList;
	}

	public List<TableIndexBean> getIndexList() {
		return indexList;
	}

	public void setIndexList(List<TableIndexBean> indexList) {
		this.indexList = indexList;
	}

	public List<TableIndexBean> getIndexUniqueList() {
		return indexUniqueList;
	}

	public void setIndexUniqueList(List<TableIndexBean> indexUniqueList) {
		this.indexUniqueList = indexUniqueList;
	}

	public String getBaseEntityClassName() {
		return baseEntityClassName;
	}

	public void setBaseEntityClassName(String baseEntityClassName) {
		this.baseEntityClassName = baseEntityClassName;
	}

}
