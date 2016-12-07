package org.ithang.test;

import java.io.Serializable;

import org.ithang.tools.mate.Primary;
import org.ithang.tools.mate.Table;

@Table("sys_user_info")
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Primary
	private long id;
	
	@Primary
	private String uname="abc";
	private int age=0;
	private String descs;
	 
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	 
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getDescs() {
		return descs;
	}
	public void setDescs(String descs) {
		this.descs = descs;
	}
	 
	
}
