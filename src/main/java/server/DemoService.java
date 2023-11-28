package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class DemoService {

	private TutoringServer server = null;
	
	public DemoService() {
		
	}
	
	public TutoringServer get_server() {
		try {
			this.server = (TutoringServer) Naming.lookup("TutoringPlatform");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return server;
	}
	
}
