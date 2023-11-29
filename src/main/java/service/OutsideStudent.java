package service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import interfaces.IAppointment;
import interfaces.IStudent;
import interfaces.ITeacher;

public class OutsideStudent extends UnicastRemoteObject implements IStudent {
	
	private Set<IAppointment> studentAppointments;
	private Set<IAppointment> appoimentsNotified;
	private Map<String, ITeacher> studentWaitingList;
	private String name;

	protected OutsideStudent(String name) throws RemoteException {
		super();
		this.name = name;
	}
	
	public void addStudentToWaitingList(String subject, ITeacher teacher) throws RemoteException {
		studentWaitingList.put(subject, teacher);
	}
	
	public String[] getStudentAppointments() throws RemoteException {
		String[] student_app = new String[studentAppointments.size()];
		int i = 0;
		
		for (IAppointment app : studentAppointments) {
			student_app[i] = app.to_string();
			i++;
		}
		
		return student_app;
	}

	public String[] getAppoimentsNotified() throws RemoteException {
		String[] student_app = new String[studentAppointments.size()];
		int i = 0;
		
		for (IAppointment app : studentAppointments) {
			student_app[i] = app.to_string();
			i++;
		}
		
		return student_app;
	}


	@Override
	public void appointmentAvailable(IAppointment appointments) throws RemoteException {
		appoimentsNotified.add(appointments);
		
	}

	@Override
	public void addAppointment(IAppointment appointment) throws RemoteException {
		studentAppointments.add(appointment);	
	}

	@Override
	public String to_string() throws RemoteException {
		return "Student [number of student_appointments=" + studentAppointments.size() + "]" + " name: " + name;
	}

	@Override
	public String getName() throws RemoteException {
		return this.name;
	}

	@Override
	public Map<String, ArrayList<ITeacher>> getStudentWaitingList() throws RemoteException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getStudentWaitingList'");
	}

}
