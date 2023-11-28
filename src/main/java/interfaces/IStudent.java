package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IStudent extends Remote {

	public void appointmentAvailable(IAppointment appointments) throws RemoteException;

	public void addAppointment(IAppointment appointment) throws RemoteException;

	public String to_string() throws RemoteException;

	public String getName() throws RemoteException;

}
