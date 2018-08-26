package org.ithang.entity;

import java.util.Date;

import org.ithang.tools.mate.Column;
import org.ithang.tools.mate.Ignore;
import org.ithang.tools.mate.Primary;
import org.ithang.tools.mate.Table;

@Table("sys_user_info")
public class UserBean {
    
	@Primary
	private String id;
	
	private String uname;
	
	@Primary
	private int age;
	
	private Integer grade;
	
	@Primary
	private Long high;
	
	@Column("create_time")
	private Date createTime;
	
	@Ignore
	private String opt;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public Long getHigh() {
		return high;
	}
	public void setHigh(Long high) {
		this.high = high;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
}
