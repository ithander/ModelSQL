package org.ithang;

import java.lang.reflect.Field;

public class Mod {

	private Class<?> cls;
	private Field[] fields;
	
	
	private volatile String tableName;
	private volatile String[] columnNames;
	private volatile String[] fieldNames;
	private volatile String[] primaryFieldNames;
	
	
	public Mod(Class<?> cls){
		setCls(cls);
		setFields(cls.getDeclaredFields());
		tableName=ModTool.getTableName(this);
		fieldNames=ModTool.getFieldNames(this);
		columnNames=ModTool.getColumnNames(this);
		primaryFieldNames=ModTool.getPrimaryFieldNames(this);
	}

	
	public String getTableName(){
		if(null==tableName){
			tableName=ModTool.getTableName(this);
		}
		return tableName;
	}
	
	public String[] getPrimaryFieldNames(){
		if(null==primaryFieldNames||0==primaryFieldNames.length){
			primaryFieldNames=ModTool.getPrimaryFieldNames(this);
		}
		return primaryFieldNames;
	}
	
	public String[] getFieldNames(){
		if(null==fieldNames||0==fieldNames.length){
			fieldNames=ModTool.getFieldNames(this);
		}
		return fieldNames;
	}
	
	public String[] getColumnNames(){
		if(null==columnNames||0==columnNames.length){
			columnNames=ModTool.getColumnNames(this);
		}
		return columnNames;
	}
	
	public String getColumnName(String fieldName){
		if(null!=fieldName&&fieldName.trim().length()>0){
			return ModTool.getColumnName(this, fieldName);
		}
		return null;
	}
	
	public Field getField(String fieldName) {
		if(null!=fields&&fields.length>0){
			for(Field field:fields){
				if(fieldName.equals(field.getName())){
					return field;
				}
			}
		}
		return null;
	}
	
	public Field[] getFields() {
		return fields;
	}

	public void setFields(Field[] fields) {
		this.fields = fields;
	}

	public Class<?> getCls() {
		return cls;
	}

	public void setCls(Class<?> cls) {
		this.cls = cls;
	}
	
	
}
