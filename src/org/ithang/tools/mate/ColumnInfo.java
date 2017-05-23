package org.ithang.tools.mate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnInfo {

	public boolean isNULL() default true;//true 可为空,false不可为空
	
	public boolean autoIncrement() default false;//是否自增长
	
	public String type();//列类型
	
	public int len();//字符长度或数字精度
	
	public String value() default "";//列默认值
	
}
