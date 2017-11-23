package test.generator;

import org.gsnaker.engine.INoGenerator;
import org.gsnaker.engine.model.ProcessModel;

public class CustomNoGenerator implements INoGenerator{

	@Override
	public String generate(ProcessModel model) {
		return java.util.UUID.randomUUID().toString().replace("-", "");
	}

}
