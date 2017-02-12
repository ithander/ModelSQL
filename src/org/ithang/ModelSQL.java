package org.ithang;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ithang.tools.ModelTools;

/**
 * 用于生成SQL的唯一可用类
 * @author ithang
 *
 * @param <T>
 */
public class ModelSQL {

	private String tableName;
	private String tableLabel;
	private String[] columnNames;
	private String[] fieldNames;
	
	private String columnNameStr;
	private String fieldNameStr;
	
	private String fieldHodler;//例如 insert into Tab(a,b,c)values([?,?,?])
	private String fieldNameHolder;//例如 insert into Tab(a,b,c)values([:a,:b,:c])
	
	private String queryColumns;//带前缀的查询列
	
	private int fieldNum=0;
	private List<Model> models;
	
	private StringBuffer sb=null; //用于构建查询
	private boolean selected=false;//用于标示是否己构建查询
	
	private static Map<String,List<Model>> allTabs=new HashMap<String,List<Model>>(50);
	
	public ModelSQL(Class<?> cls){
		sb=new StringBuffer();
		if(null!=allTabs.get(cls.getSimpleName())){
			models=allTabs.get(cls.getSimpleName());
		}else{
			models=ModelTools.parseCls(cls);
			allTabs.put(cls.getSimpleName(), models);
		}
		
		if(null!=models&&models.size()>0){
			fieldNum=models.size();
			tableName=models.get(0).getTableName();
			tableLabel=models.get(0).getTableLabel();
			columnNames=new String[fieldNum];
			fieldNames=new String[fieldNum];
			int i=0;
			StringBuilder sber=new StringBuilder();//构建占位符字符串
			StringBuilder sfer=new StringBuilder();//构建还前缀的查询列
			for(Model md:models){
				fieldNames[i]=md.getFieldName();
				columnNames[i]=md.getColumnName();
				if(0!=i++){
					sber.append(",");
					sfer.append(",");
				}
				sber.append("?");
				sfer.append(md.getTableLabel()).append(".").append(md.getColumnName());
			}
			fieldNameStr=ModelTools.pkg(fieldNames, ":", ",");
			fieldNameStr=fieldNameStr.substring(0,fieldNameStr.length()-1);
			
			columnNameStr=ModelTools.pkg(fieldNames, "", ",");
			columnNameStr=columnNameStr.substring(0,columnNameStr.length()-1);
			fieldHodler=sber.toString();
			fieldNameHolder=ModelTools.pkg(fieldNames, ":", ",");
			fieldNameHolder=fieldNameHolder.substring(0,fieldNameHolder.length()-1);
			queryColumns=sfer.toString();
		}
	}
	
	/**
	 * 生成insert sql 不带参数值
	 * @param byName 是否字段名做为参数
	 * @return
	 */
	public String insertSQL(boolean byName){
		StringBuilder sber=new StringBuilder();
		sber.append("insert into ").append(tableName);
		sber.append("(").append(columnNameStr).append(") values (");
		if(byName){
			sber.append(fieldNameHolder);
		}else{
			sber.append(fieldHodler);	
		}
		sber.append(")");
		return sber.toString();
	}
	
	/**
	 * 根据实体Bean生成insert sql
	 * @param bean
	 * @return
	 */
	public String insertSQL(Object bean){
		StringBuilder sber=new StringBuilder();
		sber.append("insert into ").append(tableName);
		sber.append("(").append(columnNameStr).append(") values (");
		
		for(int i=0;i<fieldNum;i++){
			if(0!=i){
				sber.append(",");
			}
			sber.append(getModel(fieldNames[i]).getFieldValue(bean));
		}
		
		sber.append(")");
		return sber.toString();
	}
	
	/**
	 * 根据map生成insert sql
	 * @param values 以fieldName为键，以fieldValue为值的键值对
	 * @return
	 */
	public String insertSQL(Map<String,Object> values){
		StringBuilder sber=new StringBuilder();
		sber.append("insert into ").append(tableName);
		sber.append("(");
		int i=0;
		for(String fieldName:fieldNames){
			if(values.containsKey(fieldName)){
				if(0!=i++){
					sber.append(",");
				}
				sber.append(getModel(fieldName).getColumnName());
			}
		}
		sber.append(") values (");
		i=0;
		for(String fieldName:fieldNames){
			if(values.containsKey(fieldName)){
				if(0!=i++){
					sber.append(",");
				}
				Model m=getModel(fieldName);
				sber.append(m.getFieldValue(values));
			}
		}
		sber.append(")");
		return sber.toString();
	}
	
