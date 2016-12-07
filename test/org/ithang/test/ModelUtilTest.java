package org.ithang.test;

import java.lang.reflect.Field;

import org.ithang.tools.ModelTools;
import org.junit.Test;

public class ModelUtilTest {

	@Test
	public void test() {
		String tabName=ModelTools.getTableName(User.class);
		System.out.println(tabName);
	}
	
	@Test
	public void testGetField() {
		Field f=ModelTools.getField(User.class, "uname");
		System.out.println(f);
	}
	
	@Test
	public void testPkg() {
		System.out.println(ModelTools.pkg(new String[]{"aaa","bbb","ccc","ddd"}, "'", "',"));
	}
	
	@Test
	public void testTrimPrefix() {
		System.out.println(ModelTools.trimPrefix("abcLKJLJHOULKKJNl", "ab"));
	}
	
	@Test
	public void testTrimSuffix() {
		System.out.println(ModelTools.trimSuffix("abcLKJLJHOULKKJNleer", "er"));
	}
	
	@Test
	public void testGetFieldValue() {
		User u=new User();
		System.out.println("getFieldValue="+ModelTools.getFieldValue(u, "uname"));
		u.setUname("goodjob");
		System.out.println("getFieldValue="+ModelTools.getFieldValue(u, "uname"));
	}

}
