package rossProxyNetworking;

import java.io.*;
import java.net.*;
import java.util.*;

import log.Log;

//import rossClients.RossQueryClient;
//import rossClients.RossQueryClient.QueryThread;
import rossProxy.DirReader;
import rossServers.RossProxyServer;

/**
 * Most of these codes are stolen from Paul James Mutton's Mini Wegb Server / SimpleWebServer
 * http://www.jibble.org/
 *
 */
public class RequestThread extends Thread {

	private File _rootDir = null;
	private Socket _socket = null;
	private boolean finishXMLCollecting = false;
	String xmlOutput;
	private boolean cachedXML = false;
	
	
	public RequestThread(Socket socket, File rootDir) {
		_socket = socket;
		_rootDir = rootDir;
	}

	private static void sendHeader(BufferedOutputStream out, int code, String contentType, long contentLength, long lastModified) throws IOException {
		out.write(("HTTP/1.0 " + code + " OK\r\n" + 
				"Date: " + new Date().toString() + "\r\n" +
				"Server: RossProxyServer/1.0\r\n" +
				"Content-Type: " + contentType + "\r\n" +
				"Expires: Thu, 01 Dec 1994 16:00:00 GMT\r\n" +
				((contentLength != -1) ? "Content-Length: " + contentLength + "\r\n" : "") +
				"Last-modified: " + new Date(lastModified).toString() + "\r\n" +
		"\r\n").getBytes());
	}

	private static void sendError(BufferedOutputStream out, int code, String message) throws IOException {
		message = message + "<hr>" + RossProxyServer.VERSION;
		sendHeader(out, code, "text/html", message.length(), System.currentTimeMillis());
		out.write(message.getBytes());
		out.flush();
		out.close();
	}

	private String processXML(Hashtable <String, String> list) {
		String str="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<rossXML>\n";
		Enumeration<String> e = list.elements();
		while( e.hasMoreElements() ){
			String t = e.nextElement();
			str += t + "\n";
			//    		System.err.println(t+"\n\n");
		}
		str += "</rossXML>";
		return str;
	}
	private String getRossFile()
	{
		String RossXml = null, line = " ";
		//StringBuilder contents = new StringBuilder();
		//File RossXML = new File("./xml/Ross.xml");
		try {
			FileReader RossXML = new FileReader(_rootDir + "/Ross.xml");
		
			//use buffering, reading one line at a time
			//FileReader always assumes default encoding is OK!
			BufferedReader input =  new BufferedReader(RossXML);
			while ((line = input.readLine())!= null)
			{
				RossXml = RossXml + line + "\n"; //not declared within while loop
			/*
			 * readLine is a bit quirky :
			 * it returns the content of a line MINUS the newline.
			 * it returns null only for the END of the stream.
			 * it returns an empty String if two newlines appear in a row.
			 */
			
			}

			//Log.debug(contents.toString());
			input.close();

		}
		catch (IOException ex){
			ex.printStackTrace();
		}
		Log.debug(RossXml);
		return RossXml;
	}
	public void FinishXMLCollecting() {
		finishXMLCollecting = true;
	}

	public void run() {
		InputStream reader = null;
		try {
			_socket.setSoTimeout(30000);
			BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
			BufferedOutputStream out = new BufferedOutputStream(_socket.getOutputStream());
			
			String request = in.readLine();
			if(cachedXML == true)
			{
			
				Log.debug("SERVER: a ROSS/0.1 request, must be from another Ross Proxy.");
				Log.debug("SERVER: " + xmlOutput);
				out.write(xmlOutput.getBytes());
				out.flush();
				Log.debug("SERVER: " + xmlOutput.length()+" sent.");
				out.close();
				Log.debug("SERVER: thread end.");
			//Log.debug("request="+request);

			/*if (request != null && request.startsWith("GET / ROSS/0.1")) {
				DirReader dr = new DirReader("./xml");
				xmlOutput=dr.processDir();
				Log.debug("SERVER: a ROSS/0.1 request, must be from another Ross Proxy.");
				Log.debug("SERVER: " + xmlOutput);
				out.write(xmlOutput.getBytes());
				out.flush();
				Log.debug("SERVER: " + xmlOutput.length()+" sent.");
				out.close();
				Log.debug("SERVER: thread end.");*/
				return;
			}
			else
			{
				xmlOutput = getRossFile();
				cachedXML = true;
				Log.debug(xmlOutput);
			}

			//if (request == null || !request.startsWith("GET ") || !(request.endsWith(" HTTP/1.0") || request.endsWith("HTTP/1.1"))) {
			if (request == null || !request.startsWith("GET ")){ //|| !(request.e") || request.endsWith("HTTP/1.1"))) {
				// Invalid request type (no "GET")
				sendError(out, 500, "Invalid Method.");
				return;
			}
		
/*
			String path = request.substring(4, request.length() - 9);            
			File file = new File(_rootDir, URLDecoder.decode(path, "UTF-8")).getCanonicalFile();

			if (file.isDirectory() || file.getName().compareTo("ross.xml")==0) {
				File indexFile = new File(file, "ross.xml");
				if (indexFile.exists() && !indexFile.isDirectory()) {
					file = indexFile;
				}
			}

			if (!file.toString().startsWith(_rootDir.toString())) {
				// Uh-oh, it looks like some lamer is trying to take a peek
				// outside of our web root directory.
				sendError(out, 403, "Permission Denied.");
			}
			else if (!file.exists()) {
				// The file was not found.
				sendError(out, 404, "File Not Found.");
			} else if (file.isFile()) {
				reader = new BufferedInputStream(new FileInputStream(file));

				String contentType = (String)RossProxyServer.MIME_TYPES.get(RossProxyServer.getExtension(file));
				if (contentType == null) {
					contentType = "application/octet-stream";
				}

				sendHeader(out, 200, contentType, file.length(), file.lastModified());

				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = reader.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
				reader.close();
			} else if (file.isDirectory()) {
				String xmlOutput="";
				DirReader dr = null;
				Hashtable <String, String> serverList = null;
				Log.debug("SERVER: A regular HTTP request, must be a browser or a Ross SDK request.");
				//RossQueryClient rqc = new RossQueryClient();
				//rqc.start();
				while(!finishXMLCollecting) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				Log.debug("SERVER: rqc.getRemoteServerList()");
				//serverList = rqc.getRemoteServerList();
				dr = new DirReader("./xml");
				//serverList.put(_socket.getInetAddress().getHostAddress(), dr.processDir());
				//xmlOutput = processXML(serverList);
				//                System.err.println(xmlOutput);
				//Written for debugging by Jayraj
				//xmlOutput = "<rossXml></rossXml>";
				xmlOutput = getRossFile();
				System.out.println(xmlOutput);
				if(xmlOutput == null)
					xmlOutput = "<rossxml></rossxml>";
				out.write(xmlOutput.getBytes());
			}
			out.flush();
			out.close();*/
			
			out.write(xmlOutput.getBytes());
			out.flush();
			out.close();
		}
		catch (IOException e) {
			if (reader != null) {
				try {
					reader.close();
				}
				catch (Exception anye) {
					// Do nothing.
				}
			}
		}
	}
}