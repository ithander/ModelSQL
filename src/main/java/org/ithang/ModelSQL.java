package org.ithang;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ithang.tools.ModelTools;
import org.ithang.tools.lang.StrUtils;

/**
 * 用于生成SQL的唯一可用类
 * @author ithang
 *
 * @param <T>
 */
public class ModelSQL<T> {

	private String tableName;//表名称
	private String tableLabel;//表别名 在ModelTools中随机出来
	private String[] columnNames;//所有表列名称
	private String[] fieldNames;//所有实体类字段名称
	private String[] columnNames_noid;//所有表列名称,不包括主键
	private String[] fieldNames_noid;//所有实体类字段名称,不包括主键
	
	private String columnNameStr;//所有列名称以逗号链接的字符串
	private String fieldNameStr;//所有字段名称以逗号链接的字符串
	private String columnNameStr_noid;//所有列名称以逗号链接的字符串,不包括主键
	private String fieldNameStr_noid;//所有字段名称以逗号链接的字符串,不包括主键
	
	private String fieldHodler;//例如 insert into Tab(a,b,c)values([?,?,?])
	private String fieldNameHolder;//例如 insert into Tab(a,b,c)values([:a,:b,:c])
	private String fieldHodler_noid;//例如 insert into Tab(a,b,c)values([?,?,?]),不包括主键
	private String fieldNameHolder_noid;//例如 insert into Tab(a,b,c)values([:a,:b,:c]),不包括主键
	
	private String queryColumns;//带前缀的查询列
	
	private int fieldNum=0;
	private List<Model> models;
	
	private StringBuilder sb=null; //用于构建查询
	private boolean selected=false;//用于标示是否己构建查询
	private Class<T> prototype=null;//封装类型
	private static Map<String,List<Model>> allTabs=new HashMap<String,List<Model>>(50);
	
