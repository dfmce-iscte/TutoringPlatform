package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;

import gui.CombinedGui;

public class ServerMain {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            TutoringServer server = new TutoringServer();
            Naming.rebind("TutoringPlatform", server);

            System.out.println("Server is running...");

            Teacher maria = server.addTeacher("Maria");
            Teacher pedro = server.addTeacher("Pedro");
            CombinedGui mariaGui = new CombinedGui(maria);
            CombinedGui pedroGui = new CombinedGui(pedro);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String name = scanner.nextLine();
                Boolean teacher_already_exists = false;
                for (Teacher t: server.getTeachers()) {
                    if (t.getName().equals(name)) {
                        new CombinedGui(t);
                        teacher_already_exists = true;
                    }
                }
                if (!teacher_already_exists)
                    new CombinedGui(server.addTeacher(name)).setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }

            
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

}


 