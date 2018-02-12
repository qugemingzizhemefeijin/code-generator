package ${packageName}.mapper;

<#list importPackageSet as clazz>
import ${clazz};
</#list>
import ${corePackage}.provider.DefaultSqlProvider;
import ${packageName}.entity.${beanName};
import ${corePackage}.BaseMapper;
import ${corePackage}.annotation.Mapper;

/**
 * 数据库  ${tableBean.table_comment}[${tableName}]表 dao通用操作接口实现类
 * @author ${author}
 * @Date ${createDate}
 *
 */
@Producer(entityType=${simpleBeanName}Entity.class,providerType=DefaultSqlProvider.class<#if autoIncrement=0>,increment=false</#if>)
@Mapper
public interface ${simpleBeanName}Mapper extends BaseMapper<${simpleBeanName}Entity> {
    
    <#if onePrimaryField=0>
	/**
	 * 根据主键获得对象<#list primaryField as field>
	 * @param ${field.column_name} - ${field.java_basic_type} ${field.column_comment}</#list>
	 * return ${beanName}
	 * @throw Exception
	 *
	*/
	@Select("SELECT * FROM ${tableName} WHERE <#list primaryField as field>${field.column_name} = ${"#{"}${field.column_name},jdbcType=${field.show_data_type?upper_case}${"}"}<#if field_has_next> AND </#if></#list>")
	@ProducerResult
	public ${beanName} get${simpleBeanName}(<#list primaryField as field>@Param("${field.column_name}")${field.java_basic_type} ${field.column_name}<#if field_has_next>, </#if></#list>);
	
	<#else></#if>
}