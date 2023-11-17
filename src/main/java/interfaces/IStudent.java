package interfaces;

import java.rmi.RemoteException;
import java.util.Set;

public interface IStudent {
	
	public void appointment_available(Set<IAppointment> appointments) throws RemoteException;

}
