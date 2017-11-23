package org.gsnaker.engine;

import org.gsnaker.engine.core.Execution;

/**
 * 分配参与者的处理接口
 * 建议使用Assignment接口
 * @author hoocen
 * @since 1.0
 * @see org.gsnaker.engine.Assignment
 */
public interface AssignmentHandler {

	/**
	 * 分配参与者方法，可获取到当前的执行对象
	 * @param execution 执行对象
	 * @return Object 参与者对象
	 */
	Object assign(Execution execution);
}
