package rossServers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import rossProxyNetworking.RossProxyConstant;

import log.Log;

public class RossIPCastServerThread extends Thread {
	DatagramSocket socket;
    public RossIPCastServerThread() throws IOException {
        socket = new DatagramSocket();
    }

    public void run() {
        //while (moreQuotes) {
        
                byte[] buf = new byte[256];

                    // construct quote
                String dString = null;
               InetAddress RossServerAddress = null;
			try {
				RossServerAddress = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
               Log.debug(RossServerAddress.toString());
               buf = RossServerAddress.toString().getBytes();

		    // send it
              
                InetAddress group = null;
				try {
					group = InetAddress.getByName(RossProxyConstant.IP_MULTICAST_ADDRESS_GROUP);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, RossProxyConstant.IP_MULTICAST_PORT);
                //System.out.println(buf.toString());
                while(true){
                try {
					socket.send(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    // sleep for a while
                }
}
}
