package org.gsnaker.engine.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gsnaker.engine.test.TestSnakerBase;
import org.junit.Test;
import org.gsnaker.engine.access.QueryFilter;
import org.gsnaker.engine.entity.Order;
import org.gsnaker.engine.entity.Process;
import org.gsnaker.engine.entity.Task;
import org.gsnaker.engine.helper.StreamHelper;
import org.gsnaker.engine.model.ProcessModel;
import org.gsnaker.engine.model.StartModel;

public class ProcessTest extends TestSnakerBase{

	@Test
	public void test(){
		processId = engine.process().deploy(StreamHelper.getStreamFromClasspath("test/task/simple/process.snaker.xml"));
		Process process = engine.process().getProcessById(processId);
		System.out.println("output 1="+process);
		process = engine.process().getProcessByVersion(process.getName(), process.getVersion());
		System.out.println("output 2="+process);
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("task1.operator", "1");
		engine.startInstanceById(processId, "1", args);
		//engine.process().undeploy(processId);
	}
	
	@Test
	public void testUndeply(){
		Process process = engine.process().getProcessByName("simple");
		engine.process().undeploy(process.getId());
	}
	
	@Test
	public void testStartInstanceById(){
		Process process = engine.process().getProcessByName("simple");
		engine.startInstanceById(process.getId(), "1", null);
	}
	
	@Test
	public void testTask(){
		Order order = engine.query().getOrder("35436eb1ec2b42dc9c54fcfc64801d7e");
		Process process = engine.process().getProcessById(order.getProcessId());
		ProcessModel model = process.getModel();
		
		StartModel smodel = model.getStart();
		Execution execution = new Execution(engine, process, order, null);
		smodel.execute(execution);
		List<Task> tasks2 = execution.getTasks();
		for(Task task : tasks2) {
			System.out.println(task);
		}
	}
}
