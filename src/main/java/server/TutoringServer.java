package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import interfaces.ITeacher;
import interfaces.ITutoringServer;

public class TutoringServer extends UnicastRemoteObject implements ITutoringServer{
	
	private HashMap<String, Set<Teacher>> teachers_per_subjects;

	protected TutoringServer() throws RemoteException {
		super();
		this.teachers_per_subjects = new HashMap<String, Set<Teacher>>();
	}

	@Override
	public Set<ITeacher> search_for_teachers_for_specific_subject(String subject) throws RemoteException {
		Set<ITeacher> teachers_available = new HashSet<>();
		for (Teacher teacher: teachers_per_subjects.get(subject)) {
			teachers_available.add(teacher);
		}
		return teachers_available;
	}
	
	public void add_teacher(Teacher teacher) {
		
	}

}
