package rossServers;

import java.io.IOException;

public class RossIPCastServer {
	public static void main(String[] args) throws java.io.IOException {
    new RossIPCastServerThread().start();
}
	
	public RossIPCastServer() throws IOException{
	       new RossIPCastServerThread().start();
	   }
}
