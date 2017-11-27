package org.gsnaker.engine.test.simple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gsnaker.engine.ITaskService;
import org.gsnaker.engine.access.QueryFilter;
import org.gsnaker.engine.entity.Order;
import org.gsnaker.engine.entity.Task;
import org.gsnaker.engine.helper.StreamHelper;
import org.gsnaker.engine.test.TestSnakerBase;
import org.junit.Before;
import org.junit.Test;

import org.junit.Assert;

/**
 * 转移表达式测试
 * @author hoocen
 * @since 1.1
 */
public class DecisionTest extends TestSnakerBase{
	@Before
	public void before(){
		//流程部署
		processId = engine.process().deploy(StreamHelper.getStreamFromClasspath("test/task/simple/decision.snaker.xml"));
	}
	@Test
	public void testDecision() {
		//执行start
		Order order = engine.startInstanceById(processId, "张三");
		List<Task> tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(order.getId()));
		Assert.assertEquals(tasks.size(), 1);
		
		//执行审批
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("result", true);
		engine.executeTask(tasks.get(0).getId(), "张三", args);
		
		//审批通过，流程结束
		tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(order.getId()));
		Assert.assertEquals(tasks.size(), 0);
		
		//审批通过，流程结束
		order = engine.query().getOrder(order.getId());
		Assert.assertNull(order);
	}
	@Test
	public void testDecision2() {
		//执行start
		Order order = engine.startInstanceById(processId, "张三");
		List<Task> tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(order.getId()));
		Assert.assertEquals(tasks.size(), 1);
		
		//执行审批
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("result", false);
		engine.executeTask(tasks.get(0).getId(), "张三", args);
		
		//审批未通过
		tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(order.getId()));
		Assert.assertEquals(tasks.size(), 1);
		
		//修改并通过
		args = new HashMap<String, Object>();
		args.put("result", true);
		engine.executeTask(tasks.get(0).getId(), "李四", args);
		
		//审批通过
		tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(order.getId()));
		Assert.assertEquals(tasks.size(), 1);
		engine.executeTask(tasks.get(0).getId(), "张三", args);
		
		//审批通过，流程结束
		order = engine.query().getOrder(order.getId());
		Assert.assertNull(order);
	}
}
