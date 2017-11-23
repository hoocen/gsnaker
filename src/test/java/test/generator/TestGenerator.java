package test.generator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gsnaker.engine.helper.StreamHelper;
import org.gsnaker.engine.test.TestSnakerBase;
import org.junit.Before;
import org.junit.Test;
import org.gsnaker.engine.access.QueryFilter;
import org.gsnaker.engine.entity.Order;
import org.gsnaker.engine.entity.Task;

public class TestGenerator extends TestSnakerBase{

	@Before
	public void before(){
		processId = engine.process().deploy(StreamHelper.getStreamFromClasspath("test/generator/process.snaker"));
	}
	@Test
	public void test(){
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("task1.operator", new String[]{"1"});
		Order order = engine.startInstanceById(processId, "2", args);
		System.out.println("order=" + order);
		List<Task> tasks = queryService.getActiveTasks(new QueryFilter().setOrderId(order.getId()));
		for(Task task : tasks) {
			engine.executeTask(task.getId(), "1");
		}
	}
}