	/**
	 * 生成update sql
	 */
	public String updateSQL(boolean byName){
		StringBuilder sber=new StringBuilder();
		sber.append("update ").append(tableName).append(" set ");
		StringBuilder where=new StringBuilder(" where ");
		int k=0;
		int f=0;
		for(int i=0;i<fieldNum;i++){
			if(getModel(fieldNames[i]).isPrimary()){
				if(f++>0){
					where.append(" and ");
				}
				where.append(columnNames[i]).append("=");
				if(byName){
					where.append(":").append(fieldNames[i]);
				}else{
					where.append("?");
				}
			}else{
				if(k++>0){
					sber.append(",");
				}
				sber.append(columnNames[i]).append("=");
				if(byName){
					sber.append(":").append(fieldNames[i]);
				}else{
					sber.append("?");
				}
			}
		}
		sber.append(where.toString());
		return sber.toString();
	}
	
	/**
	 * 
	 * @param bean
	 * @return
	 */
	public String updateSQL(Object bean){
		StringBuilder sber=new StringBuilder();
		sber.append("update ").append(tableName).append(" set ");
		StringBuilder where=new StringBuilder(" where ");
		int k=0;
		int f=0;
		for(int i=0;i<fieldNum;i++){
			Model m=getModel(fieldNames[i]);
			if(m.isPrimary()){
				if(f++>0){
					where.append(" and ");
				}
				where.append(columnNames[i]).append("=").append(m.getFieldValue(bean));
			}else{
				if(k++>0){
					sber.append(",");
				}
				sber.append(columnNames[i]).append("=").append(m.getFieldValue(bean));
			}
		}
		sber.append(where.toString());
		return sber.toString();
	}
	
	/**
	 * 根据map生成update sql
	 * @param values
	 * @return
	 */
	public String updateSQL(Map<String,Object> values){
		StringBuilder sber=new StringBuilder();
		sber.append("update ").append(tableName).append(" set ");
		StringBuilder where=new StringBuilder(" where ");
		int k=0;
		int f=0;
		for(int i=0;i<fieldNum;i++){
			if(!values.containsKey(fieldNames[i])){
				continue;
			}
			Model m=getModel(fieldNames[i]);
			if(m.isPrimary()){
				if(f++>0){
					where.append(" and ");
				}
				where.append(columnNames[i]).append("=").append(m.getFieldValue(values));
			}else{
				if(k++>0){
					sber.append(",");
				}
				sber.append(columnNames[i]).append("=").append(m.getFieldValue(values));
			}
		}
		sber.append(where.toString());
		return sber.toString();
	}
	
	/**
	 * 
	 * @param byName
	 * @return
	 */
	public String deleteSQL(boolean byName){
		StringBuilder sber=new StringBuilder("delete from ");
		sber.append(tableName).append(" where ");
		Model[] ms=getPrimaryModel();
		int i=0;
		for(Model m:ms){
			if(i++>0){
				sber.append(" and ");
			}
			sber.append(m.getColumnName()).append("=");
			if(byName){
				sber.append(":").append(m.getFieldName());
			}else{
				sber.append("?");
			}
		}
	    return sber.toString();	
	}
	
	/**
	 * 批量删除,适合只有一个主键字段
	 * @param ids
	 * @return
	 */
	public String deleteSQL(String...ids){
		StringBuilder sber=new StringBuilder("delete from ");
		sber.append(tableName).append(" where ");
		Model[] ms=getPrimaryModel();
		if(null!=ids&&ids.length>0){
			if(ids.length>1){
				int i=0;
				for(Model m:ms){
					if(i++>0){
						sber.append(" and ");
					}
					sber.append(m.getColumnName()).append(" in (");
					if("String".equals(m.getFieldType())){
						sber.append(ModelTools.pkg(ids,"'","',"));
					}else{
						sber.append(ModelTools.pkg(ids,"",","));
					}
					sber.delete(sber.length()-1, sber.length());
					sber.append(")");
				}
			}else{
				sber.append(ms[0].getColumnName()).append("=");
				if("String".equals(ms[0].getFieldType())){
					sber.append("'").append(ids[0]).append("'");
				}else{
					sber.append(ids[0]);
				}
			}
		}else{
			return deleteSQL(false);
		}
		
		return sber.toString();
	}
	
