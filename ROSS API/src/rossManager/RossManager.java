package rossManager;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

import log.Log;

import rossProxyNetworking.RossNetworkDevicesManager;
import rossServers.RossIPCastServer;

public class RossManager {
	RossIPCastServer IPServer = null;

	public RossManager()
	{
		/*try {
			IPServer = new RossIPCastServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	public HashMap<InetAddress, String> getRossDevices()
	{
		return RossNetworkDevicesManager.DeviceList;
	}
	
	public String getRossFilePath(InetAddress DeviceIP)
	{
		if(RossNetworkDevicesManager.isAdded(DeviceIP))
		{
			HashMap<InetAddress, String> Devices = getRossDevices();
		    return Devices.get(DeviceIP);
		}
		else
			return null; //Means device not added to list
		
	}
	//Sets whether your device is discoverable. Does this by turning on and off (according to param),
	//the IP multicast server
	public void setDiscoverable(boolean value)
	{
		if(value == true)
		{
			//WILL BE IMPLEMENTED LATER
		}
	}
	
	/*public void getFiducialPositions(InetAddress DeviceIP)
	{
		if(RossNetworkDevicesManager.isAdded(DeviceIP))
		{
			
		}
		else
			Log.debug("*** Device not present in devices list");
	}*/
	
	public HashMap getDeviceSurfaces(InetAddress DeviceIP)
	{
		HashMap surfaces = new HashMap();
		return surfaces;
	}
	
	public HashMap getDeviceSpaces(InetAddress DeviceIP)
	{
		HashMap spaces = new HashMap();
		return spaces;
	}
	public HashMap getDeviceObjects(InetAddress DeviceIP)
	{
		HashMap objects = new HashMap();
		return objects;
	}
	
	public TreeMap<String, String> getProperty(InetAddress DeviceIP, String tagName, String Id)
	{
		if(RossNetworkDevicesManager.isAdded(DeviceIP))
		{
			String RossFileName = "./xml" + DeviceIP.getHostAddress() + ".xml";
			
			TreeMap<String, String> tagProperty = new TreeMap<String, String>();
			
			return tagProperty;
		}
		else
			return null;
	}
	
	
}
