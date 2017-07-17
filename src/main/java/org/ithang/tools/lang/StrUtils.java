package org.ithang.tools.lang;

public final class StrUtils {

	/**
     * 是非空有效字段吗?是true,否false
     * @param value
     * @return 
     */
    public static boolean isNotBlank(String value){
        if(null!=value&&value.trim().length()>0){
            return true;
        }
        return false;
    }
	
    public static String trim(String str,String mark){
		if(str.startsWith(mark)){
			str=str.replaceFirst(mark, "");
		}
		if(str.endsWith(mark)){
			str=str.substring(0, str.lastIndexOf(mark));
		}
		
		return str;
	}
    
    public static boolean isEmptyArray(Object[] array){
    	if(null!=array&&array.length>0){
    		return true;
    	}
    	return false;
    }
    
    /**
	 * 增加下划线,把驼峰式转到下划线式
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
	 * 去前缀
	 * @param value
	 * @param prefix
	 * @param suffix
	 * @return
	 */
	public static String trimPrefix(String value,String ... prefixs){
		if(isNotBlank(value)){
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
	 * 为每个字符串加前后缀
	 * @param values
	 * @param prefix
	 * @param suffix
	 * @return
	 */
	public static String pkg(String[] values,String prefix,String suffix){
		StringBuilder sber=new StringBuilder();
		for(String value:values){
			if(isNotBlank(value)){
				sber.append(prefix).append(value).append(suffix);	
			}
		}
		return sber.toString();
	}
    
}
