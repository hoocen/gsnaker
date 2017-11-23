package org.gsnaker.engine.handlers;

import org.gsnaker.engine.core.Execution;

/**
 * 流程各模型操作处理接口
 * @author hoocen
 * @since 1.0
 */
public interface IHandler {

	/**
	 * 处理具体的流程操作
	 * @param execution 执行对象
	 */
	void handle(Execution execution);
}
