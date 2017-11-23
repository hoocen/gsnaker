package org.gsnaker.engine.access.transaction;
/**
 * 事务对象保持类
 * 该类用于绑定数据库访问对象（Connection、Session）
 * @author hoocen
 * @since 1.0
 */
public class TransactionObjectHolder {

	/**
	 * 线程局部变量，用于保持数据库访问对象
	 */
	private static final ThreadLocal<Object> container = new ThreadLocal<Object>();
	/**
	 * 绑定对象
	 * @param obj
	 */
	public static void bind(Object obj){
		container.set(obj);
	}
	/**
	 * 获取对象
	 */
	public static Object get(){
		return container.get();
	}
	/**
	 * 解绑对象
	 * @return
	 */
	public static Object unbind(){
		Object obj = container.get();
		container.remove();
		return obj;
	}
	
	/**
	 * 判断是否存在事务对象
	 * @return
	 */
	public static boolean isExistingTransaction() {
		return get() != null;
	}
}
