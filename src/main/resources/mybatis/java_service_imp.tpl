package ${packageName}.contract.impl;

import org.springframework.stereotype.Repository;

<#list importPackageSet as clazz>
import ${clazz};
</#list>
import ${packageName}.contract.I${simpleBeanName}Contract;
import ${packageName}.entity.${beanName};
import ${corePackage}.contract.AbstractBaseContract;
import ${packageName}.mapper.${simpleBeanName}Mapper;

/**
 * 数据库中  ${tableBean.table_comment}[${tableName}]表 接口实现类
 * @author ${author}
 * @Date ${createDate}
 *
 */
@Repository
public class ${simpleBeanName}ContractImpl extends AbstractBaseContract<${beanName} , ${simpleBeanName}Mapper> implements I${simpleBeanName}Contract {
	
	<#if onePrimaryField=0>
	@Override
	public ${beanName} get${simpleBeanName}(<#list primaryField as field>${field.java_basic_type} ${field.column_name}<#if field_has_next>, </#if></#list>) throws Exception {
		return mapper.get${simpleBeanName}(<#list primaryField as field>${field.column_name}<#if field_has_next>, </#if></#list>);
	}
	
	<#else></#if>
}
