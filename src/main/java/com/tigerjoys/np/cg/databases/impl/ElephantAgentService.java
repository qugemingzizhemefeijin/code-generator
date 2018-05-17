package com.tigerjoys.np.cg.databases.impl;

import com.tigerjoys.np.cg.databases.AbstractDataBase;

/**
 * 大象数据库服务
 *
 * @author chengang
 */
public class ElephantAgentService extends AbstractDataBase {

    public ElephantAgentService() {
        super("ff","aa","yyyy","127.0.0.1",3306);
    }

    @Override
    public String getPackageName() {
        return "com.tt.ee.agent.inter";
    }
    
    public String getBaseEntityClassName() {
    	return "com.tigerjoys.elephant.agent.basic.BaseEntity";
    }

}

