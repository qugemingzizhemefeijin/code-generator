package ${packageName}.contract;

<#list importPackageSet as clazz>
import ${clazz};
</#list>
import ${packageName}.entity.${beanName};
import ${corePackage}.BaseContract;

/**
 * 数据库中  ${tableBean.table_comment}[${tableName}]表 接口类
 * @author ${author}
 * @Date ${createDate}
 *
 */
public interface I${simpleBeanName}Contract extends BaseContract<${beanName}> {
	
	<#if onePrimaryField=0>
	/**
	 * 根据主键获得对象<#list primaryField as field>
	 * @param ${field.column_name} - ${field.java_basic_type} ${field.column_comment}</#list>
	 * @return ${beanName}
	 * @throw Exception
	 *
	 */
	public ${beanName} get${simpleBeanName}(<#list primaryField as field>${field.java_basic_type} ${field.column_name}<#if field_has_next>, </#if></#list>) throws Exception;
	
	<#else></#if>
}
