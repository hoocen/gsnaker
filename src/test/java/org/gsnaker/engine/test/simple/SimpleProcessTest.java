package org.gsnaker.engine.test.simple;

import java.util.List;

import org.gsnaker.engine.ITaskService;
import org.gsnaker.engine.access.QueryFilter;
import org.gsnaker.engine.entity.HistoryOrder;
import org.gsnaker.engine.entity.Order;
import org.gsnaker.engine.entity.Task;
import org.gsnaker.engine.helper.StreamHelper;
import org.gsnaker.engine.model.ProcessModel;
import org.gsnaker.engine.model.TaskModel;
import org.gsnaker.engine.test.TestSnakerBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
/**
 * 测试流程基本操作.
 * 1、部署
 * 2、启动（自动执行start节点）
 * 3、获取任务
 * 4、执行任务
 * 5、历史查询
 * @author hoocen
 * @since 1.1
 */
public class SimpleProcessTest extends TestSnakerBase {

	private String orderId;
	
	@Before
	public void before(){
		//流程部署
		processId = engine.process().deploy(StreamHelper.getStreamFromClasspath("test/task/simple/process.snaker.xml"));
	}
	
	@Test
	public void test() {
		testProcess();
		testOrder();
		testTask();
		testEnd();
	}
	/**
	 * 流程定义获取
	 */
	private void testProcess() {
		ProcessModel model = processService.getProcessById(processId).getModel();
		Assert.assertNotNull(model);
		List<TaskModel> taskModels = model.getTaskModels();
		Assert.assertFalse(taskModels.isEmpty());
	}
	/**
	 * 启动流程实例
	 */
	private void testOrder() {
		Order order = engine.startInstanceById(processId, "张三");
		orderId = order.getId();
		Assert.assertEquals(order.getCreator(), "张三");
	}
	
	/**
	 * 获取流程实例中的task，执行task
	 */
	private void testTask() {
		List<Task> tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(orderId));
		
		ITaskService taskService = engine.task();
		for(Task task : tasks) {
			taskService.addTaskActor(task.getId(), "王五");
			Assert.assertFalse(taskService.isAllowed(task, "李四"));
			Assert.assertTrue(taskService.isAllowed(task, "王五"));
		}
		
		for(Task task : tasks) {
			taskService.take(task.getId(), "王五");
			engine.executeTask(task.getId(), "王五");
		}
	}
	
	/**
	 * 流程执行结束后的状态
	 */
	private void testEnd(){
		List<Task> tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(orderId));
		Assert.assertTrue(tasks.isEmpty());
		
		Order order = engine.query().getOrder(orderId);
		Assert.assertNull(order);
		HistoryOrder horder = engine.query().getHistOrder(orderId);
		Assert.assertNotNull(horder);
	}
	
}
