package com.tigerjoys.np.cg.databases.generator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tigerjoys.np.cg.databases.AbstractDataBase;
import com.tigerjoys.np.cg.databases.CodeBuilderUtils;
import com.tigerjoys.np.cg.databases.util.Tools;

public class QueryFactory {

    private static final Map<String, String> DATA_TYPE_MAP = new HashMap<String, String>();

    private static final Map<String, String> JAVA_OBJECT_TO_BASIC_TYPE = new HashMap<String, String>();

    static {
        DATA_TYPE_MAP.put("datetime", "TIMESTAMP");
        DATA_TYPE_MAP.put("int", "INTEGER");
        DATA_TYPE_MAP.put("tinytext", "LONGVARCHAR");
        DATA_TYPE_MAP.put("text", "LONGVARCHAR");
        DATA_TYPE_MAP.put("mediumtext", "LONGVARCHAR");
        DATA_TYPE_MAP.put("longtext", "LONGVARCHAR");
        DATA_TYPE_MAP.put("tinyblob", "BLOB");
        DATA_TYPE_MAP.put("blob", "BLOB");
        DATA_TYPE_MAP.put("mediumblob", "BLOB");
        DATA_TYPE_MAP.put("longblob", "BLOB");

        JAVA_OBJECT_TO_BASIC_TYPE.put("Double", "double");
        JAVA_OBJECT_TO_BASIC_TYPE.put("Fouble", "float");
        JAVA_OBJECT_TO_BASIC_TYPE.put("Integer", "int");
        JAVA_OBJECT_TO_BASIC_TYPE.put("Long", "long");
    }

    private static final String[] DATA_TYPE = new String[]{"bigint", "int", "datetime", "timestamp", "time", "smallint", "date", "varchar", "char", "tinytext", "text", "mediumtext", "longtext", "float", "double", "tinyint", "decimal", "tinyblob", "blob", "mediumblob", "longblob"};

    public static TableBean getTableBean(String table_name, AbstractDataBase database) {
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            co = database.getConnection();
            st = co.createStatement();

            String sql = "SELECT table_schema,table_name,engine,table_rows,avg_row_length,auto_increment,create_time,table_collation,table_comment from information_schema.tables WHERE table_schema='" + database.getDatabase() + "' and table_name='" + table_name + "';";
            System.err.println(sql);

            rs = st.executeQuery(sql);

            if (rs.next()) {
                TableBean bean = new TableBean();
                bean.setTable_schema(rs.getString(1));
                bean.setTable_name(rs.getString(2));
                bean.setEngine(rs.getString(3));
                bean.setTable_rows(rs.getLong(4));
                bean.setAvg_row_length(rs.getLong(5));
                bean.setAuto_increment(Tools.parseLong(rs.getString(6)));
                bean.setCreate_time(rs.getDate(7));
                bean.setTable_collation(rs.getString(8));
                bean.setTable_comment(rs.getString(9));

                return bean;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CodeBuilderUtils.closeDBA(rs, st, co);
        }

        return null;
    }

    //ormType 0mybatis , 1jpa
    @SuppressWarnings("resource")
    public static List<TableColumnBean> getColumnBeanList(String table_name, AbstractDataBase database, boolean primarySetLong , int ormType) {
        Connection co = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            co = database.getConnection();
            st = co.createStatement();

            String sql = "SELECT table_schema,table_name,column_name,ordinal_position,column_default,is_nullable,data_type,column_type,column_key,column_comment,extra from information_schema.columns WHERE table_schema='" + database.getDatabase() + "' and table_name='" + table_name + "';";
            System.err.println(sql);

            rs = st.executeQuery(sql);

            List<TableColumnBean> list = new ArrayList<TableColumnBean>();
            while (rs.next()) {
                //检查字段名是否合法
                if (!FiledKeyword.lawfulKeyword(rs.getString(3))) {
                    throw new RuntimeException("字段名必须是数字，字母和下划线，并且以字母开头");
                }

                TableColumnBean bean = new TableColumnBean();
                bean.setTable_schema(rs.getString(1));
                bean.setTable_name(rs.getString(2));
                bean.setColumn_name(rs.getString(3));
                bean.setProfile_name(camelCaseName(rs.getString(3).toLowerCase()));
                bean.setOrdinal_position(rs.getInt(4));
                bean.setColumn_default(rs.getString(5));
                bean.setIs_nullable("YES".equals(rs.getString(6).toUpperCase()) ? true : false);
                bean.setData_type(rs.getString(7));
                bean.setShow_data_type(getMyBatisJdbcType(rs.getString(7)));
                bean.setJava_type(getJavaType(rs.getString(7)));
                bean.setJava_basic_type(getJavaBasicType(bean.getJava_type()));
                bean.setColumn_type(rs.getString(8));
                bean.setLength(getColumnLength(rs.getString(8)));
                bean.setColumn_key(rs.getString(9));
                bean.setColumn_comment(Tools.isNull(rs.getString(10)) ? rs.getString(3) : rs.getString(10));
                bean.setExtra(rs.getString(11));
                bean.setAuto_increment("auto_increment".equals(bean.getExtra()));
                bean.setIslob(bean.getShow_data_type().equals("BLOB"));

                list.add(bean);
            }
            //主键是否需要设置为Long
            if (primarySetLong) {
                checkMorePRI(list);
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CodeBuilderUtils.closeDBA(rs, st, co);
        }

        return null;
    }

