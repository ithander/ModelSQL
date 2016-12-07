package org.ithang;

public class ON {

	private ModelSQL host;
	private ModelSQL dest;
	
	public ON(ModelSQL ht,ModelSQL dt){
		host=ht;
		dest=dt;
	}
	
	public ModelSQL on(String afieldName,String bfieldName){
		host.getSQL().append(" on ").append(host.getTableLabel()).append(".").append(afieldName);
		host.getSQL().append("=").append(dest.getTableLabel()).append(".").append(bfieldName);
		return host;
	}
	
	/**
	 * fieldNames[偶数]=fieldNames[奇数]
	 * @param fieldNames
	 * @return
	 */
	public ModelSQL on(String...fieldNames){
		int len=fieldNames.length;
		if(0==len%2){
			host.getSQL().append(" on ");
			for(int i=0;i<len;i+=2){
				if(i>0){
					host.getSQL().append(" and ");
				}
				host.getSQL().append(host.getTableLabel()).append(".").append(fieldNames[i]);
				host.getSQL().append("=").append(dest.getTableLabel()).append(".").append(fieldNames[i+1]);
			}
		}
	    return host;	
	}
	
}
