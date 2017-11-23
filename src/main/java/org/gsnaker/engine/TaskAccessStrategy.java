package org.gsnaker.engine;

import java.util.List;

import org.gsnaker.engine.entity.TaskActor;

/**
 * 任务访问策略接口
 * 用于判断给定的操作人员是否有权限执行任务
 * @author hoocen
 * @since 1.0
 */
public interface TaskAccessStrategy {

	/**
	 * 根据操作人id、参与者集合判断是否允许访问所属任务
	 * @param operator 操作人id
	 * @param actors 参与者列表 传递至该接口的实现类中的参与者都是为非空
	 * @return boolean 是否允许访问
	 */
	boolean isAllowed(String operator, List<TaskActor> actors); 
}
