package org.gsnaker.engine.impl;

import java.util.Map;
import java.util.Map.Entry;

import javax.el.ExpressionFactory;

import org.gsnaker.engine.Expression;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
/**
 * Juel表达式解析器
 * @author hoocen
 * @since
 */
public class JuelExpression implements Expression{
	ExpressionFactory factory = new ExpressionFactoryImpl();
	@SuppressWarnings("unchecked")
	@Override
	public <T> T eval(Class<T> clazz, String expr, Map<String, Object> args) {
		SimpleContext context = new SimpleContext();
		for(Entry<String, Object> entry : args.entrySet()){
			context.setVariable(entry.getKey(), factory.createValueExpression(entry.getValue(), Object.class));
		}
		return (T) factory.createValueExpression(context, expr, clazz).getValue(context);
	}
}
