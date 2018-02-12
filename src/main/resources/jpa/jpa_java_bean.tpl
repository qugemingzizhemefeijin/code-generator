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
@Entity
@Table(name="${tableName}")
<#if onePrimaryField=0>
<#if primaryField??&&primaryField?size gt 1>
@IdClass(${simpleBeanName}Key.class)
</#if>
<#else>
<#if !primaryField.auto_increment>
@GenericGenerator(name="${primaryField.column_name}" , strategy="assigned")
</#if>
</#if>
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
	@Id
	<#if onePrimaryField=1>
	@GeneratedValue(strategy=GenerationType.<#if field.auto_increment>IDENTITY<#else>AUTO,generator="${field.column_name}"</#if>)
	</#if>
	</#if>
	@Column(name="${field.column_name}",nullable=${field.is_nullable?string('true','false')}<#if field.length gt 0>,length=${field.length}</#if>)
	private ${field.java_type} ${field.profile_name};
	</#list></#if>
	
	<#if fieldList??&&fieldList?size gt 0>
	<#list fieldList as field>
	public ${field.java_type} get${field.profile_name?cap_first}() {
		return ${field.profile_name};
	}

	public void set${field.profile_name?cap_first}(${field.java_type} ${field.profile_name}) {
		this.${field.profile_name} = ${field.profile_name};
	}
	
	</#list></#if><#if onePrimaryField=1>
	@Override
	public Long primaryKey() {
		<#if primaryField.java_type=="Integer">
		return ${primaryField.profile_name}.longValue();
		<#else>
		return ${primaryField.profile_name};
		</#if>
	}
	
	<#else>
	@Override
	public Long primaryKey() {
		throw new RuntimeException("composite primary key");
	}
	
	</#if>
}