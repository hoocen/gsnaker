package test.task.group;

import java.util.ArrayList;
import java.util.List;

import org.gsnaker.engine.impl.GeneralAccessStrategy;

public class CustomAccessStrategy extends GeneralAccessStrategy{

	@Override
	protected List<String> ensureGroup(String operator) {
		List<String> groups = new ArrayList<String>();
		if(operator.equals("test")){
			groups.add("test");
		}else{
			groups.add("role1");
		}
		return groups;
	}

}
