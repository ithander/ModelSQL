package org.ithang.tools;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import freemarker.template.Configuration;

/**
 * 解析xml里的sql
 * @author ithang
 *
 */
public class SQLParser {

	private static Configuration cfg=null;
	private static Logger logger=Logger.getLogger(SQLParser.class);
    static{
		cfg=new Configuration(Configuration.VERSION_2_3_23);
	    cfg.setEncoding(Locale.CHINA, "UTF-8");
	    cfg.setTemplateLoader(new SQLLoader());
	    cfg.setOutputEncoding("UTF-8");
	    logger.debug("init freemarker engine ...");
    }
	
	public static String getSQL(String namespace_id,Map<String,Object> values){
		try{
			Writer out = new StringWriter(32);
		    cfg.getTemplate(namespace_id).process(values, out);
		    String sql=out.toString();
		    out.close();
		    return sql;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
}
