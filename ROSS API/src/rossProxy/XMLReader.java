package rossProxy;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class XMLReader {
	private String content = "";
	private String filename = null;
	public XMLReader(String f) {
		filename = f;
		try {
	        BufferedReader in = new BufferedReader(new FileReader(filename));
	        String str="";;
	        while ((str = in.readLine()) != null) {
	            content+=str+"\n";
	        }
	        in.close();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	static public String getRossSpaceFromXML(String content) {
		int start = 0, end = content.length()-1;
		String str = null;
		if (content==null) return null;
		try {
			Matcher matcher1 = Pattern.compile("<rossXML>[\r\n]*").matcher(content);
			Matcher matcher2 = Pattern.compile("[\r\n]*</rossXML>").matcher(content);
			if (matcher1.find()) {
				start = matcher1.end();
			}
			if (matcher2.find()) {
				end = matcher2.start();
			}
			str = content.substring(start, end);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	static public String replaceIP (String content, Socket s) {
		String str = "";
		str = Pattern.compile("<ipaddress>([^<]+)</ipaddress>").matcher(content).replaceAll("<ipaddress>"+s.getInetAddress().getHostAddress()+"</ipaddress>");
		return str;
	}
	static public String replaceIP (String content) {
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String str = "";
		str = Pattern.compile("<ipaddress>([^<]+)</ipaddress>").matcher(content).replaceAll("<ipaddress>"+ip+"</ipaddress>");
		return str;
	}
	public String getContent() {
		System.out.println((new Throwable()).getStackTrace()[0].getLineNumber());
		return content;
	}
	public static void main(String [] argv) {
		XMLReader xr = new XMLReader("./xml/RossDeviceTemplate.xml");
		System.err.println(getRossSpaceFromXML(xr.getContent()));
	}
}
