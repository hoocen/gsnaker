package org.gsnaker.engine.scheduling;

import java.util.List;

import org.gsnaker.engine.entity.Task;
/**
 * 任务job执行后的回调类
 * @author hoocen
 * @since 1.0
 */
public interface JobCallback {
	/**
	 * 回调函数
	 * @param taskId 当前任务id
	 * @param newTasks 新产生的任务集合
	 */
	void callback(String taskId, List<Task> newTasks);
}
