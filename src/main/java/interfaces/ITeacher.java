package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface ITeacher extends Remote {
	
	public Set<IAppointment> check_availability() throws RemoteException;
	
	public void add_student_to_waiting_list(IStudent student) throws RemoteException;
}
