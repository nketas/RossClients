package rossProxy;

import java.io.*;
import java.net.*;
import java.util.regex.*;

public class Test {
	
	public Test (){
		try {
			QueryThread qt = new QueryThread("255.255.255.255", 7033);
			qt.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param argv
	 */
	public static void main (String [] argv) {
		String content = "";
		try {
	        BufferedReader in = new BufferedReader(new FileReader("./xml/ttt.xml"));
	        String str;
	        while ((str = in.readLine()) != null) {
	            content+=str+"\n";
	        }
	        in.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    try {
			int start = 0, end = 0;
			String str = null;
			if (content==null) return;
			Matcher matcher1 = Pattern.compile("<rossXML>[\r\n]*").matcher(content);
			Matcher matcher2 = Pattern.compile("[\r\n]*</rossXML>").matcher(content);
			if (matcher1.find()) {
				start = matcher1.end();
			}
			if (matcher2.find()) {
				end = matcher2.start();
			}

			//str = Pattern.compile("<ipaddress>([^<]+)</ipaddress>").matcher(content.substring(start, end)).replaceAll("<ipaddress>a.b.c.d</ipaddress>");
			System.err.println(str);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		
		try {
			Pattern p = Pattern.compile("RossProxyServer=([^:]+):([0-9]+)");
			Matcher m = p.matcher("RossProxyServer=127.0.0.1:7033");
			System.err.println(m.groupCount());
			if (m.find()) {
	            System.err.println("Found comment: "+m.group(0));
	            System.err.println("Found comment: "+m.group(1));
	            System.err.println("Found comment: "+m.group(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	Test t = new Test();
		/*try{
			byte [] ip = InetAddress.getLocalHost().getAddress();
			System.err.println(ip[0]);
			String bcAddr = InetAddress.getLocalHost().getHostAddress();
			System.err.println("bcAddr="+bcAddr);
			bcAddr = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.)([\\d]+)").matcher(bcAddr).replaceAll("$1255");
			System.err.println("bcAddr="+bcAddr);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}

	class QueryThread{
		private Socket socket = null;
		private BufferedReader in = null;
		private BufferedOutputStream out = null;

		public QueryThread(String ip, int p) {
			try {
				InetAddress addr = InetAddress.getByName(ip);
				System.err.println(ip+":"+p);
				socket = new Socket(addr, p);
				if (socket == null) {
					System.err.println("socket is null");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void send() {
			String message = "GET / HTTP/1.0\r\n\r\n";
			String xmlContent = null;
			try {
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				BufferedReader in = new BufferedReader(new InputStreamReader(socket
						.getInputStream()));
				System.err.println("out.writeBytes(message)");
				out.writeBytes(message);
				out.flush();
				System.err.println("in.readLine()");
				xmlContent = in.readLine();
				System.err.println("xmlContent="+xmlContent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private String getRossSpaceFromXML(String content) {
			int start = 0, end = 0;
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

				str = Pattern.compile("<ipaddress>([^<]+)</ipaddress>").matcher(content.substring(start, end)).replaceAll("<ipaddress>"+socket.getInetAddress().getHostAddress()+"</ipaddress>");
			} catch (Exception e) {
				e.printStackTrace();
			}

			return str;
		}
	}
	
}
