package org.ithang;

import org.ithang.entity.UserBean;
import org.junit.Before;
import org.junit.Test;

/**
 * ModelSQL测试用例
 */
public class ModelSQL_UpdateTest{
    
	@Before
	public void init(){
		
	}
	
	@Test
	public void updateTest(){
		ModelSQL<UserBean> ms=new ModelSQL<UserBean>(UserBean.class);
		String sql=ms.updateSQL(true);
		System.out.println(sql);
	}
	
}
