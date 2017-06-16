package org.ithang.dao;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import org.apache.log4j.Logger;
import org.ithang.ModelSQL;

public class ModelDao<T> extends BaseDao {

	
	private ModelSQL<T> modelSQL=null;
	private Class<T> modelCls=null;
	
	private Logger logger=Logger.getLogger(ModelDao.class);
	
	@SuppressWarnings("unchecked")
	public ModelDao(){
		 this.modelCls=(Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		 modelSQL=new ModelSQL<T>(modelCls);
		 logger.debug("init ModelDao about of "+modelCls);
	}
	
	public int insert(Map<String,Object> values){
		return updatesSQL(modelSQL.insertSQL(values));
	}
	
	public int update(Map<String,Object> values){
		return updatesSQL(modelSQL.updateSQL(values));
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public int delete(String... ids){
		return updatesSQL(modelSQL.deleteSQL(ids));
	}
	
	public int delete(Map<String,Object> values){
		return updatesSQL(modelSQL.deleteSQL(values));
	}
	
	/*
	public T get(Map<String,Object> values){
		List<T> rs=query(values);
		if(null!=rs&&rs.size()>0){
			return rs.get(0);
		}
		return null;
	}*/
	
	
	public Class<T> getModelCls() {
		return modelCls;
	}


	/**
	 * 单表查询，把结果封装到业务实体中，可以进行分页查询
	 * @param values
	 * @param beanCls
	 * @return
	 
	public List<T> query(Map<String,Object> values){
		String sql=modelSQL.listSQL(values);
		logger.debug("执行SQL:"+sql);
		logger.debug("执行参数:"+values==null?"":JsonUtils.toJsonStr(values));
		List<T> rs=null;
		try {
			rs=getQueryRunner().query(sql, new BeanListHandler<T>(modelCls));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}*/
	
}
