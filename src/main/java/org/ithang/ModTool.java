package org.ithang;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.ithang.tools.lang.StrUtils;
import org.ithang.tools.mate.Column;
import org.ithang.tools.mate.Primary;
import org.ithang.tools.mate.Table;

public class ModTool {

	public static String getTableName(Mod md){
		if(md.getCls().isAnnotationPresent(Table.class)){
			if(md.getCls().isAnnotationPresent(Table.class)){
				return md.getCls().getAnnotation(Table.class).value();
			}
		}
		return StrUtils.addUnderline(md.getCls().getSimpleName());
	}
	
	public static String getColumnName(Mod md,String fieldName){
		Field field=md.getField(fieldName);
		if(null!=field){
			if(field.isAnnotationPresent(Column.class)){
				return field.getAnnotation(Column.class).value();
			}else{
				StrUtils.addUnderline(field.getName());	
			}
		}
		return StrUtils.addUnderline(fieldName);
	}
	
	public static String[] getColumnNames(Mod md){
		if(null!=md&&null!=md.getFields()&&md.getFields().length>0){
			String[] columnNames=new String[md.getFields().length];
			int i=0;
			for(Field field:md.getFields()){
				if(field.isAnnotationPresent(Column.class)){
					columnNames[i++]=field.getAnnotation(Column.class).value();
				}else{
					columnNames[i++]=StrUtils.addUnderline(field.getName());	
				}
			}
			return columnNames;
		}
		return null;
	}
	
	public static String[] getFieldNames(Mod md){
		if(null!=md&&null!=md.getFields()&&md.getFields().length>0){
			String[] fieldNames=new String[md.getFields().length];
			int i=0;
			for(Field field:md.getFields()){
				fieldNames[i++]=field.getName();
			}
			return fieldNames;
		}
		return null;
	}
	
	/**
	 * 得到主键字段名称
	 * @return
	 */
	public static String[] getPrimaryFieldNames(Mod md){
		if(null!=md&&null!=md.getFields()&&md.getFields().length>0){
			String[] fieldNames=new String[md.getFields().length];
			int i=0;
			for(Field field:md.getFields()){
				if(field.isAnnotationPresent(Primary.class)){
					fieldNames[i++]=field.getName();
				}
			}
			return Arrays.copyOf(fieldNames,i);
		}
		return null;
	}
	
	/**
	 * 得到主键字段名称
	 * @return
	 */
	public static String[] getPrimaryColumnNames(Mod md){
		if(null!=md&&null!=md.getFields()&&md.getFields().length>0){
			String[] columnNames=new String[md.getFields().length];
			int i=0;
			for(Field field:md.getFields()){
				if(field.isAnnotationPresent(Primary.class)){
					if(field.isAnnotationPresent(Column.class)){
						columnNames[i++]=field.getAnnotation(Column.class).value();
					}else{
						columnNames[i++]=StrUtils.addUnderline(field.getName());	
					}
				}
			}
			return columnNames;
		}
		return null;
	}
}
