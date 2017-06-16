package org.ithang.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.ithang.tools.SQLParser;

/**
 * 这个dao可以从xml中读取SQL语句
 * @author ithang
 *
 */
public class XBaseDao extends BaseDao {

	private static Logger logger=Logger.getLogger(BaseDao.class);
	/**
	 * 执行 新增，修改，删除 SQL
	 * @param namespace
	 * @param values
	 */
	public void updateXSQL(String namespace,Map<String,Object> values){
		logger.debug("修改操作");
		updateSQL(SQLParser.getSQL(namespace, values), values);
	}
	
	
	/**
	 * 执行 查询 SQL
	 * @param namesapce
	 * @param values
	 * @return
	 */
	public Map<String,Object> getXSQL(String namesapce,Map<String,Object> values){
		List<Map<String,Object>> rs=listSQL(SQLParser.getSQL(namesapce, values), values);
		if(null!=rs&&rs.size()>0){
			return rs.get(0);
		}
		return null;
	}
	
	/**
	 * 执行 查询 SQL
	 * @param namesapce
	 * @param values
	 * @return
	 */
	public List<Map<String,Object>> listXSQL(String namesapce,Map<String,Object> values){
		return listSQL(SQLParser.getSQL(namesapce, values), values);
	}
	
}
