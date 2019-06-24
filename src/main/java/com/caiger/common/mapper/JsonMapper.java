package com.caiger.common.mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.caiger.common.lang.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import lombok.extern.slf4j.Slf4j;

/**
 * @className: JsonMapper
 * @description: 基于jackson的自定义Json解析器
 * @author: 黄凯杰
 * @date: 2018年12月27日 下午3:44:38
 * @version: V1.0.0
 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
 */
@Slf4j
public class JsonMapper extends ObjectMapper {

	/**
	 * @fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * @className: SingletonInstance
	 * @description: 单例模式，采用了类装载的机制来保证初始化实例时只有一个线程。 类的静态属性只会在第一次加载类的时候初始化，所以在这里，
	 *               JVM帮助我们保证了线程的安全性，在类进行初始化时，别的线程是无法进入的。
	 * @author: 黄凯杰
	 * @date: 2018年12月27日 下午3:58:24
	 * @version: V1.0.0
	 * @copyright: Copyright © 2018 Caiger Digital Technology co., Ltd.
	 */
	private static class SingletonInstance {
		private static final JsonMapper INSTANCE = new JsonMapper();
	}

	public static JsonMapper getInstance() {
		return SingletonInstance.INSTANCE;
	}

	private JsonMapper() {

		/**
		 * Jackson2ObjectMapperBuilder json转换器 spring ObjectMapper 初始化配置。
		 */
		new Jackson2ObjectMapperBuilder().configure(this);
		// 为Null时不序列化
		this.setSerializationInclusion(Include.NON_NULL);
		// 允许单引号
		this.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		// 允许不带引号的字段名称
		this.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		// 设置时区
		this.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// 遇到空值的处理为空字符串
		this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {

			@Override
			public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				gen.writeString(StringUtils.EMPTY);

			}
		});

	}
	
	
	/**
	 * @methodName: toJson   
	 * @description:  对象转换为JSON字符串
	 * @param object
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月27日 下午5:12:27
	 */
	public static String toJson(Object object){
		return JsonMapper.getInstance().toJsonString(object);
	}
	
	/**
	 * @methodName: fromJson   
	 * @description: JSON字符串转换为对象
	 * @param jsonString
	 * @param clazz
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月27日 下午5:12:17
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromJson(String jsonString, Class<?> clazz){
		return (T) JsonMapper.getInstance().fromJsonString(jsonString, clazz);
	}
	

	/**
	 * @methodName: toJsonString
	 * @description: 实体对象，也可以是集合或者数组 序列化成JSON字符串
	 * @param object 实体对象，也可以是集合或者数组
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月27日 下午3:46:53
	 */
	public String toJsonString(Object object) {

		try {
			return this.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.info("JsonMapper method toJsonString write to json String error:" + object, e);
		}
		return null;
	}

	
	/**
	 * @methodName: fromJsonString   
	 * @description: TODO(这里用一句话描述这个方法的作用)   
	 * @param jsonString 
	 * @param clazz
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月27日 下午5:11:30
	 */
	public <T> T fromJsonString(String jsonString, Class<T> clazz) {
		try {
			if (StringUtils.isEmpty(jsonString)) {
				return null;
			}
			return this.readValue(jsonString, clazz);
		} catch (IOException e) {
			log.info("JsonMapper method fromJsonString write to class error:" + jsonString, e);
		}
		return null;

	}
	

	/**
	 * @methodName: fromJsonForMapList   
	 * @description: JSON字符串转换为 List<Map<String, Object>>   
	 * @param jsonString
	 * @return
	 * @author: 黄凯杰
	 * @date: 2018年12月27日 下午5:10:28
	 */
	public static List<Map<String, Object>> fromJsonForMapList(String jsonString){
		List<Map<String, Object>> result = new ArrayList<>();
		if (StringUtils.startsWith(jsonString, "{")){
			Map<String, Object> map = fromJson(jsonString, Map.class);
			if (map != null){
				result.add(map);
			}
		}else if (StringUtils.startsWith(jsonString, "[")){
			List<Map<String, Object>> list = fromJson(jsonString, List.class);
			if (list != null){
				result = list;
			}
		}
		return result;
	}

}
