package org.gsnaker.engine.test;

import javax.sql.DataSource;

import org.gsnaker.engine.SnakerEngine;
import org.gsnaker.engine.access.jdbc.JdbcHelper;
import org.gsnaker.engine.cfg.Configuration;
/**
 * Snaker
 * @author hoocen
 * @since 1.0
 */
public class SnakerHelper {
	private static final SnakerEngine engine;
	static {
		DataSource dataSource = JdbcHelper.getDataSource();
		engine = new Configuration()
			.initAccessDBObject(dataSource)
			.buildSnakerEngine();
	}
	
	public static SnakerEngine getEngine() {
		return engine;
	}
}
