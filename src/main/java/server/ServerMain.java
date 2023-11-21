package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ServerMain {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            TutoringServer server = new TutoringServer();
            Naming.rebind("TutoringPlatform", server);

            System.out.println("Server is running...");

            Map<String, Double> t1_rates = new HashMap<>();
            Map<String, Double> t2_rates = new HashMap<>();
            Map<String, Double> t3_rates = new HashMap<>();

            t1_rates.put("Math", 10.0);
            t1_rates.put("Physics", 15.0);
            t2_rates.put("Math", 20.0);
            t3_rates.put("Math", 25.0);

            Teacher t1 = server.add_teacher(t1_rates, "José");
            Teacher t2 = server.add_teacher(t2_rates, "Diana");
            Teacher t3 = server.add_teacher(t3_rates, "Bruno");

            LocalDateTime d1_i = LocalDateTime.of(2023, 12, 12, 12, 0);
            LocalDateTime d1_f = LocalDateTime.of(2023, 12, 12, 12, 30, 0);

            LocalDateTime d2_i = LocalDateTime.of(2023, 11, 12, 10, 0);
            LocalDateTime d2_f = LocalDateTime.of(2023, 11, 12, 11, 30, 0);

            LocalDateTime d3_i = LocalDateTime.of(2023, 11, 12, 9, 0);
            LocalDateTime d3_f = LocalDateTime.of(2023, 11, 12, 10, 30, 0);

            t1.create_appointment(d1_i, d1_f, "Math");
            t1.create_appointment(d2_i, d2_f, "Physics");
            t2.create_appointment(d3_i, d3_f, "Math");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String text = scanner.nextLine();
                if (text.equals("app")) {
                    t3.create_appointment(d1_i, d1_f, "Math");
                }
            }

        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
