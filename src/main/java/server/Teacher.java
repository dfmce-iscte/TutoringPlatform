package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.time.LocalDateTime;

import interfaces.IAppointment;
import interfaces.IStudent;
import interfaces.ITeacher;

public class Teacher extends UnicastRemoteObject implements ITeacher{
	
	
	private Set<Appointment> past_appointments;
	private Set<Appointment> future_appointments;
	private Map<String,Double> subjects_with_rates;
	private Set<IStudent> waiting_list;

	protected Teacher(HashMap<String,Double> subjects_with_rates) throws RemoteException {
		super();
		past_appointments = new TreeSet<Appointment>(new AppointmentComparator());
		future_appointments = new TreeSet<Appointment>(new AppointmentComparator());
		waiting_list = new HashSet<IStudent>();
		this.subjects_with_rates = subjects_with_rates;
	}
	
	public void change_rate_to_subject(String subject, Double new_rate) {
		subjects_with_rates.put(subject, new_rate);
	}
	
	public void create_appointment(LocalDateTime initial_time, LocalDateTime final_time, String subject) throws RemoteException {
		future_appointments.add(new Appointment(initial_time, final_time, subject, this));
	}

	@Override
	public Set<IAppointment> check_availability() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add_student_to_waiting_list(IStudent student) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
