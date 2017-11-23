package org.gsnaker.engine;

import java.util.Map;

/**
 * 表达式解析接口
 * @author hoocen
 * @since 1.0
 */
public interface Expression {
	/**
	 * 根据表达式字符串、参数解析表达式，并返回指定类型的结果值
	 * @param clazz 返回类型
	 * @param expr 表达式
	 * @param args 参数
	 * @return T 表达式结果值
	 */
	<T> T eval(Class<T> clazz, String expr, Map<String, Object> args);
}
