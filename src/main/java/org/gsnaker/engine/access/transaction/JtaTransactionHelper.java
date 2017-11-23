package org.gsnaker.engine.access.transaction;

import javax.naming.InitialContext;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.gsnaker.engine.SnakerException;
import org.gsnaker.engine.helper.ConfigHelper;

/**
 * Jta事务帮助类
 * 
 * @author hoocen
 * @since 1.0
 */
public class JtaTransactionHelper {
	private static String userTransactionJndiName = ConfigHelper
			.getProperty("tx.jta.userTransaction");
	private static String transactionManagerJndiName = ConfigHelper
			.getProperty("tx.jta.transactionManager");

	public static UserTransaction lookupJeeUserTransaction() {
		return (UserTransaction) lookupFromJndi(userTransactionJndiName);
	}

	public static javax.transaction.Transaction lookupJeeTransaction() {
		try {
			TransactionManager transactionManager = lookupJeeTransactionManager();
			return transactionManager.getTransaction();
		} catch (Exception e) {
			throw new SnakerException("无法从事务管理中获取事务对象["
					+ transactionManagerJndiName + "]:\n" + e.getMessage(), e);
		}
	}

	public static TransactionManager lookupJeeTransactionManager() {
		return (TransactionManager) lookupFromJndi(transactionManagerJndiName);
	}

	public static Object lookupFromJndi(String jndiName) {
		try {
			InitialContext initialContext = new InitialContext();
			return initialContext.lookup(jndiName);
		} catch (Exception e) {
			throw new SnakerException("无法找到jndi名称[" + jndiName + "]\n"
					+ e.getMessage(), e);
		}
	}

	public static int getUserTransactionStatus(UserTransaction userTransaction) {
		int status = -1;
		try {
			status = userTransaction.getStatus();
		} catch (SystemException e) {
			throw new SnakerException("无法获取事务状态:" + e.getMessage(), e);
		}
		return status;
	}

	public static boolean isRollbackOnly() {
		try {
			return lookupJeeUserTransaction().getStatus() == Status.STATUS_MARKED_ROLLBACK;
		} catch (SystemException e) {
			throw new SnakerException("无法获取用户事务的状态" + e.getMessage(), e);
		}
	}

	public static void setRollbackOnly() {
		try {
			lookupJeeUserTransaction().setRollbackOnly();
		} catch (Exception e) {
			throw new SnakerException(
					"couldn't set user transaction to rollback only: "
							+ e.getMessage(), e);
		}
	}

	public static void registerSynchronization(Synchronization synchronization) {
		try {
			lookupJeeTransaction().registerSynchronization(synchronization);
		} catch (Exception e) {
			throw new SnakerException("couldn't register synchronization: "
					+ e.getMessage(), e);
		}
	}

	public static void begin() {
		try {
			lookupJeeUserTransaction().begin();
		} catch (Exception e) {
			throw new SnakerException("couldn't begin transaction: "
					+ e.getMessage(), e);
		}
	}

	public static void rollback() {
		try {
			lookupJeeUserTransaction().rollback();
		} catch (Exception e) {
			throw new SnakerException("couldn't rollback: " + e.getMessage(), e);
		}
	}

	public static void commit() {
		try {
			lookupJeeUserTransaction().commit();
		} catch (Exception e) {
			throw new SnakerException("couldn't commit: " + e.getMessage(), e);
		}
	}

	public static javax.transaction.Transaction suspend() {
		try {
			return lookupJeeTransactionManager().suspend();
		} catch (Exception e) {
			throw new SnakerException("couldn't suspend: " + e.getMessage(), e);
		}
	}

	public static void resume(javax.transaction.Transaction transaction) {
		try {
			lookupJeeTransactionManager().resume(transaction);
		} catch (Exception e) {
			throw new SnakerException("couldn't resume: " + e.getMessage(), e);
		}
	}
}
