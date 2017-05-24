package org.ithang.tools;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.ithang.Model;
import org.ithang.tools.mate.Column;
import org.ithang.tools.mate.ColumnInfo;
import org.ithang.tools.mate.Ignore;
import org.ithang.tools.mate.Primary;
import org.ithang.tools.mate.Table;

public final class ModelTools {

	private final static Random r=new Random(System.currentTimeMillis()); 
	private final static String[] tableLabels=new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","X","Y","Z"};
	public static List<Model> parseCls(Class<?> cls){
		String tableName=null;
		String tableLabel=null;
		Model m=null;
		Field[] fields=null;
		List<Model> mds=null;
		try{
			tableName=getTableName(cls);
			tableLabel=tableLabels[r.nextInt(tableLabels.length)]+String.valueOf(r.nextInt(36));
			fields=cls.getDeclaredFields();
			if(null!=fields&&fields.length>0){
				mds=new ArrayList<Model>(fields.length);
				for(Field field:fields){
					
					if("serialVersionUID".equals(field.getName())){
						continue;
					}
					
                    m=new Model();
                    m.setTableName(tableName);
                    m.setFieldName(field.getName());
                    m.setFieldType(field.getType().getSimpleName());
                    m.setTableLabel(tableLabel);
                    //判断是否存在column注解，如果存在则用注解值作为列名
                    if(field.isAnnotationPresent(Column.class)){
						m.setColumnName(field.getAnnotation(Column.class).value());
					}else{
						//如果存在ignore注解，则表示不处理该字段，该字段不对应表中任何列
						if(field.isAnnotationPresent(Ignore.class)){
							continue;
						}else{//如果column和ignore都不存在，则默认列名为字段名的驼峰式转下划线式
						    m.setColumnName(addUnderline(field.getName()));
						}
					}
                    m.setValue("NULL");
                    
                    //判断是不是主键
                    if(field.isAnnotationPresent(Primary.class)){
                    	Primary pri=field.getAnnotation(Primary.class);
                    	if(null!=pri&&null!=pri.Seq()&&pri.Seq().trim().length()>0){//设置主键sequence
                    		m.setSequence(pri.Seq());
                    	}
                    	m.setPrimary(true);	
                    }else{
                    	m.setPrimary(false);
                    }
                    
                    //得到列的相关信息，用于创建表
                    if(field.isAnnotationPresent(ColumnInfo.class)){
                    	ColumnInfo ci=field.getAnnotation(ColumnInfo.class);
                    	m.setValue((null==ci.value())?null:ci.value().trim().length()==0?null:ci.value());
                    	m.setNULL(ci.isNULL());
                    	m.setColumnType(ci.type());
                    	m.setColumnLen(ci.len());
                    	m.setAutoIncrement(ci.autoIncrement());
                    }
                    
                    mds.add(m);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return mds;
	}
	
	/**
	 * 得到表名
	 * @param cls
	 * @return
	 */
	public static String getTableName(Class<?> cls){
		if(cls.isAnnotationPresent(Table.class)){
			Table table=cls.getAnnotation(Table.class);
			if(null!=table&&isNotEmpty(table.value())){
				return table.value();
			}
		}
		return cls.getSimpleName().toLowerCase();
	}
	
	public static Field getField(Class<?> cls,String fieldName){
		Field field=null;
		try{
			field=cls.getDeclaredField(fieldName);
		}catch(Exception e){
			e.printStackTrace();
		}
		return field;
	}
	
	public static String getFieldValue(Object bean,String fieldName){
		Field field=null;
		try{
			field=getField(bean.getClass(),fieldName);
			Class<?> fieldType=field.getType();
			Object v=bean.getClass().getMethod("get"+headUpper(fieldName)).invoke(bean);
			if(fieldType==String.class){
				return "'"+(v==null?"":v)+"'";
			}else{
				return v==null?"0":String.valueOf(v);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String[] getFieldNames(Class<?> cls){
		String[] fieldNames=null;
		try{
			Field[] fields=cls.getDeclaredFields();
			if(null!=fields&&fields.length>0){
				fieldNames=new String[fields.length];
				int i=0;
				for(Field field:fields){
					fieldNames[i++]=field.getName();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return fieldNames;
	}
	
	public static String[] getColumnNames(Class<?> cls){
		String[] columnNames=null;
		try{
			Field[] fields=cls.getDeclaredFields();
			if(null!=fields&&fields.length>0){
				columnNames=new String[fields.length];
				int i=0;
				for(Field field:fields){
					if(field.isAnnotationPresent(Column.class)){
						columnNames[i++]=field.getAnnotation(Column.class).value();
					}else{
						columnNames[i++]=addUnderline(field.getName());	
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return columnNames;
	}
	
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		if(null!=str&&str.trim().length()>0){
			return true;
		}
		return false;
	}
	
	public static String pkg(String[] values,String prefix,String suffix){
		StringBuilder sber=new StringBuilder();
		for(String value:values){
			sber.append(prefix).append(value).append(suffix);
		}
		return sber.toString();
	}
	
	/**
	 * 去前缀
	 * @param value
	 * @param prefix
	 * @param suffix
	 * @return
	 */
	public static String trimPrefix(String value,String ... prefixs){
		if(null!=prefixs&&prefixs.length>0){
            for(String prefix:prefixs){
            	if(value.startsWith(prefix)){
            		return value.replaceFirst(prefix, "");//只去前缀一次
            	}
            }
		}
		return value.trim();
	}
	
	/**
	 * 去后缀
	 * @param value
	 * @param prefix
	 * @param suffix
	 * @return
	 */
	public static String trimSuffix(String value,String ... suffixs){
		if(null!=suffixs&&suffixs.length>0){
            for(String suffix:suffixs){
            	if(value.endsWith(suffix)){
            		return value.substring(0, value.length()-suffix.length());//只去后缀一次
            	}
            }
		}
		return value.trim();
	}
	
	/**
	 * 首字母大写
	 * @param value
	 * @return
	 */
	public static String headUpper(String value){
		char[] chars=value.toCharArray();
		if(null!=chars&&chars.length>1&&Character.isUpperCase(chars[1])){
			
		}else{
		    chars[0]=Character.toUpperCase(chars[0]);
		}
		return String.valueOf(chars);
    }
	
	/**
	 * 去掉下划线,并转为小写
	 * @param tname
	 * @return
	 */
	public static String dropUnderline(String tname){
		StringBuffer columnName=new StringBuffer();
		tname=tname.toLowerCase();
		
		char[] tnames=tname.toCharArray();
		boolean _show=false;
		for(int i=0;i<tnames.length;i++){
			if("_".equals(String.valueOf(tnames[i]))){
				_show=true;
				continue;
			}
			if(_show){
				columnName.append(String.valueOf(Character.toUpperCase(tnames[i])));
				_show=false;
			}else{
				columnName.append(String.valueOf(tnames[i]));
			}
		}
		return columnName.toString();
	}
	
	/**
	 * 增加下划线,并转为大写
	 * @param tname
	 * @return
	 */
	public static String addUnderline(String tname){
		String columnName="";
		
		char[] tnames=tname.toCharArray();
		boolean _show=false;
		for(int i=0;i<tnames.length;i++){
			if(Character.isUpperCase(tnames[i])&&i>0){
				_show=true;
			}
			if(_show){
				columnName+="_"+tnames[i];
				_show=false;
			}else{
				columnName+=tnames[i];
			}
		}
		return columnName.toLowerCase();
}

	public static void generBeanFromMySQL(Connection conn,String schema) throws SQLException{
		QueryRunner qr=new QueryRunner();
		List<String> tables=qr.query(conn,"select table_name from information_schema.columns where table_schema='"+schema+"'", new ColumnListHandler<String>());
		if(null!=tables&&tables.size()>0){
			for(String table:tables){
				List<Column> columns=qr.query(conn, "select table_name,column_name,is_nullable,data_type,column_key,character_maximum_length len from information_schema.columns where table_schema='"+schema+"'", new BeanListHandler<Column>(Column.class));
			}
		}
	}
	
	private static List<Model> parseModelFromTable(List<Column> columns){
		return null;
	}
}
