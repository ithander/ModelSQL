package org.ithang;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 主要工具类,功能:根据实体类生成SQL
 * @author ithang
 *
 */
public class MSQL {

	//private static boolean cache=false;//是否缓存解析实体类信息 (该功能要主用于生产环境,业务实体类不变的情况下,用于提高解析效率)
	private static Map<String,Mod> modelFields=new ConcurrentHashMap<>(50);
	
	
	public static String select(Class<?> cls,String ...fieldNames){
		StringBuilder sb=new StringBuilder();
    	Mod md=getMod(cls);
    	sb.append("select ");
    	
    	if(null!=fieldNames&&fieldNames.length>0){
    		int i=0;
    		for(String fieldName:fieldNames){
    			if(i>0){
    				sb.append(" , ");
    			}
    			sb.append(md.getColumnName(fieldName));
    			i++;
    		}
    	}else{
    		String[] columnNames=md.getColumnNames();
    		if(null!=columnNames){
    			int i=0;
    			for(String cm:columnNames){
    				if(i>0){
    					sb.append(",");
    				}
        			sb.append(cm);
        			i++;
        		}
    		}
    	}
    	sb.append(" from ").append(md.getTableName());
		return sb.toString();
	}
	
	public static String insert(Class<?> cls){
		StringBuilder sb=new StringBuilder();
    	Mod md=getMod(cls);
    	sb.append("insert into ").append(md.getTableName());
    	String[] columnNames=md.getColumnNames();
    	String[] fieldNames=md.getFieldNames();
    	if(null!=columnNames){
    		int i=0;
    		sb.append("(");
			for(String cm:columnNames){
				if(i>0){
					sb.append(",");
				}
    			sb.append(cm);
    			i++;
    		}
			sb.append(")");
    	}
    	sb.append("values");
    	if(null!=fieldNames){
    		int i=0;
    		sb.append("(");
			for(String cm:fieldNames){
				if(i>0){
					sb.append(",");
				}
    			sb.append(":").append(cm);
    			i++;
    		}
			sb.append(")");
    	}
		return sb.toString();
	}
	
    public static String update(Class<?> cls,String ...fieldNames){
    	StringBuilder sb=new StringBuilder();
    	Mod md=getMod(cls);
    	String[] primaryFieldNames=md.getPrimaryFieldNames();
    	sb.append("update ").append(md.getTableName());
    	if(null==fieldNames){
    		fieldNames=md.getFieldNames();
    	}
    	int i=0;
		sb.append(" set ");
		for(String fieldName:fieldNames){
			
			for(String pfn:primaryFieldNames){
				if(fieldName==pfn){
					continue;
				}
			}
			
			if(i>0){
				sb.append(" , ");
			}
			sb.append(md.getColumnName(fieldName)).append("=:").append(fieldName);
			i++;
		}
		
		sb.append(" where ");
		i=0;
		boolean named=primaryFieldNames.length>2?true:false;
		for(String fieldName:primaryFieldNames){
			if(i>0){
				sb.append(" and ");
			}
			sb.append(fieldName);
			if(named){
				sb.append("=:").append(md.getColumnName(fieldName));
			}else{
			    sb.append("=?");
			}
			i++;
		}
    	return sb.toString();
    }
    
    public static String delete(Class<?> cls){
    	StringBuilder sb=new StringBuilder();
    	Mod md=getMod(cls);
    	String[] primaryFieldNames=md.getPrimaryFieldNames();
    	
    	sb.append("delete from ").append(md.getTableName());
    	
    	if(null!=primaryFieldNames&&primaryFieldNames.length>0){
    		sb.append(" where ");
    		boolean named=primaryFieldNames.length>1?true:false;
    		int i=0;
    		for(String fieldName:primaryFieldNames){
    			if(i>0){
    				sb.append(" and ");
    			}
    			sb.append(md.getColumnName(fieldName));
    			if(named){
    				sb.append("=:").append(md.getColumnName(fieldName));
    			}else{
    			    sb.append("=?");
    			}
    			i++;
    		}
    	}
    	
    	return sb.toString();
    }
    
    private static Mod getMod(Class<?> cls){
    	if(null!=modelFields){
    		if(modelFields.containsKey(cls.getName())){
    			return modelFields.get(cls.getName());	
    		}else{
    			Mod mod=new Mod(cls);
    			modelFields.put(cls.getName(), mod);
    			return mod;
    		}
    	}
    	return new Mod(cls);
    }
	
}
