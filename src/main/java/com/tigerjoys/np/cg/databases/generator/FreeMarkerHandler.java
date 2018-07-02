package com.tigerjoys.np.cg.databases.generator;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.tigerjoys.np.cg.databases.AbstractDataBase;
import com.tigerjoys.np.cg.databases.CodeBuilderUtils;
import com.tigerjoys.np.cg.databases.DataBaseFactory;
import com.tigerjoys.np.cg.databases.util.ECharset;
import com.tigerjoys.np.cg.databases.util.FileUtil;
import com.tigerjoys.np.cg.databases.util.Tools;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

public class FreeMarkerHandler {

    private static final String FREEMARKER_VERSION = "2.3.21";

    private static final String ENCODING = ECharset.UTF_8.getName();

    private static final String TEMPLATE_DIRECTORY = "mybatis";

    private static final String corePackage = "com.tigerjoys.nbs.mybatis.core";

    private static Configuration freemarkerCfg = null;

    private static List<String> mybatisAliasesConfigList = new ArrayList<>();
    private static List<String> mybatisMappersConfigList = new ArrayList<>();

    static {
        freemarkerCfg = new Configuration(new Version(FREEMARKER_VERSION));
        try {
            URL rootURL = CodeBuilderUtils.class.getResource("/");

            freemarkerCfg.setDirectoryForTemplateLoading(new File(JPAFreeMarkerHandler.class.getClassLoader().getResource(TEMPLATE_DIRECTORY).toURI()));
            freemarkerCfg.setEncoding(Locale.getDefault(), ENCODING);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static <T extends AbstractDataBase> boolean makeFiles(String table_name, String author, String directory, Class<T> clazz) {
        if (Tools.isNull(directory)) {
            throw new RuntimeException("请填写文件保存的目录名称");
        }
        if (Tools.isNull(table_name)) {
            throw new NullPointerException("table_name is null");
        }
        if (clazz == null) {
            throw new ArithmeticException("clazz can not be null!");
        }

        AbstractDataBase database = null;
        try {
            database = DataBaseFactory.getInstance().getDatabase(clazz);
        } catch (InstantiationException | IllegalAccessException e1) {
            e1.printStackTrace();
            return false;
        }

        List<TableColumnBean> columnList = QueryFactory.getColumnBeanList(table_name, database, true , 0);
        if (Tools.isNull(columnList)) {
            throw new NoSuchElementException("没有查找到指定数据库的字段集合");
        }

        TableBean tableBean = QueryFactory.getTableBean(table_name, database);
        if (tableBean == null) throw new NoSuchElementException("没有查找到指定数据库的表信息");

        List<TableColumnBean> primaryColumnList = findPrimaryColumn(columnList);

        String packageName = database.getPackageName();

        //表名转换成Bean名称
        String beanName = getTableToJavaName(table_name);

        //传递Table信息
        TableInfo info = new TableInfo();
        info.setAuthor(author);
        info.setBeanName(beanName);
        info.setColumnList(columnList);
        info.setDirectory(directory);
        info.setPackageName(packageName);
        info.setPrimaryColumnList(primaryColumnList);
        info.setTable_name(table_name);
        info.setTableBean(tableBean);
        info.setBaseEntityClassName(database.getBaseEntityClassName());

        makeJavaBean(info);
        makeContract(info);
        makeContractImpl(info);
        makeMapper(info);
        makeMapperXml(info);

        String xmlFileName = tableBean.getTable_name().replaceAll("^[a-zA-Z]{1}_", "").replace("_", "-") + "-mapper.xml";

        mybatisAliasesConfigList.add("<typeAlias type=\"" + packageName + ".entity." + beanName + "Entity\" alias=\"" + Tools.uncapFirst(beanName) + "\" />");
        mybatisMappersConfigList.add("<mapper resource=\"mybatis/" + (Tools.isNotNull(database.getXmlFolder()) ? (database.getXmlFolder() + "/") : "") + xmlFileName + "\"/>");
        System.err.println("============================================================================");

        return true;
    }

    private static boolean makeMapperXml(TableInfo info) {
        System.err.println("生成 Mapper XML");
        long startTime = System.currentTimeMillis();

        Template template = null;
        try {
            template = freemarkerCfg.getTemplate("/mapper_xml.tpl");
            template.setEncoding(ENCODING);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("beanName", info.getBeanName() + "Entity");
        dataModel.put("simpleBeanName", info.getBeanName());
        dataModel.put("CopyrightYear", Calendar.getInstance().get(Calendar.YEAR));
        dataModel.put("author", info.getAuthor());
        dataModel.put("fieldList", info.getColumnList());
        dataModel.put("createDate", Tools.getDateTime());
        dataModel.put("tableName", info.getTable_name());
        dataModel.put("tableBean", info.getTableBean());
        dataModel.put("packageName", info.getPackageName());
        dataModel.put("corePackage", corePackage);

        //判断是否是多个主键
        boolean onePrimaryField = true;
        if (info.getPrimaryColumnList().size() > 1) {
            onePrimaryField = false;
        }
        if (!onePrimaryField) {
            dataModel.put("primaryField", info.getPrimaryColumnList());
        }
        dataModel.put("onePrimaryField", onePrimaryField ? 1 : 0);

        String targetFilePath = System.getProperty("user.home") + "/" + info.getDirectory() + "/" + info.getTable_name().replaceAll("^[a-zA-Z]{1}_", "").replace("_", "-") + "-mapper.xml";
        boolean b = makeFile(template, targetFilePath, dataModel);

        long endTime = System.currentTimeMillis();
        System.err.println("生成完毕，路径：" + targetFilePath + "，总耗时：" + (endTime - startTime) + "毫秒.");

        return b;
    }

    private static boolean makeMapper(TableInfo info) {
        System.err.println("生成 Mapper");
        long startTime = System.currentTimeMillis();

        Template template = null;
        try {
            template = freemarkerCfg.getTemplate("/java_mapper.tpl");
            template.setEncoding(ENCODING);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        List<TableColumnBean> primaryList = info.getPrimaryColumnList();
        //判断是否是多个主键
        boolean onePrimaryField = true;
        if (primaryList.size() > 1) {
            onePrimaryField = false;
        }

        Set<String> importPackageSet = new LinkedHashSet<String>();
        if (checkToDate(info.getPrimaryColumnList())) {
            importPackageSet.add("java.util.Date");
        }
        importPackageSet.add("org.apache.ibatis.annotations.Producer");
        if (!onePrimaryField) {
            importPackageSet.add("org.apache.ibatis.annotations.Param");
            importPackageSet.add("org.apache.ibatis.annotations.Select");
            importPackageSet.add("org.apache.ibatis.annotations.ProducerResult");
        }

        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("importPackageSet", importPackageSet);
        dataModel.put("beanName", info.getBeanName() + "Entity");
        dataModel.put("simpleBeanName", info.getBeanName());
        dataModel.put("CopyrightYear", Calendar.getInstance().get(Calendar.YEAR));
        dataModel.put("author", info.getAuthor());
        dataModel.put("createDate", Tools.getDateTime());
        dataModel.put("tableName", info.getTable_name());
        dataModel.put("tableBean", info.getTableBean());
        dataModel.put("packageName", info.getPackageName());
        dataModel.put("corePackage", corePackage);
        if (!onePrimaryField) {
            dataModel.put("primaryField", primaryList);
        }
        dataModel.put("onePrimaryField", onePrimaryField ? 1 : 0);
        //判断主键是否是自增的
        dataModel.put("autoIncrement", primaryList.get(0).isAuto_increment() ? 1 : 0);

        String targetFilePath = System.getProperty("user.home") + "/" + info.getDirectory() + "/mapper/" + info.getBeanName() + "Mapper.java";
        boolean b = makeFile(template, targetFilePath, dataModel);

        long endTime = System.currentTimeMillis();
        System.err.println("生成完毕，路径：" + targetFilePath + "，总耗时：" + (endTime - startTime) + "毫秒.");

        return b;
    }

    private static boolean makeContractImpl(TableInfo info) {
        System.err.println("生成 ContractImpl JAVA");
        long startTime = System.currentTimeMillis();

        Template template = null;
        try {
            template = freemarkerCfg.getTemplate("/java_service_imp.tpl");
            template.setEncoding(ENCODING);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        Set<String> importPackageSet = new LinkedHashSet<String>();
        if (checkToDate(info.getPrimaryColumnList())) {
            importPackageSet.add("java.util.Date");
        }

        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("importPackageSet", importPackageSet);
        dataModel.put("beanName", info.getBeanName() + "Entity");
        dataModel.put("simpleBeanName", info.getBeanName());
        dataModel.put("CopyrightYear", Calendar.getInstance().get(Calendar.YEAR));
        dataModel.put("author", info.getAuthor());
        dataModel.put("createDate", Tools.getDateTime());
        dataModel.put("tableName", info.getTable_name());
        dataModel.put("tableBean", info.getTableBean());
        dataModel.put("packageName", info.getPackageName());
        dataModel.put("corePackage", corePackage);

        //判断是否是多个主键
        boolean onePrimaryField = true;
        if (info.getPrimaryColumnList().size() > 1) {
            onePrimaryField = false;
        }
        if (!onePrimaryField) {
            dataModel.put("primaryField", info.getPrimaryColumnList());
        }
        dataModel.put("onePrimaryField", onePrimaryField ? 1 : 0);

        String targetFilePath = System.getProperty("user.home") + "/" + info.getDirectory() + "/contract/impl/" + info.getBeanName() + "ContractImpl.java";
        boolean b = makeFile(template, targetFilePath, dataModel);

        long endTime = System.currentTimeMillis();
        System.err.println("生成完毕，路径：" + targetFilePath + "，总耗时：" + (endTime - startTime) + "毫秒.");

        return b;
    }

    private static boolean makeContract(TableInfo info) {
        System.err.println("生成 Contract JAVA");
        long startTime = System.currentTimeMillis();

        Template template = null;
        try {
            template = freemarkerCfg.getTemplate("/java_service.tpl");
            template.setEncoding(ENCODING);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        Set<String> importPackageSet = new LinkedHashSet<String>();
        if (checkToDate(info.getPrimaryColumnList())) {
            importPackageSet.add("java.util.Date");
        }

        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("importPackageSet", importPackageSet);
        dataModel.put("beanName", info.getBeanName() + "Entity");
        dataModel.put("simpleBeanName", info.getBeanName());
        dataModel.put("CopyrightYear", Calendar.getInstance().get(Calendar.YEAR));
        dataModel.put("author", info.getAuthor());
        dataModel.put("createDate", Tools.getDateTime());
        dataModel.put("tableName", info.getTable_name());
        dataModel.put("tableBean", info.getTableBean());
        dataModel.put("packageName", info.getPackageName());
        dataModel.put("corePackage", corePackage);

        //判断是否是多个主键
        boolean onePrimaryField = true;
        if (info.getPrimaryColumnList().size() > 1) {
            onePrimaryField = false;
        }
        if (!onePrimaryField) {
            dataModel.put("primaryField", info.getPrimaryColumnList());
        }
        dataModel.put("onePrimaryField", onePrimaryField ? 1 : 0);

        String targetFilePath = System.getProperty("user.home") + "/" + info.getDirectory() + "/contract/I" + info.getBeanName() + "Contract.java";
        boolean b = makeFile(template, targetFilePath, dataModel);

        long endTime = System.currentTimeMillis();
        System.err.println("生成完毕，路径：" + targetFilePath + "，总耗时：" + (endTime - startTime) + "毫秒.");

        return b;
    }

    private static boolean makeJavaBean(TableInfo info) {
        System.err.println("生成 JAVA BEAN");
        long startTime = System.currentTimeMillis();

        Template template = null;
        try {
            template = freemarkerCfg.getTemplate("/java_bean.tpl");
            template.setEncoding(ENCODING);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        Set<String> importPackageSet = new LinkedHashSet<String>();
        if (checkToDate(info.getColumnList())) {
            importPackageSet.add("java.util.Date");
        }
        if (checkToTime(info.getColumnList())) {
            importPackageSet.add("java.sql.Time");
        }
        importPackageSet.add("org.apache.ibatis.type.JdbcType");
        importPackageSet.add("org.apache.ibatis.annotations.Column");
        importPackageSet.add("org.apache.ibatis.annotations.Id");
        importPackageSet.add("org.apache.ibatis.annotations.Table");

        //判断是否是多个主键
        boolean onePrimaryField = true;
        if (info.getPrimaryColumnList().size() > 1) {
            onePrimaryField = false;
        }
        //是否有叫ID的一个属性
        boolean isIdColumn = findIdColumn(info.getColumnList());

        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("importPackageSet", importPackageSet);
        dataModel.put("tableName", info.getTable_name());
        dataModel.put("simpleBeanName", info.getBeanName());
        dataModel.put("beanName", info.getBeanName() + "Entity");
        dataModel.put("CopyrightYear", Calendar.getInstance().get(Calendar.YEAR));
        dataModel.put("author", info.getAuthor());
        dataModel.put("createDate", Tools.getDateTime());
        dataModel.put("fieldList", info.getColumnList());
        dataModel.put("tableBean", info.getTableBean());
        if (onePrimaryField) {
            dataModel.put("primaryField", info.getPrimaryColumnList().get(0));
        } else {
            dataModel.put("primaryField", info.getPrimaryColumnList());
        }
        dataModel.put("onePrimaryField", onePrimaryField ? 1 : 0);
        dataModel.put("isIdColumn", isIdColumn ? 1 : 0);
        dataModel.put("packageName", info.getPackageName());
        dataModel.put("corePackage", corePackage);
        dataModel.put("entityClassName", info.getBaseEntityClassName());

        String targetFilePath = System.getProperty("user.home") + "/" + info.getDirectory() + "/entity/" + info.getBeanName() + "Entity.java";
        boolean b = makeFile(template, targetFilePath, dataModel);

        long endTime = System.currentTimeMillis();
        System.err.println("生成完毕，路径：" + targetFilePath + "，总耗时：" + (endTime - startTime) + "毫秒.");

        return b;
    }

    private static String getTableToJavaName(String table_name) {
        String name = table_name;

        if (name.matches("^[a-zA-Z]_.*")) {
            name = name.substring(2);
        }

        String[] a = name.split("_");

        String str = "";
        for (String s : a) {
            str += Tools.capFirst(s);
        }

        return str;
    }

    private static boolean checkToDate(List<TableColumnBean> list) {
        boolean b = false;
        String[] checkDateType = new String[]{"date", "datetime", "timestamp"};
        for (TableColumnBean t : list) {
            if (checkData(t.getData_type(), checkDateType)) {
                b = true;
                break;
            }
        }

        return b;
    }

    private static boolean checkToTime(List<TableColumnBean> list) {
        boolean b = false;
        String[] checkDateType = new String[]{"time"};
        for (TableColumnBean t : list) {
            if (checkData(t.getData_type(), checkDateType)) {
                b = true;
                break;
            }
        }

        return b;
    }

    private static boolean checkData(String data_type, String[] dateTypeArray) {
        for (String type : dateTypeArray) {
            if (type.equals(data_type)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 生成一个文件
     *
     * @param template       - FreeMarker模板对象
     * @param targetFilePath - 输入到目标文件
     * @param dataModel      - 传送给FreeMarker的数据
     * @return True or False
     */
    private static boolean makeFile(Template template, String targetFilePath, Object dataModel) {
        StringWriter writer = null;
        try {
            writer = new StringWriter();
            template.process(dataModel, writer);
            writer.flush();

            FileUtils.writeStringToFile(new File(targetFilePath), writer.toString(), ENCODING);
            return true;
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private static List<TableColumnBean> findPrimaryColumn(List<TableColumnBean> list) {
        List<TableColumnBean> primaryList = new ArrayList<>();
        for (TableColumnBean t : list) {
            if (t.getColumn_key().equals("PRI")) {
                primaryList.add(t);
            }
        }

        if (Tools.isNull(primaryList)) throw new RuntimeException("table is no find Primary key");
        return primaryList;
    }

    /**
     * 查找是否有ID这个字段的列，如果有此值，则不需要再重写getId了
     *
     * @param list - List<TableColumnBean>
     * @return boolean
     */
    private static boolean findIdColumn(List<TableColumnBean> list) {
        boolean b = false;
        for (TableColumnBean t : list) {
            if (t.getColumn_name().equals("id")) {
                b = true;
            }
        }

        return b;
    }

    /**
     * 删除文件
     *
     * @param path - String
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) FileUtil.deleteDir(file);
    }

    public static void printXmlConfig() {
        for (String s : mybatisAliasesConfigList) {
            System.err.println(s);
        }
        for (String s : mybatisMappersConfigList) {
            System.err.println(s);
        }
    }

}
