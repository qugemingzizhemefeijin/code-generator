package com.tigerjoys.np.cg.databases.impl;

import com.tigerjoys.np.cg.databases.AbstractDataBase;

public class OdpDataCodeService extends AbstractDataBase {

	public OdpDataCodeService() {
		super("ssp","sss","sss","127.0.0.1",3306);
	}

	@Override
	public String getPackageName() {
		return "com.zxrtb.odp.common.inter";
	}
	
	public String getBaseEntityClassName() {
    	return "com.zxrtb.odp.common.inter.BaseEntity";
    }

}
