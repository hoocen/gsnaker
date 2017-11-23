package org.gsnaker.engine.handlers.impl;

import java.util.List;

import org.gsnaker.engine.SnakerEngine;
import org.gsnaker.engine.SnakerException;
import org.gsnaker.engine.access.QueryFilter;
import org.gsnaker.engine.core.Execution;
import org.gsnaker.engine.entity.Order;
import org.gsnaker.engine.entity.Task;
import org.gsnaker.engine.entity.Process;
import org.gsnaker.engine.handlers.IHandler;
import org.gsnaker.engine.helper.StringHelper;
import org.gsnaker.engine.model.ProcessModel;
import org.gsnaker.engine.model.SubProcessModel;
/**
 * 结束流程实例的处理器
 * @author hoocen
 * @since 1.0
 */
public class EndProcessHandler implements IHandler{
	/**
	 * 结束当前流程实例，如果存在父流程，则触发父流程继续执行
	 */
	@Override
	public void handle(Execution execution) {
		SnakerEngine engine = execution.getEngine();
		Order order = execution.getOrder();
		List<Task> tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(order.getId()));
		for(Task task : tasks) {
			if(task.isMajor()) throw new SnakerException("存在未完成的主办任务，请确认。");
			engine.task().complete(task.getId(), SnakerEngine.AUTO);
		}
		//结束流程实例
		engine.order().complete(order.getId());
		
		//如果存在父流程，则重新构造Execution执行对象，交给父流程的SubProcessModel模型execute
		if(StringHelper.isNotEmpty(order.getParentId())){
			Order parentOrder = engine.query().getOrder(order.getParentId());
			if(parentOrder == null) return;
			Process process = engine.process().getProcessById(parentOrder.getParentId());
			ProcessModel pm = process.getModel();
			if(pm == null) return;
			SubProcessModel spm = (SubProcessModel) pm.getNode(order.getParentNodeName());
			Execution newExecution = new Execution(engine, process, parentOrder, execution.getArgs());
			spm.execute(newExecution);

			//SubProcessModel执行结果的tasks合并到当前执行对象execution的tasks列表中
			execution.addTasks(newExecution.getTasks());
		}
	}

}