	/**
	 * 根据条件删除
	 * @param values
	 * @return
	 */
	public String deleteSQL(Map<String,Object> values){
		StringBuilder sber=new StringBuilder("delete from ");
		sber.append(tableName).append(" where ");
		int i=0;
		for(Model m:models){
			if(values.containsKey(m.getFieldName())){
				if(i++>0){
					sber.append(" and ");
				}
				sber.append(m.getColumnName()).append("=").append(m.getFieldValue(values));
			}
		}
		return sber.toString();
	}
	
	/**
	 * 查询起步
	 * @return modelSQL 在其础上进行灵活配置
	 */
	public ModelSQL select(){
		if(!selected){
			sb.delete(0, sb.length());
			sb.append("select ");
			sb.append(queryColumns).append(" from ").append(tableName).append(" ").append(tableLabel);
		}
		return this;
	}
	
	/**
	 * 简单分页查询,相当于select+limit
	 * @param from
	 * @param size
	 * @return
	 */
	public String listSQL(int from,int size){
		return select().limit(from, size);
	}
	
	/**
	 * 简单综合查询,相当于select+where
	 * @param values
	 * @return
	 */
	public String listSQL(Map<String,Object> values){
		return select().where(values).toSQL();
	}
	
	/**
	 * 简单综合查询
	 * @param values
	 * @return
	 */
	public ModelSQL where(Map<String,Object> values){
		if(!selected){
			select();
		}
		if(null!=values&&values.size()>0){
			sb.append(" where ");
			int i=0;
			for(Model m:models){
				if(values.containsKey(m.getFieldName())){
					if(i++>0){
						sb.append(" and ");
					}
					sb.append(tableLabel).append(".").append(m.getColumnName()).append("=").append(m.getFieldValue(values));
				}
			}
			if(0==i){
				sb.append(" 1=1");
			}
		}
		
		return this;
	}
	
	public Limit ascGroupBy(String..._columnNames){
		if(!selected){
			select();
		}
		if(null!=_columnNames&&_columnNames.length>0){
			sb.append(" group by ");
			for(String cn:_columnNames){
				sb.append(tableLabel).append(".").append(cn).append(",");
			}
			sb.delete(sb.length()-1, sb.length());
			sb.append(" asc ");
		}
		return new Limit(sb);
	}
	
	public Limit descGroupBy(String..._columnNames){
		if(!selected){
			select();
		}
		if(null!=_columnNames&&_columnNames.length>0){
			sb.append(" group by ");
			for(String cn:_columnNames){
				sb.append(tableLabel).append(".").append(cn).append(",");
			}
			sb.delete(sb.length()-1, sb.length());
			sb.append(" desc ");
		}
		return new Limit(sb);
	}
	
	/**
	 * 分页
	 * @param from
	 * @param size
	 * @return
	 */
	public String limit(int from,int size){
		if(!selected){
			select();
		}
		sb.append(" limit ").append(from).append(",").append(size);
		return sb.toString();
	}
	
	public ON leftJoin(ModelSQL _ms){
		if(!selected){
			select();
		}
		sb.insert(sb.indexOf("from")-1,","+_ms.getQureyColumns());
		sb.append(" left join ").append(_ms.getTableName()).append(" ").append(_ms.getTableLabel());
		return new ON(this,_ms);
	}
	
	public ON innerJoin(ModelSQL _ms){
		if(!selected){
			select();
		}
		sb.insert(sb.indexOf("from")-1,","+_ms.getQureyColumns());
		sb.append(" inner join ").append(_ms.getTableName()).append(" ").append(_ms.getTableLabel());
		return new ON(this,_ms);
	}
	
	public String getQureyColumns(){
		return queryColumns;
	}
	
	public String getTableName(){
		return tableName;
	}
	
	public String getTableLabel(){
		return tableLabel;
	}
	
	public String toSQL(){
		return sb.toString();
	}
	
	private Model[] getPrimaryModel(){
		Model[] ms=new Model[fieldNum];
		int i=0;
		for(Model m:models){
			if(m.isPrimary()){
				ms[i++]=m;
			}
		}
		return Arrays.copyOf(ms, i);
	}
	
	private Model getModel(String fieldName){
		Model model=null;
		for(Model m:models){
			if(fieldName.equals(m.getFieldName())){
				model=m;
				break;
			}
		}
		return model;
	}
	
	protected StringBuffer getSQL(){
		return sb;
	}
	
	@Override
	public String toString() {
		return toSQL();
	}
	
}
