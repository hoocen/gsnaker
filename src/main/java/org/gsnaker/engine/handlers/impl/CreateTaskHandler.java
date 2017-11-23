package org.gsnaker.engine.handlers.impl;

import java.util.List;

import org.gsnaker.engine.SnakerInterceptor;
import org.gsnaker.engine.core.Execution;
import org.gsnaker.engine.core.ServiceContext;
import org.gsnaker.engine.entity.Task;
import org.gsnaker.engine.handlers.IHandler;
import org.gsnaker.engine.model.TaskModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gsnaker.engine.SnakerException;
/**
 * 任务创建处理器
 * @author hoocen
 * @since
 */
public class CreateTaskHandler implements IHandler{
	private static final Logger log = LoggerFactory.getLogger(CreateTaskHandler.class);
	
	private TaskModel taskModel;
	public CreateTaskHandler(TaskModel taskModel){
		this.taskModel = taskModel;
	}
	@Override
	public void handle(Execution execution) {
		List<Task> tasks = execution.getEngine().task().createTask(taskModel, execution);
		execution.addTasks(tasks);
		//从服务上下文中查找拦截器
		List<SnakerInterceptor> interceptors = ServiceContext.getContext().findList(SnakerInterceptor.class);
		try {
			for(SnakerInterceptor interceptor : interceptors) {
				interceptor.intercept(execution);
			}
		} catch(Exception e) {
			log.error("拦截器执行失败=" + e.getMessage());
			throw new SnakerException(e);
		}
	}

}
