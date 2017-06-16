package org.ithang.tools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.ithang.Model;
import org.ithang.tools.lang.StrUtils;
import org.ithang.tools.mate.Column;
import org.ithang.tools.mate.Ignore;
import org.ithang.tools.mate.Primary;
import org.ithang.tools.mate.Table;

public final class ModelTools {

	private final static Random r=new Random(System.currentTimeMillis());
	
	private final static String[] tableLabels=new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","X","Y","Z"};
	
	private final static Logger logger=Logger.getLogger(ModelTools.class);
	/**
	 * 解析class对象，获得所有字段属性的相关信息
	 * @param cls
	 * @return List<Model> 每个字段对应一个Model对象
	 */
	public static List<Model> parseCls(Class<?> cls){
		String tableName=null;
		String tableLabel=null;
		Model m=null;
		Field[] fields=null;
		List<Model> mds=null;
		try{
			tableName=getTableName(cls);
			//为表创建一个别名,用于字段前缀
			tableLabel=tableLabels[r.nextInt(tableLabels.length)]+String.valueOf(r.nextInt(36));
			fields=cls.getDeclaredFields();
			
			if(null==fields||0==fields.length){
			    logger.error("无效Class对象,得不到任何字段信息!");
			    return null;
			}
			
			mds=new ArrayList<Model>(fields.length);
			for(Field field:fields){
					
			    //不处理序列化字段
			    if("serialVersionUID".equals(field.getName())){
			        continue;
			    }
					
			    //如果存在ignore注解，则表示不处理该字段，该字段不对应表中任何列
			    if(field.isAnnotationPresent(Ignore.class)){
			        continue;
			    }
					
                m=new Model();
                m.setTableName(tableName);
                m.setFieldName(field.getName());
                m.setFieldType(field.getType().getSimpleName());
                m.setTableLabel(tableLabel);
                
                //判断是否存在column注解，如果存在则用注解值作为列名
                if(field.isAnnotationPresent(Column.class)){
                	Column column=field.getAnnotation(Column.class);
				    m.setColumnName(column.value());
				    if(StrUtils.isNotBlank(column.defaultValue())){
				    	m.setValue(column.defaultValue());
				    }else{
				    	m.setValue("NULL");
				    }
			    }else{
				    m.setColumnName(StrUtils.addUnderline(field.getName()));
				    m.setValue("NULL");
			    }
                    
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
                    
                mds.add(m);
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
			if(null!=table&&StrUtils.isNotBlank(table.value())){
				return table.value();
			}
		}
		return StrUtils.addUnderline(cls.getSimpleName());
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
			Object v=bean.getClass().getMethod("get"+StrUtils.headUpper(fieldName)).invoke(bean);
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
						columnNames[i++]=StrUtils.addUnderline(field.getName());	
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return columnNames;
	}
	
	/*public static void generBeanFromMySQL(Connection conn,String schema) throws SQLException{
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
	}*/
}