	public ModelSQL(Class<T> cls){
		setPrototype(cls);
		sb=new StringBuilder();
		if(null!=allTabs.get(cls.getSimpleName())){//如果缓存中存在，则用缓存
			models=allTabs.get(cls.getSimpleName());
		}else{//如果缓存中不存在，则解析
			models=ModelTools.parseCls(cls);
			allTabs.put(cls.getSimpleName(), models);
		}
		
		if(null!=models&&models.size()>0){
			fieldNum=models.size();
			tableName=models.get(0).getTableName();
			tableLabel=models.get(0).getTableLabel();
			columnNames=new String[fieldNum];
			fieldNames=new String[fieldNum];
			
			columnNames_noid=new String[fieldNum];
			fieldNames_noid=new String[fieldNum];
			
			int i=0;
			int ii=0;
			StringBuilder sber=new StringBuilder();//构建占位符字符串
			StringBuilder sber_noid=new StringBuilder();//构建占位符字符串
			
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
				
				
				if(!md.isPrimary()){
					fieldNames_noid[ii]=md.getFieldName();
					columnNames_noid[ii]=md.getColumnName();
					if(0!=ii++){
						sber_noid.append(",");
					}
					sber_noid.append("?");
				}
			}
			fieldNameStr=StrUtils.pkg(fieldNames, ":", ",");
			fieldNameStr=fieldNameStr.substring(0,fieldNameStr.length()-1);
			
			fieldNameStr_noid=StrUtils.pkg(fieldNames_noid, ":", ",");
			fieldNameStr_noid=fieldNameStr_noid.substring(0,fieldNameStr_noid.length()-1);
			
			columnNameStr=StrUtils.pkg(columnNames, "", ",");
			columnNameStr=columnNameStr.substring(0,columnNameStr.length()-1);
			
			columnNameStr_noid=StrUtils.pkg(columnNames_noid, "", ",");
			columnNameStr_noid=columnNameStr_noid.substring(0,columnNameStr_noid.length()-1);
			
			fieldHodler=sber.toString();
			fieldNameHolder=StrUtils.pkg(fieldNames, ":", ",");
			fieldNameHolder=fieldNameHolder.substring(0,fieldNameHolder.length()-1);
			
			fieldHodler_noid=sber.toString();
			fieldNameHolder_noid=StrUtils.pkg(fieldNames_noid, ":", ",");
			fieldNameHolder_noid=fieldNameHolder_noid.substring(0,fieldNameHolder_noid.length()-1);
			
			queryColumns=sfer.toString();
		}
	}
	
	/**
	 * 生成insertSQL,默认byName=false;includeID=true
	 * @return
	 */
	public String insertSQL(){
		return insertSQL(false,true);
	}
	
	/**
	 * 生成insert sql 不带参数值
	 * @param byName 是否字段名做为参数
	 * @return
	 */
	public String insertSQL(boolean byName){
		return insertSQL(byName,true);
	}
	
	/**
	 * 生成insert语句
	 * @param byName 是否能过字段匹配参数,true是,false否
	 * @param includeID 是否包括ID字段
	 * @return
	 */
	public String insertSQL(boolean byName,boolean includeID){
		StringBuilder sber=new StringBuilder();
		sber.append("insert into ").append(tableName);
		sber.append("(");
		if(includeID){
		    sber.append(columnNameStr);
		}else{
			sber.append(columnNameStr_noid);
		}
		
		sber.append(") values (");
		
		if(byName){
			if(includeID){
			    sber.append(fieldNameHolder);
			}else{
				sber.append(fieldNameHolder_noid);
			}
		}else{
			if(includeID){
			    sber.append(fieldHodler);
			}else{
				sber.append(fieldHodler_noid);
			}
		}
		sber.append(")");
		return sber.toString();
	}
	
	
	/**
	 * 根据实体Bean生成insert sql
	 * @param bean
	 * @return
	 */
	public String insertSQL(T bean){
		StringBuilder sber=new StringBuilder();
		sber.append("insert into ").append(tableName);
		sber.append("(").append(columnNameStr).append(") values (");
		
		for(int i=0;i<fieldNum;i++){
			if(0!=i){
				sber.append(",");
			}
			sber.append(getModel(fieldNames[i]).getBeanFieldValue(bean));
		}
		
		sber.append(")");
		return sber.toString();
	}
	
	/**
	 * 根据map生成insert sql
	 * @param map 以fieldName为键，以fieldValue为值的键值对
	 * @return
	 */
	public String insertSQL(Map<String,Object> map){
		StringBuilder sber=new StringBuilder();
		sber.append("insert into ").append(tableName);
		sber.append("(");
		int i=0;
		for(String fieldName:fieldNames){
			if(map.containsKey(fieldName)){
				if(0!=i++){
					sber.append(",");
				}
				sber.append(getModel(fieldName).getColumnName());
			}
		}
		sber.append(") values (");
		i=0;
		for(String fieldName:fieldNames){
			if(map.containsKey(fieldName)){
				if(0!=i++){
					sber.append(",");
				}
				Model m=getModel(fieldName);
				sber.append(m.getMapFieldValue(map));
			}
		}
		sber.append(")");
		return sber.toString();
	}
	
	
	public String insertMapsSQL(List<Map<String,Object>> maps){
		StringBuilder sber=new StringBuilder();
		sber.append("insert into ").append(tableName);
		sber.append("(").append(columnNameStr).append(") values ");
		
		maps.forEach(mp->{
			int i=0;
			for(String fieldName:fieldNames){
				if(0!=i++){
					sber.append(",");
				}else{
					sber.append("(");
				}
				Model m=getModel(fieldName);
				sber.append(m.getMapFieldValue(mp));
			}
			if(fieldNum==i){
				sber.append("),");	
			}
		});
		return sber.delete(sber.length()-1, sber.length()).toString();
	}
	
	public String insertBeansSQL(List<T> beans){
		StringBuilder sber=new StringBuilder();
		sber.append("insert into ").append(tableName);
		sber.append("(").append(columnNameStr).append(") values ");
		
		beans.forEach(bean->{
			int i=0;
			for(;i<fieldNum;i++){
				if(0!=i++){
					sber.append(",");
				}else{
					sber.append("(");
				}
				sber.append(getModel(fieldNames[i]).getBeanFieldValue(bean));
			}
			if(fieldNum==i){
				sber.append("),");	
			}
		});
		return sber.delete(sber.length()-1, sber.length()).toString();
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
				where.append(columnNames[i]).append("=").append(m.getBeanFieldValue(bean));
			}else{
				if(k++>0){
					sber.append(",");
				}
				sber.append(columnNames[i]).append("=").append(m.getBeanFieldValue(bean));
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
				where.append(columnNames[i]).append("=").append(m.getMapFieldValue(values));
			}else{
				if(k++>0){
					sber.append(",");
				}
				sber.append(columnNames[i]).append("=").append(m.getMapFieldValue(values));
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
						sber.append(StrUtils.pkg(ids,"'","',"));
					}else{
						sber.append(StrUtils.pkg(ids,"",","));
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
				sber.append(m.getColumnName()).append("=").append(m.getMapFieldValue(values));
			}
		}
		return sber.toString();
	}
	
	/**
	 * 查询起步
	 * @return modelSQL 在其础上进行灵活配置
	 */
	public ModelSQL<T> select(){
		if(selected){//如果己构建则删除，因为可能包含了where join 等子句
			sb.delete(0, sb.length());
		}
		sb.append("select ");
		sb.append(queryColumns).append(" from ").append(tableName).append(" ").append(tableLabel);
		selected=true;
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
		return select().where(values).toString();
	}
	
	/**
	 * 直接返回一个新构建的StringBuilder让使用者自己构建where
	 * @param columnName
	 * @param columnValue
	 * @return
	 */
	public StringBuilder where(String columnName,Object columnValue){
		if(!selected){//如果未生成select语句，则生成
			select();
		}
		sb.append(" where ").append(columnName).append("=");
		if(columnValue instanceof String){
			sb.append("'").append(columnValue).append("'");
		}else{
			sb.append(columnValue);
		}
		
		return sb;
	}
	
	/**
	 * 简单综合查询
	 * @param values
	 * @return
	 */
	public StringBuilder where(Map<String,Object> values){
		if(!selected){//如果未生成select语句，则生成
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
					sb.append(tableLabel).append(".").append(m.getColumnName()).append("=").append(m.getMapFieldValue(values));
				}
			}
			if(0==i){
				sb.append(" 1=1");
			}
		}
		
		return sb;
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
	
	public ModelSQL<T> leftJoin(ModelSQL<?> _ms,String acolumnName,String bcolumnName){
		if(!selected){
			select();
		}
		sb.insert(sb.indexOf("from")-1,","+_ms.getQureyColumns());
		sb.append(" left join ").append(_ms.getTableName()).append(" ").append(_ms.getTableLabel());
		sb.append(" on ");
		sb.append(" ").append(getTableLabel()).append(".").append(acolumnName).append("=").append(_ms.getTableLabel()).append(".").append(bcolumnName);
		return this;
	}
	
	public ModelSQL<T> leftJoin(ModelSQL<?> _ms,String[] acolumnNames,String[] bcolumnNames){
		if(!selected){
			select();
		}
		sb.insert(sb.indexOf("from")-1,","+_ms.getQureyColumns());
		sb.append(" left join ").append(_ms.getTableName()).append(" ").append(_ms.getTableLabel());
		sb.append(" on ");
		int len=acolumnNames.length>bcolumnNames.length?bcolumnNames.length:acolumnNames.length;
		for(int i=0;i<len;i++){
			sb.append(" ").append(getTableLabel()).append(".").append(acolumnNames[i]).append("=").append(_ms.getTableLabel()).append(".").append(bcolumnNames[i]);
		}
		return this;
	}
	
	public ModelSQL<T> innerJoin(ModelSQL<?> _ms,String acolumnName,String bcolumnName){
		if(!selected){
			select();
		}
		sb.insert(sb.indexOf("from")-1,","+_ms.getQureyColumns());
		sb.append(" inner join ").append(_ms.getTableName()).append(" ").append(_ms.getTableLabel());
	    sb.append(" ").append(getTableLabel()).append(".").append(acolumnName).append("=").append(_ms.getTableLabel()).append(".").append(bcolumnName);
		return this;
	}
	
	public ModelSQL<T> innerJoin(ModelSQL<?> _ms,String[] acolumnNames,String[] bcolumnNames){
		if(!selected){
			select();
		}
		sb.insert(sb.indexOf("from")-1,","+_ms.getQureyColumns());
		sb.append(" inner join ").append(_ms.getTableName()).append(" ").append(_ms.getTableLabel());
		int len=acolumnNames.length>bcolumnNames.length?bcolumnNames.length:acolumnNames.length;
		for(int i=0;i<len;i++){
			sb.append(" ").append(getTableLabel()).append(".").append(acolumnNames[i]).append("=").append(_ms.getTableLabel()).append(".").append(bcolumnNames[i]);
		}
		return this;
	}
	
	public String createTable(boolean delExist){
		StringBuilder sber=new StringBuilder();
		if(delExist){
			sber.append(" drop table `").append(tableName).append("` ;\n");
		}
		sber.append(" create table `").append(tableName).append("` ( \n");
		String primaryKey=null;
		for(Model model:models){
			if(model.isPrimary()){
				if(null==primaryKey){
					primaryKey="`"+model.getColumnName()+"`";	
				}else{
					primaryKey+=",`"+model.getColumnName()+"`";
				}
			}
			sber.append("\t\t\t`").append(model.getColumnName()).append("` ").append(model.getColumnType());
			if(model.getColumnLen()>0){
				sber.append("(").append(model.getColumnLen()).append(")");
			}
			
			//是否可为空
			if(model.isNULL()){
				sber.append(" NULL ");
			}else{
				sber.append(" NOT NULL ");
			}
			
			if(null!=model.getValue()){
				sber.append(" default ");
				if("varchar".equals(model.getColumnType())||model.getColumnType().toLowerCase().endsWith("text")){
					sber.append("'").append(model.getValue()).append("'");
				}else{
				    sber.append(model.getValue());
				}
			}
			if(model.isAutoIncrement()){
				sber.append(" AUTO_INCREMENT");
			}
			sber.append(",\n");
		}
		
		if(null!=primaryKey){
			sber.append("\t\t\tPRIMARY KEY (").append(primaryKey).append(")");
		}else{
			sber.delete(sber.length()-2, sber.length());
		}
		sber.append("\n\t)ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci;");
		return sber.toString();
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
	
	protected StringBuilder getSQL(){
		return sb;
	}
	
	@Override
	public String toString() {
		return toSQL();
	}

	public Class<T> getPrototype() {
		return prototype;
	}

	public void setPrototype(Class<T> prototype) {
		this.prototype = prototype;
	}
	
}
