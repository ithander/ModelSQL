package org.ithang;

import java.lang.reflect.Method;
import java.util.Map;

import org.ithang.tools.ModelTools;

/**
 * 
 * @author ithang
 * 针对实体类每个字段与表中每一列的详细对应关系
 */
public class Model {

	private String tableName;
	private String tableLabel;//表别名
	
	private String fieldName;
	private String fieldType;
	
	private String columnName;
	private String columnType;
	private int columnLen;//列长度或精度
	private String labelName;//别名
	private String desc;//备注
	private Object value;//列默认值
	private boolean isNULL;//是否允许为空,true可以为空,false不可为空
	private boolean isPrimary;
	private boolean autoIncrement;//是否自增
	private String sequence;//采用的sequence，用在oracle中
   
	private String type;//列对应的java类型
	
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
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public boolean isPrimary() {
		return isPrimary;
	}
	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}
	
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
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
			if(isPrimary&&null!=sequence&&sequence.trim().length()>0){
				return sequence+".nextVal";
			}
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
			if(isPrimary&&null!=sequence&&sequence.trim().length()>0){
				return sequence+".nextVal";
			}
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
	public int getColumnLen() {
		return columnLen;
	}
	public void setColumnLen(int columnLen) {
		this.columnLen = columnLen;
	}
	public boolean isNULL() {
		return isNULL;
	}
	public void setNULL(boolean isNULL) {
		this.isNULL = isNULL;
	}
	public boolean isAutoIncrement() {
		return autoIncrement;
	}
	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
