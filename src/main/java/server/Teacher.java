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
import interfaces.AppointmentComparator;

public class Teacher extends UnicastRemoteObject implements ITeacher {

	private Set<Appointment> past_appointments;
	private Set<Appointment> future_appointments;
	private Map<String, Double> subjects_with_rates;
	private Map<String, Set<IStudent>> waiting_list;
	private String name;

	protected Teacher(Map<String, Double> subjects_with_rates, String name) throws RemoteException {
		super();
		past_appointments = new TreeSet<Appointment>(new AppointmentComparator());
		future_appointments = new TreeSet<Appointment>(new AppointmentComparator());
		waiting_list = new HashMap<String, Set<IStudent>>();
		this.subjects_with_rates = subjects_with_rates;
		this.name = name;
	}

	public void change_rate_to_subject(String subject, Double new_rate) {
		subjects_with_rates.put(subject, new_rate);
	}

	public void create_appointment(LocalDateTime initial_time, LocalDateTime final_time, String subject)
			throws RemoteException {
		future_appointments.add(new Appointment(initial_time, final_time, subject, this));
	}

	public Set<Appointment> getPast_appointments() {
		return past_appointments;
	}

	public Set<Appointment> getFuture_appointments() {
		return future_appointments;
	}

	public Map<String, Double> getSubjects_with_rates() {
		return subjects_with_rates;
	}

	public void addPast_appointments(Appointment appointment) {
		past_appointments.add(appointment);
	}

	public void addFuture_appointments(Appointment appointment) {
		waiting_list.get(appointment.get_subject()).remove(appointment.get_student());
		future_appointments.add(appointment);
	}

	public void addSubject_with_rates(String subject, Double value) {
		subjects_with_rates.put(subject, value);
	}

	@Override
	public void add_student_to_waiting_list(IStudent student, String subject) throws RemoteException {
		if (waiting_list.containsKey(subject)) {
			waiting_list.get(subject).add(student);
		} else {
			Set<IStudent> students = new HashSet<IStudent>();
			students.add(student);
			waiting_list.put(subject, students);
		}
	}

	@Override
	public Set<IAppointment> check_availability() throws RemoteException {
		Set<IAppointment> appointments = new TreeSet<IAppointment>(new AppointmentComparator());

		for (Appointment appointment : future_appointments) {
			if (appointment.get_student() == null) {
				appointments.add(appointment);
			}
		}
		return appointments;
	}

	@Override
	public String to_string() throws RemoteException{
		return "Teacher "+ name + " with subjects: " + subjects_with_rates.toString();
	}

}
