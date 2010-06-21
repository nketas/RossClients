package rossProxy;

import java.net.*;
import java.io.*;

import log.Log;

import rossClients.RossIPCastListener;
import rossProxyNetworking.RossNetworkDevicesManager;
import rossServers.RossIPCastServer;
import rossServers.RossProxyServer;

public class RossProxy {
	public static RossProxyServer server = null;
	
	//RossQueryClient client = null;
	public static RossIPCastListener IPClient = null;
	RossNetworkDevicesManager RDeviceManager = null;
	Log log = null;
	public RossProxy () {
		try {
			log = new Log();
			RDeviceManager = new RossNetworkDevicesManager(InetAddress.getLocalHost(), "xml/Ross.xml");
			//server = new RossProxyServer("./");
			//IPServer = new RossIPCastServer();
			IPClient = new RossIPCastListener();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String [] argv) {
		RossProxy p = new RossProxy();
		
	}
}
