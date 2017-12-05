package org.gsnaker.engine.helper;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

public class ClassHelperTest {

	@Test
	public void testCastLong() {
		Long l = new Long(1);
		Assert.assertEquals(1L,ClassHelper.castLong(l));
		
		BigDecimal bd = new BigDecimal(1);
		Assert.assertEquals(1L,ClassHelper.castLong(bd));
		
		BigInteger bi = new BigInteger("1");
		Assert.assertEquals(1L,ClassHelper.castLong(bi));
		
		Integer i = new Integer(1);
		Assert.assertEquals(1L,ClassHelper.castLong(i));
		
		Short s = new Short("1");
		Assert.assertEquals(1L,ClassHelper.castLong(s));
		
		Byte b = new Byte("1");
		Assert.assertEquals(1L,ClassHelper.castLong(b));
	}
	
	@Test
	public void testLoadClass() {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			loader.loadClass("java.lang.String");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testInstance() {
		Object object = ClassHelper.newInstance("org.gsnaker.engine.helper.ClassHelperTest");
		Assert.assertTrue(object instanceof ClassHelperTest);
	}
}
