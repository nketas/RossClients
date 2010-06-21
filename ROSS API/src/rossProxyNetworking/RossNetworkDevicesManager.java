package rossProxyNetworking;
import java.net.InetAddress;
import java.util.HashMap;

public class RossNetworkDevicesManager {
	
	public static HashMap<InetAddress, String> DeviceList = new HashMap<InetAddress, String>();
	InetAddress LocalHostAddress = null;
	String LocalRossXMLFile = null;
	
	public RossNetworkDevicesManager(InetAddress local, String RossFile)
	{
		if(local != null)
			LocalHostAddress = local;
		else
			System.err.println("Null passed as local host address");
		if(RossFile != null)
			LocalRossXMLFile = RossFile;
		else
			System.err.println("Null RossFileName passed");
	}
	
	public static boolean addRossDevice(InetAddress DeviceIP, String RossFilePath)
	{
		DeviceList.put(DeviceIP, RossFilePath);
		return true;
	}
	
	public static boolean isAdded(InetAddress DeviceIP)
	{
		if(DeviceList == null)
			return false;
		else
		{
			if (DeviceList.containsKey(DeviceIP))
				return true;
			else 
				return false;
		}
	}
	

}
