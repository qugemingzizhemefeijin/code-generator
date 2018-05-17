package com.tigerjoys.np.cg.databases.impl;

import com.tigerjoys.np.cg.databases.AbstractDataBase;

/**
 * 蜜爱数据库类
 *
 * @author chengang
 */
public class SharkMiaiService extends AbstractDataBase {

    public SharkMiaiService() {
        super("yyyy","aaaa","bbbb","127.0.0.1",3306);
    }

    @Override
    public String getPackageName() {
        return "com.tt.ss.mm.inter";
    }
    
    public String getBaseEntityClassName() {
    	return "com.tigerjoys.nbs.mybatis.core.BaseEntity";
    }

    @Override
    public String getXmlFolder() {
        return "service";
    }

}
