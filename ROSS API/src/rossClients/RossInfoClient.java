package rossClients;

public class RossInfoClient {
	
	public static void main(String[] args) throws java.io.IOException {
		new RossInfoClientThread().start();
	}

	public RossInfoClient(){
		new RossInfoClientThread().start();
	}
}