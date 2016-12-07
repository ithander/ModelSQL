package org.ithang.test;

import java.util.HashMap;
import java.util.Map;

import org.ithang.ModelSQL;
import org.junit.Before;
import org.junit.Test;

public class ModelSQLTest {

	private ModelSQL ms=null;
	
	@Before
	public void setup(){
		ms=new ModelSQL(User.class);
		System.out.println("before test");
	}
	
/*	@Test
	public void testInsertBean() {
		User u=new User();
		u.setId(123546);
		//u.setAge(25);
		u.setDesc(null);
		u.setName("haha");
		System.out.println(ms.insertSQL(true));
		System.out.println(ms.insertSQL(u));
	}
	
	@Test
	public void testInsertMap() {
		Map<String,Object> u=new HashMap<String,Object>();
		u.put("id",123546);
		//u.put("age",25);
		u.put("desc","good job good boy");
		u.put("name","haha");
		System.out.println(ms.insertSQL(u));
	}
	
	@Test
	public void testInsertValues() {
		System.out.println(ms.updateSQL(true));
	}
	
	@Test
	public void testUpdateBean()  {
		User u=new User();
		u.setId(1200);
		u.setName("good");
		System.out.println(ms.updateSQL(u));
	}
	
	@Test
	public void testUpdateValues()  {
		Map<String,Object> u=new HashMap<String,Object>();
		u.put("id", "13");
		u.put("age", 25);
		u.put("desc", "good job");
		u.put("deer", "good job");
		System.out.println(ms.updateSQL(u));
	}*/
	
	/*@Test
	public void testDelete()  {
		
		System.out.println(ms.deleteSQL(true));
	}
	
	@Test
	public void testDeleteIds()  {
		Map<String,Object> u=new HashMap<String,Object>();
		u.put("id", "13");
		u.put("age", 25);
		u.put("desc", "good job");
		u.put("deer", "good job");
		System.out.println("ids==="+ms.deleteSQL("a","b","cdd"));
	}
	
	@Test
	public void testDeleteValues()  {
		Map<String,Object> u=new HashMap<String,Object>();
		u.put("id", "13");
		u.put("age", 25);
		u.put("desc", "good job");
		u.put("name", "good job");
		System.out.println(ms.deleteSQL(u));
	}*/
	
	@Test
	public void testSelectValues()  {
		ModelSQL mm=new ModelSQL(User.class); 
		mm.select();
		Map<String,Object> u=new HashMap<String,Object>();
		u.put("id", "13");
		u.put("age", 25);
		//u.put("desc", "good job");
		//u.put("name", "good job");
		System.out.println(ms.select().leftJoin(mm).on("id", "id","uname","uname"));
	}
	
	@Test
	public void testSelectGroupBy()  {
		Map<String,Object> u=new HashMap<String,Object>();
		u.put("id", "13");
		u.put("age", 25);
		//u.put("desc", "good job");
		//u.put("name", "good job");
		System.out.println(ms.select().ascGroupBy("id","name").limit(0, 12));
	}

}
