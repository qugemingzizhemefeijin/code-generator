package com.tigerjoys.np.cg.databases.generator;

import java.util.HashSet;
import java.util.Set;

public final class FiledKeyword {
	
	private static final Set<String> words = new HashSet<>();
	
	static {
		words.add("ACCESSIBLE");
		words.add("ADD");
		words.add("ALL");
		words.add("ALTER");
		words.add("ANALYZE");
		words.add("AND");
		words.add("AS");
		words.add("ASC");
		words.add("ASENSITIVE");
		words.add("BEFORE");
		words.add("BETWEEN");
		words.add("BIGINT");
		words.add("BINARY");
		words.add("BLOB");
		words.add("BOTH");
		words.add("BY");
		words.add("CALL");
		words.add("CASCADE");
		words.add("CASE");
		words.add("CHANGE");
		words.add("CHAR");
		words.add("CHARACTER");
		words.add("CHECK");
		words.add("COLLATE");
		words.add("COLUMN");
		words.add("CONDITION");
		words.add("CONSTRAINT");
		words.add("CONTINUE");
		words.add("CONVERT");
		words.add("CREATE");
		words.add("CROSS");
		words.add("CURRENT_DATE");
		words.add("CURRENT_TIME");
		words.add("CURRENT_TIMESTAMP");
		words.add("CURRENT_USER");
		words.add("CURSOR");
		words.add("DATABASE");
		words.add("DATABASES");
		words.add("DAY_HOUR");
		words.add("DAY_MICROSECOND");
		words.add("DAY_MINUTE");
		words.add("DAY_SECOND");
		words.add("DEC");
		words.add("DECIMAL");
		words.add("DECLARE");
		words.add("DEFAULT");
		words.add("DELAYED");
		words.add("DELETE");
		words.add("DESC");
		words.add("DESCRIBE");
		words.add("DETERMINISTIC");
		words.add("DISTINCT");
		words.add("DISTINCTROW");
		words.add("DIV");
		words.add("DOUBLE");
		words.add("DROP");
		words.add("DUAL");
		words.add("EACH");
		words.add("ELSE");
		words.add("ELSEIF");
		words.add("ENCLOSED");
		words.add("ESCAPED");
		words.add("EXISTS");
		words.add("EXIT");
		words.add("EXPLAIN");
		words.add("FALSE");
		words.add("FETCH");
		words.add("FLOAT");
		words.add("FLOAT4");
		words.add("FLOAT8");
		words.add("FOR");
		words.add("FORCE");
		words.add("FOREIGN");
		words.add("FROM");
		words.add("FULLTEXT");
		words.add("GET");
		words.add("GRANT");
		words.add("GROUP");
		words.add("HAVING");
		words.add("HIGH_PRIORITY");
		words.add("HOUR_MICROSECOND");
		words.add("HOUR_MINUTE");
		words.add("HOUR_SECOND");
		words.add("IF");
		words.add("IGNORE");
		words.add("IN");
		words.add("INDEX");
		words.add("INFILE");
		words.add("INNER");
		words.add("INOUT");
		words.add("INSENSITIVE");
		words.add("INSERT");
		words.add("INT");
		words.add("INT1");
		words.add("INT2");
		words.add("INT3");
		words.add("INT4");
		words.add("INT8");
		words.add("INTEGER");
		words.add("INTERVAL");
		words.add("INTO");
		words.add("IO_AFTER_GTIDS");
		words.add("IO_BEFORE_GTIDS");
		words.add("IS");
		words.add("ITERATE");
		words.add("JOIN");
		words.add("KEY");
		words.add("KEYS");
		words.add("KILL");
		words.add("LEADING");
		words.add("LEAVE");
		words.add("LEFT");
		words.add("LIKE");
		words.add("LIMIT");
		words.add("LINEAR");
		words.add("LINES");
		words.add("LOAD");
		words.add("LOCALTIME");
		words.add("LOCALTIMESTAMP");
		words.add("LOCK");
		words.add("LONG");
		words.add("LONGBLOB");
		words.add("LONGTEXT");
		words.add("LOOP");
		words.add("LOW_PRIORITY");
		words.add("MASTER_BIND");
		words.add("MASTER_SSL_VERIFY_SERVER_CERT");
		words.add("MATCH");
		words.add("MAXVALUE");
		words.add("MEDIUMBLOB");
		words.add("MEDIUMINT");
		words.add("MEDIUMTEXT");
		words.add("MIDDLEINT");
		words.add("MINUTE_MICROSECOND");
		words.add("MINUTE_SECOND");
		words.add("MOD");
		words.add("MODIFIES");
		words.add("NATURAL");
		words.add("NOT");
		words.add("NO_WRITE_TO_BINLOG");
		words.add("NULL");
		words.add("NUMERIC");
		words.add("ON");
		words.add("OPTIMIZE");
		words.add("OPTION");
		words.add("OPTIONALLY");
		words.add("OR");
		words.add("ORDER");
		words.add("OUT");
		words.add("OUTER");
		words.add("OUTFILE");
		words.add("PARTITION");
		words.add("PRECISION");
		words.add("PRIMARY");
		words.add("PROCEDURE");
		words.add("PURGE");
		words.add("RANGE");
		words.add("READ");
		words.add("READS");
		words.add("READ_WRITE");
		words.add("REAL");
		words.add("REFERENCES");
		words.add("REGEXP");
		words.add("RELEASE");
		words.add("RENAME");
		words.add("REPEAT");
		words.add("REPLACE");
		words.add("REQUIRE");
		words.add("RESIGNAL");
		words.add("RESTRICT");
		words.add("RETURN");
		words.add("REVOKE");
		words.add("RIGHT");
		words.add("RLIKE");
		words.add("SCHEMA");
		words.add("SCHEMAS");
		words.add("SECOND_MICROSECOND");
		words.add("SELECT");
		words.add("SENSITIVE");
		words.add("SEPARATOR");
		words.add("SET");
		words.add("SHOW");
		words.add("SIGNAL");
		words.add("SMALLINT");
		words.add("SPATIAL");
		words.add("SPECIFIC");
		words.add("SQL");
		words.add("SQLEXCEPTION");
		words.add("SQLSTATE");
		words.add("SQLWARNING");
		words.add("SQL_BIG_RESULT");
		words.add("SQL_CALC_FOUND_ROWS");
		words.add("SQL_SMALL_RESULT");
		words.add("SSL");
		words.add("STARTING");
		words.add("STRAIGHT_JOIN");
		words.add("TABLE");
		words.add("TERMINATED");
		words.add("THEN");
		words.add("TINYBLOB");
		words.add("TINYINT");
		words.add("TINYTEXT");
		words.add("TO");
		words.add("TRAILING");
		words.add("TRIGGER");
		words.add("TRUE");
		words.add("UNDO");
		words.add("UNION");
		words.add("UNIQUE");
		words.add("UNLOCK");
		words.add("UNSIGNED");
		words.add("UPDATE");
		words.add("USAGE");
		words.add("USE");
		words.add("USING");
		words.add("UTC_DATE");
		words.add("UTC_TIME");
		words.add("UTC_TIMESTAMP");
		words.add("VALUES");
		words.add("VARBINARY");
		words.add("VARCHAR");
		words.add("VARCHARACTER");
		words.add("VARYING");
		words.add("WHEN");
		words.add("WHERE");
		words.add("WHILE");
		words.add("WITH");
		words.add("WRITE");
		words.add("XOR");
		words.add("YEAR_MONTH");
		words.add("ZEROFILL");
	}
	
	/**
	 * 查看字段名称是否是合法，字段名必须是数字，字母和下划线，并且以字母开头
	 * @param fieldName - String
	 * @return boolean
	 */
	public static boolean lawfulKeyword(String fieldName) {
		String f = fieldName.toUpperCase();
		if(!f.matches("[A-Z][A-Z0-9_]*")) return false;
		
		return !words.contains(f);
	}
	
	private FiledKeyword(){}
	
	public static void main(String[] args) {
		System.err.println(lawfulKeyword("show"));
	}

}
