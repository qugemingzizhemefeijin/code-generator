package ${packageName}.dao;

<#if importPackageSet??&&importPackageSet?size gt 0>
<#list importPackageSet as clazz>
import ${clazz};
</#list>
</#if>
import ${packageName?substring(0,packageName?last_index_of('.'))}.extension.jpa.IJPACustomRepository;
import ${packageName}.entity.${beanName};
<#if onePrimaryField=0>
import ${packageName}.entity.${simpleBeanName}Key;
</#if>

/**
 * 数据库  ${tableBean.table_comment}[${tableName}]表 DAO操作类
 * @author ${author}
 * @Date ${createDate}
 *
 */
public interface I${simpleBeanName}Repository extends IJPACustomRepository<${beanName}, <#if onePrimaryField=1>${primaryField.java_type}<#else>${simpleBeanName}Key</#if>> {
	
	<#if onePrimaryField=0>
	/**
	 * 根据主键获得对象<#list primaryField as field>
	 * @param ${field.profile_name} - ${field.java_basic_type} ${field.profile_name}</#list>
	 * @return ${beanName}
	 * @throw Exception
	 *
	 */
	<#if primaryField??&&primaryField?size lt 3>
	public ${beanName} findBy<#list primaryField as field>${field.profile_name?cap_first}<#if field_has_next>And</#if></#list>(<#list primaryField as field>${field.java_basic_type} ${field.profile_name}<#if field_has_next>, </#if></#list>);
	<#else>
	@Query("select p from ${beanName} p where <#list primaryField as field>p.${field.profile_name}= ?<#if field_has_next> and </#if></#list>")
	public ${beanName} find${simpleBeanName}(<#list primaryField as field>${field.java_basic_type} ${field.profile_name}<#if field_has_next>, </#if></#list>);
	</#if>
	
	</#if>
	<#if uniqueFieldList??&&uniqueFieldList?size gt 0>
	<#list uniqueFieldList as uniqueField>
	<#assign columnFieldList=uniqueField.columnBeanList />
	/**
	 * 根据 <#list columnFieldList as field>${field.column_comment}<#if field_has_next>, </#if></#list> 获得对象<#list columnFieldList as field>
	 * @param ${field.profile_name} - ${field.java_basic_type} ${field.column_comment}</#list>
	 * @return ${beanName}
	 * @throw Exception
	 *
	 */
	<#if columnFieldList??&&columnFieldList?size lt 3>
	public ${beanName} findBy<#list columnFieldList as field>${field.profile_name?cap_first}<#if field_has_next>And</#if></#list>(<#list columnFieldList as field>${field.java_basic_type} ${field.profile_name}<#if field_has_next>, </#if></#list>);
	<#else>
	@Query("select p from ${beanName} p where <#list columnFieldList as field>p.${field.profile_name}= ?<#if field_has_next> and </#if></#list>")
	public ${beanName} find${simpleBeanName}(<#list columnFieldList as field>${field.java_basic_type} ${field.profile_name}<#if field_has_next>, </#if></#list>);
	</#if>
	
	</#list>
	</#if>
}
