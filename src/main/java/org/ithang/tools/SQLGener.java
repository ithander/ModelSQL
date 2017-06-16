package org.ithang.tools;

import java.lang.reflect.Field;
import java.util.Map;

import org.ithang.tools.lang.StrUtils;

@Deprecated
public class SQLGener {
	/**
	 * 根据实体类生成Insert SQL
	 * @param bean
	 * @return
	 */
	public static String insert(Object bean){
		StringBuilder sber=new StringBuilder();
		String[] fieldNames=ModelTools.getFieldNames(bean.getClass());
		sber.append("insert into ").append(ModelTools.getTableName(bean.getClass()));
		sber.append("(").append(StrUtils.trimSuffix(StrUtils.pkg(fieldNames, "", ","),",")).append(")");
		sber.append(" values (");
		int i=0;
		for(String fieldName:fieldNames){
			sber.append(ModelTools.getFieldValue(bean, fieldName));
			if(i<fieldNames.length-1){
				sber.append(",");		
			}
			i++;
		}
		sber.append(")");
		return sber.toString();
	}
	
	/**
	 * 根据类信息与key-value对生成Insert SQL
	 * @param cls
	 * @param values
	 * @return
	 */
	public static String insertSQL(Class<?> cls,Map<String,Object> values){
		StringBuilder sber=new StringBuilder();
		String[] fieldNames=ModelTools.getFieldNames(cls);
		String[] columnNames=ModelTools.getColumnNames(cls);
		sber.append("insert into ").append(ModelTools.getTableName(cls));
		sber.append("(").append(StrUtils.trimSuffix(StrUtils.pkg(columnNames, "", ","),",")).append(")");
		sber.append(" values (");
		int i=0;
		for(String fieldName:fieldNames){
			Field field=ModelTools.getField(cls, fieldName);
			if(field.getType()==String.class){
				sber.append("'").append(values.get(fieldName)).append("'");
			}else{
				sber.append(values.get(fieldName));
			}
			if(i<fieldNames.length-1){
				sber.append(",");		
			}
			i++;
		}
		sber.append(")");
		return sber.toString();
	}
	
	/**
	 * 根据类信息与相关值(有顺序性)生成Insert SQL
	 * @param cls
	 * @param values
	 * @return
	 */
	public static String insertSQL(Class<?> cls,Object...values){
		StringBuilder sber=new StringBuilder();
		String[] fieldNames=ModelTools.getFieldNames(cls);
		String[] columnNames=ModelTools.getColumnNames(cls);
		sber.append("insert into ").append(ModelTools.getTableName(cls));
		sber.append("(").append(StrUtils.trimSuffix(StrUtils.pkg(columnNames, "", ","),",")).append(")");
		sber.append(" values (");
		
		if(null!=values&&values.length>0){
			int i=0;
			for(Object value:values){
				if(value.getClass()==String.class){
					sber.append("'").append(value).append("'");
				}else{
					sber.append(value);
				}
				if(i<fieldNames.length-1){
					sber.append(",");		
				}
				i++;
			}
		}else{
			int len=fieldNames.length;
			for(int i=0;i<len;i++){
				if(i<fieldNames.length-1){
					sber.append("?,");
				}else{
					sber.append("?");
				}
				
			}
		}
		
		sber.append(")");
		return sber.toString();
	}
	
	/**
	 * 根据实体类实例生成Update SQL
	 * @param bean
	 * @return
	 */
	public static String updateSQL(Object bean){
	    return null;	
	}
	
	/**
	 * 根据类信息及key-value对生成Update SQL
	 * @param cls
	 * @param values
	 * @return
	 */
	public static String updateSQL(Class<?> cls,Map<String,Object> values){
	    return null;	
	}
	
	/**
	 * 根据类信息及主键ID值生成Delete SQL
	 * @param cls
	 * @param id 主键值
	 * @return
	 */
	public static String deleteSQL(Class<?> cls,Object... id){
		return null;
	}
	
	/**
	 * 根据类信息生成全量Select SQL
	 * @param cls
	 * @return
	 */
	public static String selectSQL(Class<?> cls){
		return null;
	}
	
	/**
	 * 根据条件生成条件Select SQL
	 * @param cls
	 * @param values
	 * @return
	 */
	public static String selectSQL(Class<?> cls,Map<String,Object> values){
		return null;
	}
	
}
