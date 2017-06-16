package org.ithang.entity;

import java.sql.Date;

import org.ithang.tools.mate.Primary;

public class PersonBean {

	@Primary
	private long id;
	private String name;
	private int money;
	private Integer grade;
	private Long high;
	private Date birth;
	private String desc;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
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
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
