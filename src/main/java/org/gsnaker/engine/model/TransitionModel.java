package org.gsnaker.engine.model;

import org.gsnaker.engine.Action;
import org.gsnaker.engine.core.Execution;
import org.gsnaker.engine.handlers.impl.CreateTaskHandler;
import org.gsnaker.engine.handlers.impl.StartSubProcessHandler;
/**
 * Transition变迁定义元素
 * @author hoocen
 * @since
 */
public class TransitionModel extends BaseModel implements Action{

	private static final long serialVersionUID = -9099134575400466517L;

	/**
	 * 变迁的源节点引用
	 */
	private NodeModel source;
	/**
	 * 变迁的目标节点引用
	 */
	private NodeModel target;
	/**
	 * 变迁目标节点名称
	 */
	private String to;
	/**
	 * 变迁条件表达式，用于decision
	 */
	private String expr;
	/**
	 * 转折点图形数据
	 */
	private String g;
	/**
	 * 描述便宜位置
	 */
	private String offset;
	/**
	 * 当前变迁路径是否可用
	 */
	private boolean enabled = false;
	
	@Override
	public void execute(Execution execution) {
		if(!enabled) return;
		if(target instanceof TaskModel){
			//如果目标节点为TaskModel,则创建task
			fire(new CreateTaskHandler((TaskModel)target), execution);
		}else if(target instanceof SubProcessModel){
			//如果目标节点是SubProcessModel，则启动子流程
			fire(new StartSubProcessHandler((SubProcessModel) target), execution);
		}else{
			//如果目标几点为其他控制类，则由目标节点继续执行
			target.execute(execution);
		}
		
		
	}
	
	public NodeModel getSource() {
		return source;
	}
	public void setSource(NodeModel source) {
		this.source = source;
	}
	public NodeModel getTarget() {
		return target;
	}
	public void setTarget(NodeModel target) {
		this.target  = target;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getExpr() {
		return expr;
	}
	public void setExpr(String expr) {
		this.expr = expr;
	}
	public String getG() {
		return g;
	}
	public void setG(String g) {
		this.g = g;
	}
	public String getOffset() {
		return offset;
	}
	public void setOffset(String offset) {
		this.offset = offset;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
