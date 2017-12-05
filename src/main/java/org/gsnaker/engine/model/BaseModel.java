package org.gsnaker.engine.model;

import java.io.Serializable;

import org.gsnaker.engine.core.Execution;
import org.gsnaker.engine.handlers.IHandler;

/**
 * 模型元素基础类
 * @author hoocen
 * @since 1.0
 */
public class BaseModel implements Serializable{
	private static final long serialVersionUID = -8977367334960067695L;

	/**
	 * 元素名称
	 */
	private String name;
	/**
	 * 显示名称
	 */
	private String displayName;
	/**
	 * 将流程对象execution交给具体的处理器处理
	 * @param handler 执行器
	 * @param execution 执行对象
	 */
	protected void fire(IHandler handler, Execution execution){
		handler.handle(execution);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	
}
