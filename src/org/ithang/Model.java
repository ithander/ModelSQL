package org.ithang;

import java.lang.reflect.Method;
import java.util.Map;

import org.ithang.tools.ModelTools;

/**
 * 
 * @author ithang
 *
 */
public class Model {

	private String tableName;
	private String tableLabel;//表别名
	
	private String fieldName;
	private String fieldType;
	
	private String columnName;
	private String columnType;
	private String labelName;//别名
	private String desc;//备注
	private Object defaultValue;
	private boolean isPrimary;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTableLabel() {
		return tableLabel;
	}
	public void setTableLabel(String tableLabel) {
		this.tableLabel = tableLabel;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Object getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	public boolean isPrimary() {
		return isPrimary;
	}
	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}
	
	/**
	 * 从实体类里得到值
	 * @param bean
	 * @return
	 */
	public Object getFieldValue(Object bean){
		Method md=null;
		Object r=null;
		try{
			md=bean.getClass().getMethod("get"+ModelTools.headUpper(fieldName));
			if(null!=md){
				r=md.invoke(bean);
			}
			if(null==r){
				r="NULL";
			}else if("String".equals(fieldType)){
				r="'"+r+"'";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return r;
	}
	
	/**
	 * 从实体类里得到值
	 * @param bean
	 * @return
	 */
	public Object getFieldValue(Map<String,Object> values){
		Object r=null;
		try{
			r=values.get(fieldName);
			if(null==r){
				r="NULL";
			}else if("String".equals(fieldType)){
				r="'"+r+"'";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return r;
	}
	
}
