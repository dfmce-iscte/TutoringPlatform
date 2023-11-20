package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.time.LocalDateTime;

import interfaces.IAppointment;
import interfaces.IStudent;
import interfaces.ITeacher;

public class Teacher extends UnicastRemoteObject implements ITeacher {

	private Set<Appointment> past_appointments;
	private Set<Appointment> future_appointments;
	private Map<String, Double> subjects_with_rates;
	private Map<String, Set<IStudent>> waiting_list;
	private String name;
	private final int id;

	protected Teacher(int id, Map<String, Double> subjects_with_rates, String name) throws RemoteException {
		super();
//		past_appointments = new TreeSet<Appointment>(new AppointmentComparator());
//		future_appointments = new TreeSet<Appointment>(new AppointmentComparator());
		this.id = id;
		waiting_list = new HashMap<String, Set<IStudent>>();
		past_appointments = new HashSet<Appointment>();
		future_appointments = new HashSet<Appointment>();
		this.subjects_with_rates = subjects_with_rates;
		this.name = name;

	}

	public void change_rate_to_subject(String subject, Double new_rate) {
		subjects_with_rates.put(subject, new_rate);
	}

	public void notify_student(String subject) throws RemoteException {
		if (waiting_list.containsKey(subject)) {
			waiting_list.get(subject).forEach(student -> {
				try {
					student.appointment_available(null);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			});
		}
	}

	public void create_appointment(LocalDateTime initial_time, LocalDateTime final_time, String subject)
			throws RemoteException {
		Appointment new_appointment = new Appointment(initial_time, final_time, subject, this);		
		future_appointments.add(new_appointment);
		notify_student(subject);

	}
	public int get_id() {
		return id;
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
		//waiting_list.get(appointment.get_subject()).remove(appointment.get_student());
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
		System.out.println("For teacher " + name + ", student added to waiting list. Size: " + waiting_list.get(subject).size());
	}

	// @Override
	public Set<IAppointment> check_availability(String subject) throws RemoteException {
//		Set<IAppointment> appointments = new TreeSet<IAppointment>(new AppointmentComparator());
		Set<IAppointment> appointments = new HashSet<IAppointment>();

		for (Appointment appointment : future_appointments) {
			if (appointment.get_student() == null && appointment.get_subject().equals(subject)) {
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
