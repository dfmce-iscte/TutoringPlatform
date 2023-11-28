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
		public ITeacher teacher;
		public IAppointment[] appointments;

		public ITeacherWithIAppointments(ITeacher teacher, IAppointment[] appointments) {
			this.teacher = teacher;
			this.appointments = appointments;
		}

		public IAppointment[] getIAppointments() {
			return appointments;
		}

		public ITeacher getITeacher() {
			return teacher;
		}
	}  

	private ITutoringServer server = null;
	
	public GustaveTutorService() {
		try {
			this.server = (ITutoringServer) Naming.lookup("TutoringPlatform");
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
			IAppointment[] teacher_appointments = new IAppointment[appointments.get(teacher).size()];
			int j = 0;
			for (IAppointment appointment : appointments.get(teacher)) {
				teacher_appointments[j] = appointment;
				j++;
			}
			appointmentsArray[i] = new ITeacherWithIAppointments(teacher, teacher_appointments);
			i++;
		}

		return appointmentsArray;
	}
	
	
}
