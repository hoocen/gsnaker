package org.gsnaker.engine.model;

import org.gsnaker.engine.core.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gsnaker.engine.SnakerException;
import org.gsnaker.engine.core.ServiceContext;
import org.gsnaker.engine.helper.ClassHelper;
import org.gsnaker.engine.helper.StringHelper;
import org.gsnaker.engine.model.TransitionModel;
import org.gsnaker.engine.DecisionHandler;
import org.gsnaker.engine.Expression;
/**
 * 决策定义decision元素
 * @author hoocen
 * @since 1.0
 */
public class DecisionModel extends NodeModel{
	private static final Logger log = LoggerFactory.getLogger(DecisionModel.class);

	private static final long serialVersionUID = -1010301550111397526L;
	/**
	 * 决策选择表达式串（需要表达式引擎解析）
	 */
	private String expr;
	/**
	 * 决策处理类，对于复杂的分支条件，可通过handleClass来处理
	 */
	private String handleClass;
	/**
	 * 决策处理类实例
	 */
	private DecisionHandler decide;
	/**
	 * 表达式解析器
	 */
	private transient Expression expression;
	@Override
	public void exec(Execution execution) {
		log.info(execution.getOrder().getId() + "->decision execution.getArgs():" + execution.getArgs());
		if(expression == null) {
			expression = ServiceContext.getContext().find(Expression.class);
		}
		log.info("expression is " + expression);
		if(expression == null) throw new SnakerException("表达式解析器为空，请检查配置.");
		String next = null;
		if(StringHelper.isNotEmpty(expr)) {
			next = expression.eval(String.class, expr, execution.getArgs());
		} else if(decide != null) {
			next = decide.decide(execution);
		}
		log.info(execution.getOrder().getId() + "->decision expression[expr=" + expr + "] return result:" + next);
		boolean isfound = false;
		for(TransitionModel tm : getOutputs()) {
			if(StringHelper.isEmpty(next)) {
				String expr = tm.getExpr();
				if(StringHelper.isNotEmpty(expr) && expression.eval(Boolean.class, expr, execution.getArgs())) {
					tm.setEnabled(true);
					tm.execute(execution);
					isfound = true;
				}
			} else {
				if(tm.getName().equals(next)) {
					tm.setEnabled(true);
					tm.execute(execution);
					isfound = true;
				}
			}
		}
		if(!isfound) throw new SnakerException(execution.getOrder().getId() + "->decision节点无法确定下一步执行路线");
	}
	
	public String getExpr() {
		return expr;
	}
	public void setExpr(String expr) {
		this.expr = expr;
	}

	public String getHandleClass() {
		return handleClass;
	}

	public void setHandleClass(String handleClass) {
		this.handleClass = handleClass;
		if(StringHelper.isNotEmpty(handleClass)) {
			decide = (DecisionHandler)ClassHelper.newInstance(handleClass);
		}
	}

}
