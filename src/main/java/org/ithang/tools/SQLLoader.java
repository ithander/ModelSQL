package org.ithang.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import freemarker.cache.TemplateLoader;

public class SQLLoader implements TemplateLoader {

	
	private static Logger logger=Logger.getLogger(SQLLoader.class);
	private final static Map<String,String> templates=new HashMap<String,String>(60);
	
	static{
		File classDir=new File(SQLLoader.class.getResource("/").getPath());
		List<File> files=new ArrayList<File>();
		loadSQLFile(classDir,files);
		files.forEach(file->{
			loadTemplate(file);
		});
	}
	
	public static void loadSQLFile(File dir,List<File> sqlFiles){
		File[] files=dir.listFiles(new FileFilter(){

			@Override
			public boolean accept(File subDir) {
				if(subDir.isDirectory()){
					loadSQLFile(subDir,sqlFiles);
					return false;
				}
				if(subDir.getName().endsWith(".md")){
					return true;
				}
				return false;
			}
			
		});
		
		for(File f:files){
    		sqlFiles.add(f);
    	}
	}
	
	public static void loadTemplate(File sqlFile){
		try{
		    FileInputStream fis=new FileInputStream(sqlFile);
		    List<String> lines=IOUtils.readLines(fis, "UTF-8");
		    StringBuilder sber=new StringBuilder();
		    String key=null;
			for(String line:lines){
				if(line.startsWith("*")){
					continue;
				}
				if(line.startsWith("#")){
					if(null!=key){
						templates.put(key, sber.toString());
						sber.delete(0, sber.length());
					}
					key=line.replaceFirst("#", "").trim();
				}else{
					sber.append(line);				
				}
			}
			if(null!=key){
			    templates.put(key,sber.toString());
			}
			fis.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void closeTemplateSource(Object obj) throws IOException {
		if(null!=obj){
			((StringReader)obj).close();
		}
	}

	@Override
	public Object findTemplateSource(String namespace_id) throws IOException {
		logger.debug("Find SQL:"+namespace_id);
		namespace_id=namespace_id.replaceAll("_zh_CN", "");
		namespace_id=namespace_id.replaceAll("_en_US", "");
		return new StringReader(templates.get(namespace_id));
	}

	@Override
	public long getLastModified(Object obj) {
		return 0;
	}

	@Override
	public Reader getReader(Object obj, String encoding) throws IOException {
		return ((StringReader)obj);
	}

}
