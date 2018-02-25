package com.tigerjoys.np.cg.test;

import com.tigerjoys.np.cg.databases.generator.FreeMarkerHandler;
import com.tigerjoys.np.cg.databases.generator.JPAFreeMarkerHandler;
import com.tigerjoys.np.cg.databases.impl.ElephantAgentService;

public class JPAMain {

	public static void main(String[] args) {
		String directory = getDirName();
		FreeMarkerHandler.deleteFile(System.getProperty("user.home")+"\\"+directory);

		String[] ss = new String[]{"t_error_login_log"};
		for(String table : ss) {
			JPAFreeMarkerHandler.makeFiles(table, "chengang", directory ,ElephantAgentService.class);
		}
	}

	/**
	 * 用户文件保存的相对路径
	 * @return String
	 */
	private static String getDirName(){
		String osname = System.getProperty("os.name").toLowerCase();
		if(osname.indexOf("mac") > -1) {
			return "tmp/test";
		}
		return "test";
	}

	
}
