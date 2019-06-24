package com.caiger.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang3.Validate;

import lombok.extern.slf4j.Slf4j;

/**
 * @className:  ReflectionUtils   
 * @description: 反射工具类
 * @author: 黄凯杰 
 * @date: 2019年2月26日 上午11:34:35
 * @version: V1.0.0
 * @copyright: Copyright © 2019 Caiger Digital Technology co., Ltd.
 */
@Slf4j
public class ReflectionUtils {
	
	
	/**
	 * @methodName: getFieldValue   
	 * @description: 获取对象中的属性值，不经过GET方法
	 * @param obj 对象
	 * @param fieldName 对象属性名称
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年2月26日 上午11:43:08
	 */
	public static Object getFieldValue(Object obj,String fieldName) {
		
		Field field = getAccessibleField(obj, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}
		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			log.error("不可能抛出的异常{ }", e.getMessage());
		}
		return result;
		
	}
	
	
	/**
	 * @methodName: setFieldValue   
	 * @description: 设置对象中的属性值，不经过SET方法
	 * @param obj 对象
	 * @param fieldName 对象属性
	 * @param value 对象属性值 
	 * @author: 黄凯杰
	 * @date: 2019年2月26日 下午2:09:53
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			log.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	
	
	/**
	 * @methodName: getAccessibleField   
	 * @description: 获取对象的getDeclaredField，并且设置为可访问
	 * @param obj  对象
	 * @param fieldName 对象属性名称
	 * @return
	 * @author: 黄凯杰
	 * @date: 2019年2月26日 下午1:14:37
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(fieldName, "fieldName can't be blank");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				//如果取不到fieldName属性则继续往父类搜索，直到类型为Object为止。
				Field field = superClass.getDeclaredField(fieldName);
				makeAccessible(field);
				return field;
			} catch (NoSuchFieldException e) {//NOSONAR
				// Field不在当前类定义,继续向上转型
				continue;
			}
		}
		return null;
	}
	
	
	/**
	 * @methodName: makeAccessible   
	 * @description: 强制修改属性为可访问,将private，protected属性转成public
	 * @param field
	 * @author: 黄凯杰
	 * @date: 2019年2月26日 下午1:19:32
	 */
	public static void makeAccessible(Field field) {
	
		/**
		 *  getModifiers():返回此类或接口以整数编码的 Java 语言修饰符
		 *  getDeclaringClass():如果此 Class 对象所表示的类或接口是另一个类的成员，则返回的 Class 对象表示该对象的声明类
		 *  isAccessible():获得此对象的 accessible 标志的值。
		 */
		if ((!Modifier.isPublic(field.getModifiers()) 
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers()) 
				|| Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}
	
	/**
	 * @methodName: getClassGenricType   
	 * @description: 通过反射获得class定义中生命的父类泛型的参数的类型
	 * @param clazz 对象
	 * @param index 例如：AreaService extends TreeService<AreaDao,Area> index:0=>AreaDao;1=>Area
	 * @return 返回一个对象。
	 * @author: 黄凯杰
	 * @date: 2019年2月26日 下午2:44:20
	 */
	@SuppressWarnings("rawtypes")
	public static Class getClassGenricType(Class clazz, int index) {

		//返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type
		Type genType = clazz.getGenericSuperclass();

		//该方法用于比较两个共享相同一般类型声明和具有相同类型参数的任何实例。 
		if (!(genType instanceof ParameterizedType)) {
			log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}
		
		//返回表示此类型实际类型参数的 Type 对象的数组
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			log.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}
	

}
