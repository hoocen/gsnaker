package org.gsnaker.engine.access.transaction;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gsnaker.engine.SnakerException;
import org.gsnaker.engine.helper.AssertHelper;
import org.gsnaker.engine.access.transaction.TransactionStatus;
import org.gsnaker.engine.helper.StringHelper;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 事务拦截器，用户生成业务逻辑类的代理类
 * @author hoocen
 * @since 1.0
 */
public abstract class TransactionInterceptor implements MethodInterceptor{
	private static final Logger log = LoggerFactory.getLogger(TransactionInterceptor.class);
	/**
	 * 需要拦截添加事务的方法集合
	 */
	private final static List<String> txMethods = new ArrayList<String>();
	static {
		txMethods.add("start*");
		txMethods.add("execute*");
		txMethods.add("finish*");
		txMethods.add("terminate*");
		txMethods.add("take*");
		txMethods.add("create*");
		txMethods.add("save*");
		txMethods.add("delete*");
		txMethods.add("remove*");
		txMethods.add("update*");
		txMethods.add("deploy*");
		txMethods.add("undeploy*");
		txMethods.add("redeploy*");
		txMethods.add("complete*");
		txMethods.add("assign*");
		txMethods.add("withdraw*");
		txMethods.add("reject*");
		txMethods.add("add*");
		txMethods.add("cascade*");
		txMethods.add("get*");
	}
	@Override
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		Object result = null;
		if(isMatch(method.getName())){
			if(log.isDebugEnabled()) {
				log.debug("intercept method is[name="  + method.getName() + "]");
			}
			TransactionStatus status = getTransaction();
			AssertHelper.notNull(status);
			try {
				result = proxy.invokeSuper(obj, args);
				//如果整个执行过程无异常抛出，则提交TransactionStatus持有的transaction对象
				if(status.isNewTransaction()) {
					commit(status);
				}
			} catch (Exception e) {
				rollback(status);
				throw new SnakerException(e);
			}
		}else{
			if(log.isDebugEnabled()) {
				log.debug("****don't intercept method is[name="  + method.getName() + "]");
			}
			result = proxy.invokeSuper(obj, args);
		}
		return result;
	}

	public <T> T getProxy(Class<T> clazz) {
		return clazz.cast(Enhancer.create(clazz, this));
	}
	
	/**
	 * 根据方法名称，匹配所有初始化的需要事务拦截的方法
	 * @param methodName
	 * @return
	 */
	private boolean isMatch(String methodName) {
		for(String pattern : txMethods) {
			if(StringHelper.simpleMatch(pattern, methodName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 初始化事务拦截器，设置访问对象
	 * @param accessObject
	 */
	public abstract void initialize(Object accessObject);
	
	/**
	 * 返回当前事务状态
	 * @return
	 */
	protected abstract TransactionStatus getTransaction();
	
	/**
	 * 提交当前事务状态
	 * @param status
	 */
	protected abstract void commit(TransactionStatus status);
	
	/**
	 * 回滚当前事务状态
	 * @param status
	 */
	protected abstract void rollback(TransactionStatus status);
}
