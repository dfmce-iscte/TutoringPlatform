package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface IStudent extends Remote{
	
	public void appointment_available(IAppointment appointments) throws RemoteException;

	public void add_appointment(IAppointment appointment) throws RemoteException;

	public String to_string() throws RemoteException;

}
