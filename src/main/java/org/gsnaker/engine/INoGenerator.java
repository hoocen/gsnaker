package org.gsnaker.engine;

import org.gsnaker.engine.model.ProcessModel;
/**
 * 编号生成接口
 * 流程实例的编号字段使用该接口实现类来产生对应的编号
 * @author hoocen
 * @since
 */
public interface INoGenerator {

	/**
	 * 生成器方法
	 * @param model
	 * @return String 编号
	 */
	String generate(ProcessModel model);
}
