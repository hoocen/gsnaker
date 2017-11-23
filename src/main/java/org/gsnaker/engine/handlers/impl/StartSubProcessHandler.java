package org.gsnaker.engine.handlers.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.gsnaker.engine.core.Execution;
import org.gsnaker.engine.entity.Order;
import org.gsnaker.engine.handlers.IHandler;
import org.gsnaker.engine.model.SubProcessModel;
import org.gsnaker.engine.SnakerEngine;
import org.gsnaker.engine.entity.Process;
import org.gsnaker.engine.SnakerException;
import org.gsnaker.engine.access.QueryFilter;
import org.gsnaker.engine.helper.AssertHelper;
/**
 * 启动子流程处理器
 * @author hoocen
 * @since
 */
public class StartSubProcessHandler implements IHandler{
	private SubProcessModel model;
	/**
	 * 是否以future方式执行启动子流程任务
	 */
	private boolean isFutureRunning = false;
	public StartSubProcessHandler(SubProcessModel model) {
		this.model = model;
	}
	
	public StartSubProcessHandler(SubProcessModel model, boolean isFutureRunning) {
		this.model = model;
		this.isFutureRunning = isFutureRunning;
	}
	@Override
	public void handle(Execution execution) {
		SnakerEngine engine = execution.getEngine();
		Process process = engine.process().getProcessByVersion(model.getProcessName(), model.getVersion());
		Order order = null;
		
		if(isFutureRunning){
			ExecutorService es = Executors.newSingleThreadExecutor();
			Future<Order> future = es.submit(new ExecuteTask(execution, process, model.getName()));
			es.shutdown();
			try {
				order = future.get();
			} catch (InterruptedException e) {
				throw new SnakerException("创建子流程线程被强制终止执行", e.getCause());
			} catch (ExecutionException e) {
				throw new SnakerException("创建子流程线程执行异常.", e.getCause());
			}
		}else{
			Execution child = execution.createSubExecution(execution, process, model.getName());
			order = engine.startInstanceByExecution(child);
		}
		
		AssertHelper.notNull(order, "子流程创建失败");
		execution.addTasks(engine.query().getActiveTasks(new QueryFilter().setOrderId(order.getId())));
	}
	
	class ExecuteTask implements Callable<Order> {
		private SnakerEngine engine;
		private Execution child;
		public ExecuteTask(Execution execution, Process process,String parentNodeName) {
			this.engine = execution.getEngine();
			child = execution.createSubExecution(execution, process, parentNodeName);
		}
		@Override
		public Order call() throws Exception {
			return engine.startInstanceByExecution(child);
		}
		
	}

}
