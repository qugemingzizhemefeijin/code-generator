package com.tigerjoys.np.cg.databases.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * JSON辅助类
 * @author chengang
 *
 */
public class JsonHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonHelper.class);
	
	/**
	 * 空的JSON字符串
	 */
	private static final String EMPTY_JSON_STRING = "{}";
	
	/**
	 * ObjectMapper，空值过滤
	 */
	private static final ObjectMapper OBJECT_MAPPER = initObjectMapper(true);
	
	/**
	 * ObjectMapper，空值不过滤
	 */
	private static final ObjectMapper OBJECT_NULL_MAPPER = initObjectMapper(false);
	
	/**
	 * 初始化ObjectMapper
	 */
	private static ObjectMapper initObjectMapper(boolean filterNull) {
		ObjectMapper objectMapper = new ObjectMapper();
		// 去掉默认的时间戳格式
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// 设置为中国上海时区
		objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		// 设置输入:禁止把POJO中值为null的字段映射到json字符串中
		objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		if(filterNull) {
			// 空值不序列化
			objectMapper.setSerializationInclusion(Include.NON_NULL);
		}
		// 反序列化时，属性不存在的兼容处理
		objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// 序列化时，日期的统一格式
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 单引号处理
		objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		
		return objectMapper;
	}

	/**
	 * 将JSON字符串转换为指定类型的JAVA BEAN对象
	 * @param json - String
	 * @param clazz - Class
	 * @return T
	 */
	public static <T> T toObject(String json, Class<T> clazz) {
		if (json == null || json.length() == 0)
			return null;

		try {
			return OBJECT_MAPPER.readValue(json, clazz);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 将JAVA BEAN对象转换为JSON字符串，不包含为空的属性，如果Map为空或者转换失败，将返回{@link JsonHelper#EMPTY_JSON_STRING}字符
	 * @param entity - T
	 * @return String
	 */
	public static <T> String toJson(T entity) {
		if (entity == null)
			return EMPTY_JSON_STRING;

		try {
			return OBJECT_MAPPER.writeValueAsString(entity);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return EMPTY_JSON_STRING;
	}
	
	/**
	 * 将JAVA BEAN对象转换为JSON字符串，并且包含为空的属性，如果Map为空或者转换失败，将返回{@link JsonHelper#EMPTY_JSON_STRING}字符
	 * @param entity - T
	 * @return String
	 */
	public static <T> String toJsonIncludeNull(T entity) {
		if (entity == null) {
			return EMPTY_JSON_STRING;
		}

		try {
			return OBJECT_NULL_MAPPER.writeValueAsString(entity);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return EMPTY_JSON_STRING;
	}

	/**
	 * 将JSON字符串转换为Map
	 * @param json - String
	 * @return Map<K , V>
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> toMap(String json) {
		if (json == null || json.length() == 0)
			return Collections.emptyMap();

		try {
			return OBJECT_MAPPER.readValue(json, HashMap.class);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}

		return Collections.emptyMap();
	}
	
	/**
	 * 将JSON字符串转换为有序Map
	 * @param json - String
	 * @return Map<K , V>
	 */
	@SuppressWarnings("unchecked")
	public static<K , V> Map<K , V> toLinkedMap(String json) {
		if (json == null || json.length() == 0)
			return Collections.emptyMap();

		try {
			return OBJECT_MAPPER.readValue(json, LinkedHashMap.class);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}

		return Collections.emptyMap();
	}
	
	/**
	 * 将JSON字符串转换为List
	 * @param json - String
	 * @return List<T>
	 */
	@SuppressWarnings("unchecked")
	public static <T>  List<T> toList(String json) {
		if (json == null || json.length() == 0)
			return Collections.emptyList();

		try {
			return OBJECT_MAPPER.readValue(json, ArrayList.class);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}

		return Collections.emptyList();
	}

	/**
	 * 将Map对象转换为JSON字符串，不包含为空的属性，如果Map为空或者转换失败，将返回{@link JsonHelper#EMPTY_JSON_STRING}字符
	 * @param obj - Map<K , V>
	 * @return String
	 */
	public static <K, V> String toJson(Map<K, V> obj) {
		if (obj == null || obj.isEmpty())
			return EMPTY_JSON_STRING;

		try {
			return OBJECT_MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return EMPTY_JSON_STRING;
	}
	
	/**
	 * 将Map对象转换为JSON字符串，并且包含为空的属性，如果Map为空或者转换失败，将返回{@link JsonHelper#EMPTY_JSON_STRING}字符
	 * @param obj - Map<K , V>
	 * @return String
	 */
	public static <K, V> String toJsonIncludeNull(Map<K, V> obj) {
		if (obj == null || obj.isEmpty()) {
			return EMPTY_JSON_STRING;
		}

		try {
			return OBJECT_NULL_MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return EMPTY_JSON_STRING;
	}

	/**
	 * 将Map对象转换为指定的Bean对象
	 * @param map - map
	 * @param clazz - Class<T>
	 * @return T
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T toObject(Map map, Class<T> clazz) {
		if (map == null || map.isEmpty())
			return null;

		return OBJECT_MAPPER.convertValue(map, clazz);
	}

	/**
	 * 将json array 字符串 转换为指定的集合类型
	 * @param jsonArrayStr - String
	 * @param clazz - Class<T>
	 * @return List<T>
	 */
	public static <T> List<T> toList(String jsonArrayStr, Class<T> clazz) {
		List<T> result = new ArrayList<T>();

		if (jsonArrayStr != null && jsonArrayStr.length() > 0) {
			try {
				List<Map<String, Object>> list = OBJECT_MAPPER.readValue(jsonArrayStr, new TypeReference<List<T>>() {});

				for (Map<String, Object> map : list) {
					result.add(toObject(map, clazz));
				}

				return result;
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		return result;
	}

}
