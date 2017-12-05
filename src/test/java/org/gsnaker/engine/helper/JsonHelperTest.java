package org.gsnaker.engine.helper;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;


public class JsonHelperTest {

	@Test
	public void testToJson() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 1);
		map.put("name", "Json");
		String json = JsonHelper.toJson(map);
	
		Person person = JsonHelper.fromJson(json, Person.class);
		
		Assert.assertEquals("Json", person.getName());
	}
	
	
}
class Person {
	private Long id;
	private String name;
	
	public Person() {
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
