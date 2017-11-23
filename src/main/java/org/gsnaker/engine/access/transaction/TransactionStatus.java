package org.gsnaker.engine.access.transaction;
/**
 * 事务对象
 * @author hoocen
 * @since 1.0
 */
public class TransactionStatus {

	/**
	 * 事务对象
	 */
	private Object transaction;
	/**
	 * 是否为新的事务，考虑到业务层互相调用导致事务提前commit
	 */
	private final boolean newTransaction;
	
	public TransactionStatus(Object transaction, boolean newTransaction) {
		this.transaction = transaction;
		this.newTransaction = newTransaction;
	}

	public Object getTransaction() {
		return transaction;
	}

	public void setTransaction(Object transaction) {
		this.transaction = transaction;
	}

	/**
	 * 判断是否为新的事务对象，用于业务嵌套时commit判断是否对事务进行提交
	 */
	public boolean isNewTransaction() {
		return newTransaction;
	}
}
