package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface ITutoringServer extends Remote {
	
	public Set<ITeacher> search_for_teachers_for_specific_subject(String subject) throws RemoteException;
	
	

}
