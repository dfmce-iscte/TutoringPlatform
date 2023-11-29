package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public interface IStudent extends Remote {

	public void appointmentAvailable(IAppointment appointments) throws RemoteException;

	public void addAppointment(IAppointment appointment) throws RemoteException;

	public String to_string() throws RemoteException;

	public String getName() throws RemoteException;

	public Map<String, ArrayList<ITeacher>> getStudentWaitingList() throws RemoteException;


}
