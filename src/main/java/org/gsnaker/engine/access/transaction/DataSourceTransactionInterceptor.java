package org.gsnaker.engine.access.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gsnaker.engine.access.jdbc.JdbcHelper;
import org.gsnaker.engine.helper.AssertHelper;

/**
 * Jdbc方式的数据库事务拦截处理
 * @author hoocen
 * @since
 */
public class DataSourceTransactionInterceptor extends TransactionInterceptor{
	private static final Logger log = LoggerFactory.getLogger(DataSourceTransactionInterceptor.class);
	private DataSource dataSource;
	@Override
	public void initialize(Object accessObject) {
		if(accessObject == null) return;
		if(accessObject instanceof DataSource) {
			this.dataSource = (DataSource)accessObject;
		}
	}

	@Override
	protected TransactionStatus getTransaction() {
		try {
			boolean isExistingTransaction = TransactionObjectHolder.isExistingTransaction();
			if(isExistingTransaction) {
				return new TransactionStatus(TransactionObjectHolder.get(), false);
			}
			Connection conn = JdbcHelper.getConnection(dataSource);
			conn.setAutoCommit(false);
        	if(log.isInfoEnabled()) {
        		log.info("begin transaction=" + conn.hashCode());
        	}
			TransactionObjectHolder.bind(conn);
			return new TransactionStatus(conn, true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	protected void commit(TransactionStatus status) {
		AssertHelper.isTrue(status.isNewTransaction());
		Connection conn = (Connection) status.getTransaction();
		if(conn != null){
			try {
				if(log.isInfoEnabled()) {
            		log.info("commit transaction=" + conn.hashCode());
            	}
				conn.commit();
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
			}finally{
				try {
					JdbcHelper.close(conn);
				} catch (SQLException e) {
	            	log.error(e.getMessage(), e);
	                throw new RuntimeException(e.getMessage(), e);
				}
            	TransactionObjectHolder.unbind();
			}
		}
	}

	@Override
	protected void rollback(TransactionStatus status) {
		Connection conn = (Connection) status.getTransaction();
		if(conn != null){
			try {
				if(log.isInfoEnabled()) {
            		log.info("rollback transaction=" + conn.hashCode());
            	}
				if(!conn.isClosed()){
					conn.rollback();
				}
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
			}finally{
				try {
					JdbcHelper.close(conn);
				} catch (SQLException e) {
	            	log.error(e.getMessage(), e);
	                throw new RuntimeException(e.getMessage(), e);
				}
            	TransactionObjectHolder.unbind();
			}
		}
		
	}

}
