package org.ithang.s;

import static org.junit.Assert.*;

import org.ithang.MSQL;
import org.ithang.entity.UserBean;
import org.junit.Test;

public class MSQLTest {

	@Test
	public void select_test() {
		String ss=MSQL.select(UserBean.class);
		System.out.println(ss);
	}
	
	@Test
	public void select2_test() {
		String ss=MSQL.select(UserBean.class,"id , uname");
		System.out.println(ss);
	}
	
	@Test
	public void delete_test() {
		String ss=MSQL.delete(UserBean.class);
		System.out.println(ss);
	}
	
	@Test
	public void insert_test() {
		String ss=MSQL.insert(UserBean.class);
		System.out.println(ss);
	}
	
	@Test
	public void update_test() {
		String ss=MSQL.update(UserBean.class);
		System.out.println(ss);
	}

}
