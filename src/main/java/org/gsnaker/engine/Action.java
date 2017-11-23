package org.gsnaker.engine;

import org.gsnaker.engine.core.Execution;

/**
 * 所有模型对象需要实现的接口，需要实现execute方法，每个节点实现方式不一样
 * @author hoocen
 * @since 1.0
 */
public interface Action {
	void execute(Execution execution);
}
