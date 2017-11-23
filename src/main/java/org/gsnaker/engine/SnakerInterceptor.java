package org.gsnaker.engine;

import org.gsnaker.engine.core.Execution;

/**
 * 任务拦截器
 * @author hoocen
 * @since 1.0
 */
public interface SnakerInterceptor {

	/**
	 * 拦截执行方法
	 * @param execution 流程执行对象
	 */
	void intercept(Execution execution);
}
