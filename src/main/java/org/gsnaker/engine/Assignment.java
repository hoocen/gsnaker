package org.gsnaker.engine;

import org.gsnaker.engine.core.Execution;
import org.gsnaker.engine.model.TaskModel;

/**
 * 分配参与者的处理抽象类
 * @author hoocen
 * @since 1.0
 */
public abstract class Assignment implements AssignmentHandler{

	/**
     * 分配参与者方法，可获取到当前的任务模型、执行对象
     * @param model 任务模型
     * @param execution 执行对象
     * @return Object 参与者对象
     */
    public abstract Object assign(TaskModel model, Execution execution);

    public Object assign(Execution execution) {
        return assign(null, execution);
    }
}
