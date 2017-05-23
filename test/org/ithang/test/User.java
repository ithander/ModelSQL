package org.ithang.test;

import java.io.Serializable;

import org.ithang.tools.mate.ColumnInfo;
import org.ithang.tools.mate.Ignore;
import org.ithang.tools.mate.Primary;
import org.ithang.tools.mate.Table;

@Table("sys_user_info")
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Primary
	@ColumnInfo(type="bigint",len=25,autoIncrement=true)
	private long id;
	
	@Primary
	@ColumnInfo(type="varchar",len=18,value="abc")
	private String uname="abc";
	@ColumnInfo(type="int",len=3,value="18")
	private int age=0;
	@ColumnInfo(type="varchar",len=200)
	private String descs;
	@Ignore
	private int grade;
	 
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
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	 
	
}
