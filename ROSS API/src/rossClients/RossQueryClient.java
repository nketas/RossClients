/*
 * Ross Proxy project
 * 
 * @author Andy Wu
 * @version %I%, %G%
 */

package rossClients;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

import log.Log;

import rossProxyNetworking.FinishXMLCollecting;
import rossProxyNetworking.RossNetworkDevicesManager;
import rossProxyNetworking.RossProxyConstant;
import rossProxyXMLParser.StaXParser;

/*
 * Broadcasts to the network to look for other proxies. After that starts a TCP thread to get the XML file from each proxy.

 */
public class RossQueryClient extends Thread{
	public static final String VERSION = RossProxyConstant.PROTOCOL
			+ RossProxyConstant.PROXY_VERSION;
	//private Hashtable<String, String> remoteServerList = new Hashtable<String, String>();

	private DatagramSocket UDPsocket;
	private int bcTimeout = 20 * 1000; //20 seconds

	private int clientThreadNum = 0;
	private String localIP = "";
	
	private FinishXMLCollecting fxc = null;
	
	/*
	 * @param event a callback event
	 */
	public RossQueryClient(InetAddress RossDeviceIP) {
		try {
			//fxc = event;
			//localIP = InetAddress.getLocalHost().getHostAddress();
			localIP = RossDeviceIP.getHostAddress();
			UDPsocket = new DatagramSocket();
			
				/*RossQueryClient rqc = new RossQueryClient(new FinishXMLCollecting() {
					public void FinishXMLCollecting() {;}
				});*/
				//rqc.start();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	public RossQueryClient() {
		try {
			//fxc = event;
			localIP = InetAddress.getLocalHost().getHostAddress();
			//localIP = RossDeviceIP.getHostAddress();
			UDPsocket = new DatagramSocket();
			
				/*RossQueryClient rqc = new RossQueryClient(new FinishXMLCollecting() {
					public void FinishXMLCollecting() {;}
				});*/
				//rqc.start();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void run () {
		sendQuery();
		waitForPackets();
	}

	public void sendQuery() {
		try {
			String message = VERSION+"Send";
			
			byte data[] = message.getBytes();
			String bcAddr = localIP;//InetAddress.getLocalHost().getHostAddress();
			//for subnet x.y.z.255
			//bcAddr = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.)([\\d]+)").matcher(bcAddr).replaceAll("$1255");
			Log.debug("CLIENT: RossQueryClient broadcasts using protocol " + VERSION +" ip:"+InetAddress.getByName(bcAddr)+":"+RossProxyConstant.queryServerPort);
			DatagramPacket sendPacket = new DatagramPacket(data, data.length,
					InetAddress.getByName(bcAddr), RossProxyConstant.queryServerPort);
			UDPsocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void waitForPackets() {
		//clearServerList();
		while (true) {
			QueryThread xmlProcessThread = null;
			try {
				byte data[] = new byte[100];
				UDPsocket.setSoTimeout(bcTimeout);
				DatagramPacket receivePacket = new DatagramPacket(data,
						data.length);
				Log.debug("CLIENT: RossQueryClient waiting for UDPsocket returns...>>>");
				UDPsocket.receive(receivePacket);
				//if (!isValidPacket(receivePacket)) continue;
				Log.debug("CLIENT: <<<RossQueryClient got UDPsocket ...");
				
				String rString = new String(receivePacket.getData());
				Log.debug("CLIENT: RossQueryClient receives ack from server "
						+ receivePacket.getAddress().getHostAddress() + ": "
						+ rString);

				Matcher m = Pattern.compile("RossProxyServer=([^:]+):([0-9]+)").matcher(rString);
				String proxyServerIP = "";
				int proxyServerPort = RossProxyConstant.proxyServerPort;
				if (m.find()) {
					proxyServerIP = m.group(1);
					proxyServerPort=new Integer(m.group(2));
				}
				Log.debug("CLIENT: xmlProcessThread("+proxyServerIP+","+proxyServerPort+")");
				xmlProcessThread = new QueryThread(proxyServerIP, proxyServerPort);
				incClientThreadNum();
				xmlProcessThread.start();	
			} catch (SocketTimeoutException ste) {
				Log.debug("CLIENT: timeout.");
				break;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		Log.debug("end of thread?");
		//fxc.FinishXMLCollecting();
	}

	private boolean isValidPacket (DatagramPacket packet) {
		String str = new String(packet.getData());
		if (!str.startsWith(VERSION+"Ack")) return false;
		if (packet.getAddress().getHostAddress().compareTo(localIP)==0) return false;
		return true;
	}

	//public static void main(String[] argv) {
		
	//}

	/*public synchronized Hashtable<String, String> getRemoteServerList() {
		try {
			while (clientThreadNum > 0) {
				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return remoteServerList;
	}*/

	/*private synchronized void clearServerList() {
		remoteServerList.clear();
		clrClientThreadNum();
	}*/

	public class QueryThread extends Thread {
		private Socket socket = null;
		private BufferedReader in = null;
		private DataOutputStream out = null;
		private InetAddress RossDeviceIP = null;
		public QueryThread(String ip, int p) {
			try {
				socket = new Socket(ip, p);
				RossDeviceIP = InetAddress.getByName(ip);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void run() {
			String message = "GET / ROSS/0.1\r\n\r\n";
			String xmlContent = "";
			try {
				String str = null;
				out = new DataOutputStream(socket.getOutputStream());
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out.writeBytes(message);
				out.flush();
				while((str=in.readLine())!=null) {
					xmlContent+=str+"\n";
				}
				Log.debug("CLIENT: size of xmlContent="+xmlContent.length());
				StaXParser temp = new StaXParser();
				//addServerList(socket.getInetAddress().getHostAddress(), xmlContent);
				if(!RossNetworkDevicesManager.isAdded(RossDeviceIP));
				{
					//RossNetworkDevicesManager.addRossDevice(RossDeviceIP, xmlContent);
					Log.debug(xmlContent);
					//File RossFile = new File("./Device_Ross_Files/" + RossDeviceIP + ".xml");
					FileWriter RossFileWriter = new FileWriter("./Device_Ross_Files/" + RossDeviceIP + ".xml", true);
					BufferedWriter xmlContentWriter = new BufferedWriter(RossFileWriter);
					xmlContentWriter.write(xmlContent);
					xmlContentWriter.flush();
				}
				decClientThreadNum();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*private synchronized void addServerList(String srv, String xmlContent) {
			if (!remoteServerList.containsKey(srv))
				remoteServerList.put(srv, xmlContent);
			//Log.debug(remoteServerList.size());
		}*/
	}

	private synchronized int clrClientThreadNum() {
		clientThreadNum = 0;
		return clientThreadNum;
	}

	public synchronized int getClientThreadNum() {
		//Log.debug("CLIENT: getClientThreadNum():clientThreadNum="+clientThreadNum);
		return clientThreadNum;
	}

	private synchronized int incClientThreadNum() {
		clientThreadNum++;
		//Log.debug("CLIENT: incClientThreadNum:clientThreadNum="+clientThreadNum);
		return clientThreadNum;
	}

	private synchronized int decClientThreadNum() {
		clientThreadNum = (clientThreadNum == 0) ? 0 : --clientThreadNum;
		//Log.debug("CLIENT: decClientThreadNum:clientThreadNum="+clientThreadNum);
		return clientThreadNum;
	}
}
