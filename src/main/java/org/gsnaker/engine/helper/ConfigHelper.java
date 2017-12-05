package org.gsnaker.engine.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置属性帮助类
 * @author hoocen
 * @since 1.0
 */
public class ConfigHelper {
	private static final Logger log = LoggerFactory.getLogger(ConfigHelper.class);
	/**
	 * 默认配置属性文件名
	 */
	private final static String PROPERTIES_FILENAME = "gsnaker.properties";
	/**
	 * 配置属性对象
	 */
	private static Properties properties;
	/**
     * 重新加载属性，之前的属性被清空
     * @param props 新的属性键值对
     */
    public static void loadProperties(Properties props) {
        properties = props;
    }
    /**
     * 获取当前属性对象
     * @return 属性对象
     */
    public static Properties getProperties() {
        if(properties == null) {
            synchronized (ConfigHelper.class) {
                if(properties == null) {
                    loadProperties(PROPERTIES_FILENAME);
                }
            }
        }
        return properties;
	}

    /**
     * 根据key获取配置的字符串value值
     * @param key 属性键
     * @return 属性值
     */
    public static String getProperty(String key) {
        if (key == null) {
            return null;
        }
        return getProperties().getProperty(key);
    }
    
    /**
     * 根据key获取配置的数字value值
     * @param key 属性键
     * @return 转为int的属性值
     */
    public static int getNumerProperty(String key) {
        String value = getProperties().getProperty(key);
        if(NumberUtils.isNumber(value)) {
        	return Integer.parseInt(value);
        } else {
        	return 0;
        }
    }
	/**
	 * 根据指定文件名，从类路径中加载属性配置文件
	 * @param fileName 属性配置文件名
	 */
	public static void loadProperties(String fileName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream in = null;
		properties = new Properties();
		if(classLoader != null) {
			in = classLoader.getResourceAsStream(fileName);
		}
		
		if(in == null) {
			in = ConfigHelper.class.getClassLoader().getResourceAsStream(fileName);
			if (in == null) {
                log.warn("No properties file found in the classpath by filename " + fileName);
            }
		}else {
			try {
				properties.load(in);
				log.info("Properties read " + properties);
			} catch (IOException e) {
				log.error("Error reading from " + fileName, e);
			} finally {
                try {
                    in.close();
                } catch (IOException e) {
                    log.warn("IOException while closing InputStream: " + e.getMessage());
                }
            }
		}
	}
}
