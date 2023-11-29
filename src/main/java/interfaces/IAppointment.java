package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

public interface IAppointment extends Remote {

	
	public String bookAppointment(IStudent student) throws RemoteException;

	public String to_string() throws RemoteException;

	public LocalDateTime getInitialTime() throws RemoteException;

	public ITeacher getTeacher() throws RemoteException;

	public String getSubject() throws RemoteException;
}
