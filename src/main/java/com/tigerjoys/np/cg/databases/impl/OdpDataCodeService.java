package com.tigerjoys.np.cg.databases.impl;

import com.tigerjoys.np.cg.databases.AbstractDataBase;

public class OdpDataCodeService extends AbstractDataBase {

	public OdpDataCodeService() {
		super("ssp","ssp","ssp","127.0.0.1",3306);
	}

	@Override
	public String getPackageName() {
		return "com.zxrtb.odp.common.inter";
	}

	@Override
	public String getXmlFolder() {
		return "service";
	}

}
