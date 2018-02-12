package com.tigerjoys.np.cg.databases.generator;

import com.tigerjoys.np.cg.databases.AbstractDataBase;
import com.tigerjoys.np.cg.databases.DataBaseFactory;
import com.tigerjoys.np.cg.databases.util.ECharset;
import com.tigerjoys.np.cg.databases.util.FileUtil;
import com.tigerjoys.np.cg.databases.util.Tools;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.FileSystems;
import java.util.*;

public class JPAFreeMarkerHandler {

    private static final String FREEMARKER_VERSION = "2.3.21";

    private static final String ENCODING = ECharset.UTF_8.getName();

    private static final String TEMPLATE_DIRECTORY = "/out/production/code-generator_main/";

    private static final String corePackage = "com.tigerjoys.elephant.agent.basic";

    private static Configuration freemarkerCfg = null;

    static {
        freemarkerCfg = new Configuration(new Version(FREEMARKER_VERSION));
        try {
            freemarkerCfg.setDirectoryForTemplateLoading(new File(FileSystems.getDefault().getPath("").toAbsolutePath() + TEMPLATE_DIRECTORY));
            freemarkerCfg.setEncoding(Locale.getDefault(), ENCODING);
            freemarkerCfg.setNumberFormat("#");
        } catch (IOException e) {
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

        List<TableColumnBean> columnList = QueryFactory.getColumnBeanList(table_name, database, false);
        if (Tools.isNull(columnList)) {
            throw new NoSuchElementException("没有查找到指定数据库的字段集合");
        }

        TableBean tableBean = QueryFactory.getTableBean(table_name, database);
        if (tableBean == null) throw new NoSuchElementException("没有查找到指定数据库的表信息");

        List<TableColumnBean> primaryColumnList = findPrimaryColumn(columnList);
        //索引列表
        List<TableIndexBean> indexList = QueryFactory.getIndexList(columnList, table_name, database);
        //唯一索引列表
        List<TableIndexBean> uniqueIndexlist = findUniqueIndex(indexList);

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
        info.setIndexList(indexList);
        info.setIndexUniqueList(uniqueIndexlist);

        makeJavaBean(info);
        makeRepository(info);

        System.err.println("============================================================================");

        return true;
    }

    private static boolean makeRepository(TableInfo info) {
        System.err.println("生成 Contract JAVA");
        long startTime = System.currentTimeMillis();

        Template template = null;
        try {
            template = freemarkerCfg.getTemplate("jpa/jpa_repository.tpl");
            template.setEncoding(ENCODING);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        Set<String> importPackageSet = new LinkedHashSet<String>();
        //importPackageSet.add("org.springframework.data.jpa.repository.JpaRepository");
        //importPackageSet.add("org.springframework.data.jpa.repository.JpaSpecificationExecutor");

        if (checkToDate(info.getPrimaryColumnList())) {
            importPackageSet.add("java.util.Date");
        }
        //多主键
        if (info.getPrimaryColumnList().size() > 2) {
            importPackageSet.add("org.springframework.data.jpa.repository.Query");
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
        } else {
            dataModel.put("primaryField", info.getPrimaryColumnList().get(0));
        }
        dataModel.put("onePrimaryField", onePrimaryField ? 1 : 0);

        //唯一索引列表
        dataModel.put("uniqueFieldList", info.getIndexUniqueList());

        String targetFilePath = System.getProperty("user.home") + "/" + info.getDirectory() + "/dao/I" + info.getBeanName() + "Repository.java";
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
            template = freemarkerCfg.getTemplate("jpa/jpa_java_bean.tpl");
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

        //判断是否是多个主键
        boolean onePrimaryField = true;
        if (info.getPrimaryColumnList().size() > 1) {
            onePrimaryField = false;
        }

        importPackageSet.add("javax.persistence.Column");
        importPackageSet.add("javax.persistence.Entity");
        if (onePrimaryField) {
            importPackageSet.add("javax.persistence.GeneratedValue");
            importPackageSet.add("javax.persistence.GenerationType");

            if (!info.getPrimaryColumnList().get(0).isAuto_increment()) {
                importPackageSet.add("org.hibernate.annotations.GenericGenerator");
            }
        } else {
            importPackageSet.add("javax.persistence.IdClass");
        }
        importPackageSet.add("javax.persistence.Id");
        importPackageSet.add("javax.persistence.Table");

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

        if (!onePrimaryField) {
            makeJavaPrimaryKey(info);
        }

        String targetFilePath = System.getProperty("user.home") + "/" + info.getDirectory() + "/entity/" + info.getBeanName() + "Entity.java";
        boolean b = makeFile(template, targetFilePath, dataModel);

        long endTime = System.currentTimeMillis();
        System.err.println("生成完毕，路径：" + targetFilePath + "，总耗时：" + (endTime - startTime) + "毫秒.");

        return b;
    }

    private static boolean makeJavaPrimaryKey(TableInfo info) {
        System.err.println("生成 联合主键Bean");
        long startTime = System.currentTimeMillis();

        Template template = null;
        try {
            template = freemarkerCfg.getTemplate("jpa/jpa_java_key_bean.tpl");
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

        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("importPackageSet", importPackageSet);
        dataModel.put("tableName", info.getTable_name());
        dataModel.put("simpleBeanName", info.getBeanName());
        dataModel.put("beanName", info.getBeanName() + "Entity");
        dataModel.put("CopyrightYear", Calendar.getInstance().get(Calendar.YEAR));
        dataModel.put("author", info.getAuthor());
        dataModel.put("createDate", Tools.getDateTime());
        dataModel.put("fieldList", info.getPrimaryColumnList());
        dataModel.put("tableBean", info.getTableBean());
        dataModel.put("packageName", info.getPackageName());
        dataModel.put("corePackage", corePackage);

        String targetFilePath = System.getProperty("user.home") + "/" + info.getDirectory() + "/entity/" + info.getBeanName() + "Key.java";
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

    private static List<TableIndexBean> findUniqueIndex(List<TableIndexBean> list) {
        List<TableIndexBean> uniqueList = new ArrayList<>();
        for (TableIndexBean t : list) {
            if (t.getUnique() == 1 && !t.getIndex_name().equals("PRIMARY")) {
                uniqueList.add(t);
            }
        }

        return uniqueList;
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

}
