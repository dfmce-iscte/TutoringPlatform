package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;

public class Server {
    
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            TutoringServer server = new TutoringServer();
            Naming.rebind("TutoringPlatform", server);

            Map<String,Double> t1_rates = new HashMap<>();
            Map<String,Double> t2_rates = new HashMap<>();
            
            t1_rates.put("Math", 10.0);
            t1_rates.put("Physics", 15.0);
            t2_rates.put("Math", 20.0);
            server.add_teacher(new Teacher(t1_rates, "José"));
            server.add_teacher(new Teacher(t2_rates, "Diana"));
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

}

