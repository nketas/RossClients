package rossMessage;

import java.util.HashMap;

public class RossMessageHandler {

	private RossMessage rm;
	private static HashMap<String, RossMessage> RegisteredMessages;
	
	public RossMessageHandler()
	{
		RegisteredMessages = new HashMap<String, RossMessage>();
	}
	
	
	public static boolean createSubscription(RossMessage rm, String message)
	{
		//rm.processMessage(message);
		if(!RegisteredMessages.containsValue(message))
		{
			RegisteredMessages.put(message, rm);
			return true;
		}
		else
			return false;
		
	}
	
	public static void handleMessage(String message)
	{
		RegisteredMessages.get(message).processMessage(message);
	}
	
}
