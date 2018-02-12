package com.tigerjoys.np.cg.test;

import com.tigerjoys.np.cg.databases.generator.FreeMarkerHandler;
import com.tigerjoys.np.cg.databases.impl.SharkMiaiService;

/**
 *
 */
public class MybatisMain {

    public static void main(String[] args) throws Exception {
        String directory = getDirName();
        FreeMarkerHandler.deleteFile(System.getProperty("user.home")+"\\"+directory);

        String[] ss = new String[]{"t_talent_level"};
        for(String table : ss) {
            FreeMarkerHandler.makeFiles(table, "chengang", directory ,SharkMiaiService.class);
        }
        FreeMarkerHandler.printXmlConfig();
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
