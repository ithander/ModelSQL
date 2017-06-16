package org.ithang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ithang.entity.UserBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * ModelSQL测试用例
 */
public class ModelSQL_InsertTest{
    
	@Before
	public void init(){
		
	}
	
	@Test
	public void insertTest0(){
		ModelSQL<UserBean> ms=new ModelSQL<UserBean>(UserBean.class);
		String sql=ms.insertSQL();
		System.out.println("byName=false includeID=true  ===>\n"+sql);
	}
	
	@Test
	public void insertTest1(){
		ModelSQL<UserBean> ms=new ModelSQL<UserBean>(UserBean.class);
		String sql=ms.insertSQL(true,true);
		System.out.println("byName=true includeID=true  ===>\n"+sql);
	}
	
	@Test
	public void insertTest2(){
		ModelSQL<UserBean> ms=new ModelSQL<UserBean>(UserBean.class);
		String sql=ms.insertSQL(false, false);
		System.out.println("byName=false includeID=false  ===>\n"+sql);
	}
	
	@Test
	public void insertTest3(){
		ModelSQL<UserBean> ms=new ModelSQL<UserBean>(UserBean.class);
		String sql=ms.insertSQL(true, false);
		System.out.println("byName=true includeID=false  ===>\n"+sql);
	}
	
	@Test
	public void inserttest_Map(){
		ModelSQL<UserBean> ms=new ModelSQL<UserBean>(UserBean.class);
		Map<String,Object> values=new HashMap<String,Object>();
		values.put("id", "10001");
		values.put("uname", "zhangsan");
		values.put("age", 18);
		values.put("grade", 80);
		values.put("high",170);
		values.put("createTime", new java.util.Date());
		String sql=ms.insertSQL(values);
		System.out.println("\n\n insert values===>"+sql);
	}
	
	@Test
	public void inserttest_Map1(){
		ModelSQL<UserBean> ms=new ModelSQL<UserBean>(UserBean.class);
		Map<String,Object> values=new HashMap<String,Object>();
		values.put("id", "10001");
		values.put("uname", "zhangsan");
		values.put("age", 18);
		//values.put("grade", 80);
		//values.put("high",170);
		values.put("createTime", new java.util.Date());
		String sql=ms.insertSQL(values);
		System.out.println("\n\n insert values1===>"+sql);
	}
	
	@Test
	public void inserttest_Map2(){
		ModelSQL<UserBean> ms=new ModelSQL<UserBean>(UserBean.class);
		Map<String,Object> values=new HashMap<String,Object>();
		//values.put("id", "10001");
		values.put("uname", "zhangsan");
		values.put("age", 18);
		//values.put("grade", 80);
		//values.put("high",170);
		values.put("createTime", new java.util.Date());
		String sql=ms.insertSQL(values);
		System.out.println("\n\n insert values2===>"+sql);
	}
	
	@Test
	public void insertTest_Bean(){
		ModelSQL<UserBean> ms=new ModelSQL<UserBean>(UserBean.class);
		UserBean ub=new UserBean();
		String sql=ms.insertSQL(ub);
		System.out.println("\n insert bean===>"+sql);
	}
	
	@Test
	public void insertTest_Bean1(){
		ModelSQL<UserBean> ms=new ModelSQL<UserBean>(UserBean.class);
		UserBean ub=new UserBean();
		ub.setId("sb001");
		String sql=ms.insertSQL(ub);
		System.out.println("\n insert bean1===>"+sql);
	}
	
	@Test
	public void insertTest_maps(){
		ModelSQL<UserBean> ms=new ModelSQL<UserBean>(UserBean.class);
		List<Map<String,Object>> mps=new ArrayList<Map<String,Object>>(3);
		
		Map<String,Object> v1=new HashMap<String,Object>();
		//values.put("id", "10001");
		v1.put("uname", "zhangsan");
		v1.put("age", 18);
		//values.put("grade", 80);
		//values.put("high",170);
		v1.put("createTime", new java.util.Date());
		
		Map<String,Object> v2=new HashMap<String,Object>();
		v2.put("uname", "lishi");
		v2.put("age", 19);
		v2.put("createTime", new java.util.Date());
		
		Map<String,Object> v3=new HashMap<String,Object>();
		v3.put("uname", "goodjob");
		v3.put("age", 21);
		v3.put("createTime", new java.util.Date());
		
		mps.add(v1);
		mps.add(v2);
		mps.add(v3);
		
		String s=ms.insertMapsSQL(mps);
		System.out.println("\n insert maps===>"+s);
	}
	
	
	@After
	public void destory(){
		
	}
	
}
