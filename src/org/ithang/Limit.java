package org.ithang;

public class Limit {

	private StringBuffer sb=null;
	
	public Limit(StringBuffer _sb){
		sb=_sb;
	}
	/**
	 * 分页
	 * @param from
	 * @param size
	 * @return
	 */
	public String limit(int from,int size){
		sb.append(" limit ").append(from).append(",").append(size);
		return sb.toString();
	}

	public String toSQL(){
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return sb.toString();
	}
	
	
}
