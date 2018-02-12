package ${packageName}.entity;

import java.io.Serializable;
<#list importPackageSet as clazz>
import ${clazz};
</#list>
import ${corePackage}.BaseEntity;

/**
 * 数据库中  ${tableBean.table_comment}[${tableName}] 表对应的实体类
 * @author ${author}
 * @Date ${createDate}
 *
 */
@Table(name="${tableName}")
public class ${beanName} extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	<#if fieldList??&&fieldList?size gt 0>
	<#list fieldList as field>
	
	/**
	 * ${field.column_comment}
	 */
	<#if "PRI"==field.column_key>
	<#if field.auto_increment>
	@Id
	<#else>
	@Id(increment=false)
	</#if>
	</#if>
	@Column(name="${field.column_name}",nullable=${field.is_nullable?string('true','false')},jdbcType=JdbcType.${field.show_data_type?upper_case},comment="${field.column_comment}")
	private ${field.java_type} ${field.column_name};
	</#list></#if>
	
	<#if fieldList??&&fieldList?size gt 0>
	<#list fieldList as field>
	public ${field.java_type} get${field.column_name?cap_first}() {
		return ${field.column_name};
	}

	public void set${field.column_name?cap_first}(${field.java_type} ${field.column_name}) {
		this.${field.column_name} = ${field.column_name};
	}
	
	</#list></#if><#if isIdColumn=0><#if onePrimaryField=1><#if primaryField??&&primaryField.column_name!="id">
	@Override
	public Long getId() {
		return ${primaryField.column_name};
	}
	</#if>
	<#else>
	@Override
	public Long getId() {
		return null;
	}
	
	</#if></#if>
}