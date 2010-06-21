package rossServers;

import java.io.*;
import java.net.*;

import rossProxyNetworking.RossProxyConstant;

import log.Log;

public class RossQueryServer extends Thread{
	public static final String VERSION = RossProxyConstant.PROTOCOL + RossProxyConstant.PROXY_VERSION;
	public String serverIP = null;
	
	private DatagramSocket UDPSocket;
	boolean _running = true;

	public RossQueryServer() {
		try {
			serverIP = InetAddress.getLocalHost().getHostAddress();
			UDPSocket = new DatagramSocket (RossProxyConstant.queryServerPort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void run() {
		Log.debug("RossQueryServer: "+VERSION+" starts on " + serverIP);
		while (_running) {
			try {
				byte data[] = new byte [100];
				DatagramPacket receivePacket = new DatagramPacket(data, data.length);
				Log.debug("RossQueryServer waiting for UDPsocket message...>>>");
				UDPSocket.receive(receivePacket);
				
				Log.debug("<<<RossQueryServer got UDPsocket message...");
				
				HandleClient clientThread = new HandleClient(receivePacket, UDPSocket);
				clientThread.setVersionStr(VERSION);
				clientThread.setSendString(VERSION+"Ack"+":RossProxyServer="+InetAddress.getLocalHost().getHostAddress()+":"+RossProxyConstant.proxyServerPort);
				clientThread.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean isValidPacket (DatagramPacket packet) {
		String str = new String(packet.getData());
		//if (!str.startsWith(VERSION+"Send")) return false;
		//if (packet.getAddress().getHostAddress().compareTo(serverIP)==0) return false;
		return true;
	}
	
	class HandleClient extends Thread {
		private String versionStr = "";
		private DatagramPacket receivePacket;
		private DatagramSocket UDPSocket;
		private String sendString = "From RossQueryServer.";
		
		public HandleClient(DatagramPacket packet, DatagramSocket socket){
			receivePacket = packet;
			UDPSocket = socket;
		}
		
		public void run() {
			try {
				byte [] sendData = sendString.getBytes();
				Log.debug("RossQueryServer accepts a client from "+receivePacket.getAddress().getHostAddress());
				DatagramPacket sendPacket = new DatagramPacket (sendData, sendData.length,
						receivePacket.getAddress(), receivePacket.getPort());
				UDPSocket.send(sendPacket);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public String getSendString() {
			return sendString;
		}

		public void setSendString(String sendString) {
			this.sendString = sendString;
		}

		public String getVersionStr() {
			return versionStr;
		}

		public void setVersionStr(String versionStr) {
			this.versionStr = versionStr;
		}
	}
}

