package rossClients;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import log.Log;

import rossProxyNetworking.RossNetworkDevicesManager;


public class RossIPListenerClientThread extends Thread {

	MulticastSocket socket; 
	InetAddress address; 
	DatagramPacket packet;
	public final int TIME_TO_LISTEN = 1000;
	public RossIPListenerClientThread()
	{
		try
		{

			address =  InetAddress.getByName("230.0.0.1");


		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}


	public void run()
	{

		// get a few quotes
		//for (int i = 0; i > -5; i++) {
		while(true)
		{
			try {
				socket =  new MulticastSocket(4446);
				socket.joinGroup(address);
				byte[] buf = new byte[256];
				packet = new DatagramPacket(buf, buf.length);

				socket.receive(packet);


				String received = new String(packet.getData(), 0, packet.getLength());
				String splitResult[] = received.split("/");
				//System.out.println(splitResult[1]);
				InetAddress RossDeviceIP = InetAddress.getByName(splitResult[1]);
				//System.out.println(RossDeviceIP.toString());
				/*
				 * 
				 * Heavily troubled by this piece of code. Violates principle of 
				 * modularity. Ideally, RossIPListenerClientThread shouldn't have
				 * anything to do with RossNetworkDevicesManager
				 */

				if((splitResult[1].equals(  InetAddress.getLocalHost().getHostAddress())))
				{
					Log.debug("LHS: " + splitResult[1]);
					Log.debug("RHS: " + InetAddress.getLocalHost().getHostAddress());
					if(!RossNetworkDevicesManager.isAdded(RossDeviceIP))
					{
						RossQueryClient rqc = new RossQueryClient(RossDeviceIP);
						rqc.start();
						RossNetworkDevicesManager.addRossDevice(RossDeviceIP, "./Device_Ross_Files/" + splitResult[1] + ".xml");
					}
					else
					{
						Log.debug("Device already added to list");
					}
					//System.out.println("Quote of the Moment: " + received);


					socket.leaveGroup(address);
					socket.close();
					Thread.sleep(TIME_TO_LISTEN);
				}
				else
				{
					
					Log.debug("Server located on same machine as client");
					Thread.sleep(TIME_TO_LISTEN);
					
				}
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


	}
}


