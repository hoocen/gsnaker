package org.gsnaker.engine.test.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Assert;
import org.gsnaker.engine.access.ScriptRunner;
import org.gsnaker.engine.access.jdbc.JdbcHelper;
import org.junit.Test;

/**
 * 测试ScriptRunner工具类
 * @author hoocen
 * @since 1.0
 */
public class ScriptRunnerTest {

	@Test
	public void test(){
		try {
			Connection conn = JdbcHelper.getConnection(null);
			ScriptRunner runner = new ScriptRunner(conn, true);
			runner.runScript("db/core/schema-mysql.sql");
		} catch (SQLException | IOException e) {
			Assert.fail(e.getMessage());
		}
	}
}
