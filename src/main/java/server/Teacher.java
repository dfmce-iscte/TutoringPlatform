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

	private Set<Appointment> appointments;
	private Map<String, Double> subjects_with_rates;
	private Map<String, Set<IStudent>> waiting_list;
	private String name;
	private final int id;
	private TutoringServer server;

	protected Teacher(TutoringServer server, int id, String name) throws RemoteException {
		super();
		this.server = server;
		this.id = id;
		waiting_list = new HashMap<String, Set<IStudent>>();
		appointments = new HashSet<Appointment>();
		this.subjects_with_rates = new HashMap<>();
		this.name = name;
	}

	protected Teacher(TutoringServer server, int id, Map<String, Double> subjects_with_rates, String name) throws RemoteException {
		super();
		// past_appointments = new TreeSet<Appointment>(new AppointmentComparator());
		// future_appointments = new TreeSet<Appointment>(new AppointmentComparator());
		this.server = server;
		this.id = id;
		waiting_list = new HashMap<String, Set<IStudent>>();
		appointments = new HashSet<Appointment>();
		this.subjects_with_rates = subjects_with_rates;
		this.name = name;
	}

	public void changeRateToSubject(String subject, Double new_rate) {
		subjects_with_rates.put(subject, new_rate);
	}

	public void notifyStudent(Appointment appointment) throws RemoteException {
		if (waiting_list.containsKey(appointment.getSubject())) {
			waiting_list.get(appointment.getSubject()).forEach(student -> {
				try {
					student.appointmentAvailable(appointment);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			});
		}
	}

	public Appointment createAppointment(LocalDateTime initial_time, LocalDateTime final_time, String subject)
			throws RemoteException {
		Appointment new_appointment = new Appointment(initial_time, final_time, subject, this);
		appointments.add(new_appointment);
		notifyStudent(new_appointment);
		return new_appointment;
	}

	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	public Set<Appointment> getAppointments() {
		return appointments;
	}

	public Map<String, Double> getSubjectsWithRates() {
		return subjects_with_rates;
	}

	// public void addFuture_appointments(Appointment appointment) {
	// 	// waiting_list.get(appointment.get_subject()).remove(appointment.get_student());
	// 	appointments.add(appointment);
	// }

	public void addSubjectWithRates(String subject, Double value) {
		subjects_with_rates.put(subject, value);
		server.addNewSubjectToTeacher(this, subject);
	}

	// public void removeSubject(String subject) {
	// 	subjects_with_rates.remove(subject);
	// 	server.removeSubjectFromTeacher(this, subject);
	// }

	@Override
	public void addStudentToWaitingList(IStudent student, String subject) throws RemoteException {
		if (waiting_list.containsKey(subject)) {
			waiting_list.get(subject).add(student);
		} else {
			Set<IStudent> students = new HashSet<IStudent>();
			students.add(student);
			waiting_list.put(subject, students);
		}
		System.out.println("For teacher " + name + ", student " + student.getName() + " added to waiting list. Size: "
				+ waiting_list.get(subject).size());
	}

	@Override
	public void removeStudentFromWaitingList(IStudent student, String subject) throws RemoteException {
		
		if (waiting_list.containsKey(subject)) {
			waiting_list.get(subject).remove(student);
			System.out.println(student.getName() + " removed from waiting list for subject " + subject);
		}
		
	}

	// @Override
	public Set<IAppointment> checkAvailability(String subject) throws RemoteException {
		// Set<IAppointment> appointments = new TreeSet<IAppointment>(new
		// AppointmentComparator());
		Set<IAppointment> appointments_available = new HashSet<IAppointment>();

		for (Appointment appointment : appointments) {
			if (appointment.getStudent() == null && appointment.getSubject().equals(subject)) {
				appointments_available.add(appointment);
			}
		}
		return appointments_available;
	}

	@Override
	public String to_string() {
		return "Teacher " + name + " with subjects: " + subjects_with_rates.toString();
	}

	@Override
	public IAppointment getAppointment(String appString) throws RemoteException{
		for (Appointment app : appointments) {
			if (app.to_string().equals(appString)) {
				return app;
			}
		}
		return null;
	}

}
