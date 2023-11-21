package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

public interface ITutoringServer extends Remote {

	public Map<ITeacher, Set<IAppointment>> search_availability_for_specific_subject(String subject)
			throws RemoteException;

	public String to_string() throws RemoteException;

}
