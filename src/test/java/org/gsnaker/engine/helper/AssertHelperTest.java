package org.gsnaker.engine.helper;

import org.junit.Assert;
import org.junit.Test;

public class AssertHelperTest {

	@Test
	public void testNoException() {
		AssertHelper.isTrue(true);
		AssertHelper.isTrue(true, "true expression");
		
		AssertHelper.isNull(null);
		AssertHelper.isNull(null, "null expression");
		
		AssertHelper.notNull(new Object());
		AssertHelper.notNull(new Object(), "not null expression");
		
		AssertHelper.notEmpty("abc");
		AssertHelper.notEmpty("abc", "not empty expression");
	}
	
	@Test
	public void testException() {
		try {
			AssertHelper.isTrue(false);
			Assert.fail();
		}catch(IllegalArgumentException ex) {
		}
		
		try {
			AssertHelper.isTrue(false, "false expression");
			Assert.fail();
		}catch(IllegalArgumentException ex) {
		}
		
		try {
			AssertHelper.isNull(new Object());
			Assert.fail();
		}catch(IllegalArgumentException ex) {
		}
		
		try {
			AssertHelper.isNull(new Object(), "not null expression");
			Assert.fail();
		}catch(IllegalArgumentException ex) {
		}
		
		try {
			AssertHelper.notNull(null);
			Assert.fail();
		}catch(IllegalArgumentException ex) {
		}
		
		try {
			AssertHelper.notNull(null, "null expression");
			Assert.fail();
		}catch(IllegalArgumentException ex) {
		}
		
		try {
			AssertHelper.notEmpty("");
			Assert.fail();
		}catch(IllegalArgumentException ex) {
		}
		
		try {
			AssertHelper.notEmpty("", "empty expression");
			Assert.fail();
		}catch(IllegalArgumentException ex) {
		}
		
		try {
			AssertHelper.notEmpty(null, "empty expression");
			Assert.fail();
		}catch(IllegalArgumentException ex) {
		}
	}
	
}
