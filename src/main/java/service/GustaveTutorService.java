package service;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

import interfaces.*;

public class GustaveTutorService {

	public class ITeacherWithIAppointments {
		public String teacher;
		public String[] appointments;
		
		public ITeacherWithIAppointments() {
			this.teacher = "";
			this.appointments = new String[1];
		}
		
		public ITeacherWithIAppointments(String teacher, String[] appointments) {
			this.teacher = teacher;
			this.appointments = appointments;
		}

		public String[] getIAppointments() {
			return appointments;
		}

		public String getITeacher() {
			return teacher;
		}
	}  

	private ITutoringServer server = null;
	private OutsideStudent student = null; 
	
	public GustaveTutorService() {
		try {
			this.server = (ITutoringServer) Naming.lookup("TutoringPlatform");
			student = new OutsideStudent("Student X");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public GustaveTutorService(String name) {
		try {
			this.server = (ITutoringServer) Naming.lookup("TutoringPlatform");
			student = new OutsideStudent(name);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ITeacherWithIAppointments[] searchAvailabilityForSpecificSubject(String subject) throws RemoteException {
		Map<ITeacher, Set<IAppointment>> appointments = server.searchAvailabilityForSpecificSubject(subject);
		ITeacherWithIAppointments[] appointmentsArray = new ITeacherWithIAppointments[appointments.size()];

		int i = 0;
		for (ITeacher teacher : appointments.keySet()) {
			String[] teacher_appointments = new String[appointments.get(teacher).size()];
			int j = 0;
			for (IAppointment appointment : appointments.get(teacher)) {
				teacher_appointments[j] = appointment.to_string();
				j++;
			}
			appointmentsArray[i] = new ITeacherWithIAppointments(teacher.getName(), teacher_appointments);
			i++;
		}

		return appointmentsArray;
	}
	
	public void addStudentToWaitingList(String teacher, String subject) throws RemoteException {
		ITeacher teacher_instance = server.getTeacherByName(teacher);
		teacher_instance.addStudentToWaitingList(student, subject);
	}
	
	public void removeStudentToWaitingList(String teacher, String subject) throws RemoteException {
		ITeacher teacher_instance = server.getTeacherByName(teacher);
		teacher_instance.removeStudentFromWaitingList(student, subject);
	}
	
	

	
	
}
