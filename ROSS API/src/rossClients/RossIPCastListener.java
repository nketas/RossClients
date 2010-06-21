package rossClients;

public class RossIPCastListener {
	public static void main(String[] args) throws java.io.IOException {
		new RossIPListenerClientThread().start();
	}
	public RossIPCastListener()
	{
		new RossIPListenerClientThread().start();
	}
}