    public static List<TableIndexBean> getIndexList(List<TableColumnBean> columnList, String table_name, AbstractDataBase database) {
        //将数据库字段映射
        Map<String, TableColumnBean> columnMap = new HashMap<>();
        if (Tools.isNotNull(columnList)) {
            for (TableColumnBean column : columnList) {
                columnMap.put(column.getColumn_name(), column);
            }
        }

        Connection co = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            co = database.getConnection();
            st = co.createStatement();

            String sql = "SELECT table_schema,table_name,non_unique,index_name,group_concat(column_name) from information_schema.statistics WHERE table_schema='" + database.getDatabase() + "' and table_name='" + table_name + "' group by index_name;";
            System.err.println(sql);

            rs = st.executeQuery(sql);

            List<TableIndexBean> list = new ArrayList<TableIndexBean>();
            while (rs.next()) {
                TableIndexBean bean = new TableIndexBean();
                bean.setTable_schema(rs.getString(1));
                bean.setTable_name(rs.getString(2));
                bean.setUnique(rs.getInt(3) == 0 ? 1 : 0);//是否是唯一索引
                bean.setIndex_name(rs.getString(4));

                String[] indexColumnArray = rs.getString(5).split(",");
                List<TableColumnBean> columnBeanList = new ArrayList<>(indexColumnArray.length);
                for (String indexColumn : indexColumnArray) {
                    columnBeanList.add(columnMap.get(indexColumn));
                }
                bean.setColumnBeanList(columnBeanList);

                list.add(bean);
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CodeBuilderUtils.closeDBA(rs, st, co);
        }

        return null;
    }

    private static int getColumnLength(String column_type) {
        int length = 0;

        int idx_from = column_type.indexOf("(");
        if (idx_from > 0) {
            int idx_to = column_type.indexOf(")", idx_from);
            if (idx_to > 0) {
                length = Tools.parseInt(column_type.substring(idx_from + 1, idx_to));
            }
        }

        return length;
    }

    private static String getJavaType(String data_type) {
        checkDataType(data_type);

        if ("double".equals(data_type) || "float".equals(data_type)) {
            return Tools.capFirst(data_type);
        }

        if ("varchar".equals(data_type) || "tinytext".equals(data_type) || "text".equals(data_type) || "mediumtext".equals(data_type) || "longtext".equals(data_type) || "char".equals(data_type)) {
            return "String";
        } else if ("int".equals(data_type) || "tinyint".equals(data_type) || "smallint".equals(data_type)) {
            return "Integer";
        } else if ("bigint".equals(data_type)) {
            return "Long";
        } else if ("date".equals(data_type) || "datetime".equals(data_type) || "timestamp".equals(data_type)) {
            return "Date";
        } else if ("time".equals(data_type)) {
            return "Time";
        } else if ("decimal".equals(data_type)) {
            return "Double";
        } else if("tinyblob".equals(data_type) || "blob".equals(data_type) || "mediumblob".equals(data_type) || "longblob".equals(data_type)) {//mybatis应该是blob,jpa是byte[]
        	return "byte[]";
        }

        throw new RuntimeException(data_type + " no find java type");
    }

    private static String getJavaBasicType(String java_type) {
        String s = JAVA_OBJECT_TO_BASIC_TYPE.get(java_type);
        if (s == null) return java_type;
        return s;
    }

    private static String getMyBatisJdbcType(String data_type) {
        String s = DATA_TYPE_MAP.get(data_type);
        if (s == null) return data_type;
        return s;
    }

    private static void checkDataType(String data_type) {
        boolean b = false;
        for (String type : DATA_TYPE) {
            if (type.equals(data_type)) {
                b = true;
                break;
            }
        }

        if (!b) throw new RuntimeException(data_type + " no pass check");
    }

    private static void checkMorePRI(List<TableColumnBean> list) {
        List<TableColumnBean> morePriList = new ArrayList<TableColumnBean>();
        for (TableColumnBean t : list) {
            if ("PRI".equals(t.getColumn_key())) {
                morePriList.add(t);
            }
        }

        if (morePriList.size() == 1) {
            morePriList.get(0).setJava_type("Long");
            return;
        }

        if (morePriList.size() == 0) throw new RuntimeException("table is no find Primary key");

        //如果有多个PRI，则判断哪个是ID
        /*for(Iterator<TableColumnBean> it = morePriList.iterator();it.hasNext();) {
            TableColumnBean t = it.next();
			if(t.getColumn_name().equals("id")) {
				//t.setJava_type("Long");
				
				it.remove();
			}
		}
		
		for(TableColumnBean t : morePriList) {
			t.setColumn_key("MUL");
		}*/
    }

    /**
     * 转换为驼峰
     *
     * @param underscoreName
     * @return
     */
    public static String camelCaseName(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.err.println(getColumnLength("varchar(50)"));
    }

}
