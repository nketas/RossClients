package rossServers;

import java.io.*;
import java.net.*;
import java.util.*;

import rossProxyNetworking.RequestThread;
import rossProxyNetworking.RossProxyConstant;

import log.Log;

/**
 * Most of these codes are stolen from Paul James Mutton's Mini Wegb Server /
 * SimpleWebServer http://www.jibble.org/
 * 
 */
public class RossProxyServer extends Thread {

	public static final String VERSION = "Ross API Proxy Server - <a href='http://synlab.gatech.edu/'>http://synlab.gatech.edu</a>";
	public static final Hashtable<String, String> MIME_TYPES = new Hashtable<String, String>();

	private File _rootDir;
	private ServerSocket _serverSocket;
	private boolean _running = true;

	static {
		String image = "image/";
		MIME_TYPES.put(".gif", image + "gif");
		MIME_TYPES.put(".jpg", image + "jpeg");
		MIME_TYPES.put(".jpeg", image + "jpeg");
		MIME_TYPES.put(".png", image + "png");
		String text = "text/";
		MIME_TYPES.put(".html", text + "html");
		MIME_TYPES.put(".htm", text + "html");
		MIME_TYPES.put(".txt", text + "plain");
		MIME_TYPES.put(".xml", text + "xml");
	}

	public RossProxyServer(String dir) {
		try {
			System.out.println(dir);
			_rootDir = (new File(dir)).getCanonicalFile();
			if (!_rootDir.isDirectory()) {
				throw new IOException("Not a directory.");
			}
			_serverSocket = new ServerSocket(RossProxyConstant.proxyServerPort);
			Log.debug(_serverSocket.getInetAddress().getHostAddress());
			start();
			RossQueryServer rqs = new RossQueryServer();
			rqs.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void run() {
		Log.debug(VERSION);
		while (_running) {
			try {
				Socket socket = _serverSocket.accept();
				//System.err.println("create a requestThread>>>");
				Log.debug("create a requestThread>>>");
				RequestThread requestThread = new RequestThread(socket, _rootDir);
				requestThread.start();
				Log.debug("<<<a requestThread created");
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		try {
			_serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(1);
	}

	// Work out the filename extension. If there isn't one, we keep
	// it as the empty string ("").
	public static String getExtension(File file) {
		String extension = "";
		String filename = file.getName();
		int dotPos = filename.lastIndexOf(".");
		if (dotPos >= 0) {
			extension = filename.substring(dotPos);
		}
		return extension.toLowerCase();
	}

	public static void main(String[] args) {
		try {
			RossProxyServer server = new RossProxyServer("./xml");
			RossIPCastServer IPServer = new RossIPCastServer();
			Log log = new Log();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}