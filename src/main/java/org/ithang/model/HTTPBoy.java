package org.ithang.model;

import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class HTTPBoy {

	public final static String CT_Plain="text/plain";
	public final static String CT_HTML="text/html";
	public final static String CT_XML="text/xml";
	public final static String CT_CSS="text/css";
	public final static String CT_Class="java/*";
	public final static String CT_PDF="application/pdf";
	public final static String CT_Torrent="application/x-bittorrent";
	public final static String CT_PPT="application/x-ppt";
	public final static String CT_MP4="video/mpeg4";
	public final static String CT_APK="application/vnd.android.package-archive";
	/**
	 *针对未知数据，二进制流数据 
	 */
	public final static String CT_OctetStream="application/octet-stream";
	
	private URL url=null;
	private URLConnection urlc=null;
	
	private HTTPBoy(URL url){
		setUrl(url);
	}
	
	public static HTTPBoy get(String url){
		try{
			URL _url=new URL(url);
			HTTPBoy hboy=new HTTPBoy(_url);
			hboy.setUrlc(_url.openConnection());
			return hboy;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static HTTPBoy get(String url,Map<String,Object> params){
		try{
			URL _url=new URL(url);
			HTTPBoy hboy=new HTTPBoy(_url);
			hboy.setUrlc(_url.openConnection());
			return hboy;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static HTTPBoy post(String url,String param_name,String param_value){
		try{
			URL _url=new URL(url);
			HTTPBoy hboy=new HTTPBoy(_url);
			hboy.setUrlc(_url.openConnection());
			return hboy;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static HTTPBoy post(String url,Map<String,Object> params){
		try{
			URL _url=new URL(url);
			HTTPBoy hboy=new HTTPBoy(_url);
			hboy.setUrlc(_url.openConnection());
			return hboy;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private HTTPBoy contentType(String contentType){
		urlc.setRequestProperty("Content-Type",contentType);
		return this;
	}
	
	private HTTPBoy encode(String encode){
		urlc.setRequestProperty("Charset", encode);
		return this;
	}
	
	private HTTPBoy decode(String decode){
		urlc.setRequestProperty("Accept-Charset", decode);
		return this;
	}
	
	private HTTPBoy setRequestProperty(String key,String value){
		urlc.setRequestProperty(key, value);
		return this;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public URLConnection getUrlc() {
		return urlc;
	}

	public void setUrlc(URLConnection urlc) {
		this.urlc = urlc;
	}
	
}
