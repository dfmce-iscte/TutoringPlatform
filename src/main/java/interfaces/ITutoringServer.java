package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface ITutoringServer extends Remote {

	public Map<ITeacher, Set<IAppointment>> searchAvailabilityForSpecificSubject(String subject)
			throws RemoteException;

	public String to_string() throws RemoteException;

	public List<String> getAllSubjects() throws RemoteException;

	public List<IAppointment> getAppointmentsForSubjectAndTeacher(String teacher,String subject) throws RemoteException;

	public ITeacher getTeacherByName(String name)throws RemoteException;

}
