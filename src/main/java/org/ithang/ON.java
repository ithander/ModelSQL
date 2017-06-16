package org.ithang;

@Deprecated
public class ON<T> {

	private String atLabel;
	private String btLabel;
	private ModelSQL<T> host;
	public ON(ModelSQL<T> _host,String btLabel){
		this.host=_host;
		this.atLabel=host.getTableLabel();
		this.btLabel=btLabel;
		host.getSQL().append(" on ");
	}
	
	public ModelSQL<T> on(String afieldName,String bfieldName){
		host.getSQL().append(atLabel).append(".").append(afieldName);
		host.getSQL().append("=").append(btLabel).append(".").append(bfieldName);
		return host;
	}
	
	
}
