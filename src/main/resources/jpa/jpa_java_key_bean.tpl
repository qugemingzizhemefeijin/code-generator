package ${packageName}.entity;

import java.io.Serializable;

/**
 * 数据库中  ${tableBean.table_comment}[${tableName}] 表对应的主键类
 * @author ${author}
 * @Date ${createDate}
 *
 */
public class ${simpleBeanName}Key implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	<#if fieldList??&&fieldList?size gt 0>
	<#list fieldList as field>
	
	/**
	 * ${field.column_comment}
	 */
	private ${field.java_type} ${field.profile_name};
	</#list></#if>
	
	public ${simpleBeanName}Key() {
		
	}
	
	public ${simpleBeanName}Key(<#list fieldList as field>${field.java_basic_type} ${field.profile_name}<#if field_has_next>, </#if></#list>) {
		<#list fieldList as field>
		this.${field.profile_name} = ${field.profile_name};
		</#list>
	}

	<#if fieldList??&&fieldList?size gt 0>
	<#list fieldList as field>
	public ${field.java_type} get${field.profile_name?cap_first}() {
		return ${field.profile_name};
	}

	public void set${field.profile_name?cap_first}(${field.java_type} ${field.profile_name}) {
		this.${field.profile_name} = ${field.profile_name};
	}
	
	</#list></#if>
    @Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
        <#if fieldList??&&fieldList?size gt 0>
        <#list fieldList as field>
		result = PRIME * result + ((${field.profile_name} == null) ? 0 : ${field.profile_name}.hashCode());
        </#list>
        </#if>
		return result;
    }
    
    @Override
    public boolean equals(Object obj){
    	if(obj == null){
            return false;
        } else if(this == obj){
    		return true;
    	} else if(getClass() != obj.getClass()) {
    		return false;
    	}
    	final ${simpleBeanName}Key other = (${simpleBeanName}Key)obj;
    	<#if fieldList??&&fieldList?size gt 0>
    	<#list fieldList as field>
    	
    	if(${field.profile_name} == null){
    		if(other.${field.profile_name} != null){
    			return false;
    		}
    	} else if(!${field.profile_name}.equals(other.${field.profile_name})){
    		return false;
    	}
    	</#list>
        </#if>
    	
    	return true;
    }
    
}
