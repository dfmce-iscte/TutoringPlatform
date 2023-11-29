package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;

import interfaces.IAppointment;
import interfaces.ITeacher;
import interfaces.ITutoringServer;

public class TutoringServer extends UnicastRemoteObject implements ITutoringServer {

	private HashMap<String, Set<Teacher>> teachers_per_subjects;
	private Set<Teacher> teachers;
	private int last_teacher_id_used;

	protected TutoringServer() throws RemoteException {
		super();
		this.teachers_per_subjects = new HashMap<String, Set<Teacher>>();
		this.teachers = new HashSet<>();
		this.last_teacher_id_used = 0;
	}

	// public Teacher add_teacher_with_rates(Map<String, Double> rates, String name) throws RemoteException {
	// 	Teacher new_teacher = new Teacher(last_teacher_id_used, rates, name);
	// 	last_teacher_id_used++;
	// 	for (Map.Entry<String, Double> entry : rates.entrySet()) {
	// 		if (!teachers_per_subjects.containsKey(entry.getKey())) {
	// 			teachers_per_subjects.put(entry.getKey(), new HashSet<Teacher>());
	// 		}
	// 		teachers_per_subjects.get(entry.getKey()).add(new_teacher);
	// 	}
	// 	return new_teacher;

	// }

	public Teacher addTeacher(String name) throws RemoteException {
		Teacher new_teacher = new Teacher(this,last_teacher_id_used, name);
		teachers.add(new_teacher);
		last_teacher_id_used++;
		return new_teacher;
	}

	public void removeTeacher(Teacher teacher) {
		Map<String, Double> subjects_with_rates_of_teacher = teacher.getSubjectsWithRates();
		for (Map.Entry<String, Double> entry : subjects_with_rates_of_teacher.entrySet()) {
			if (teachers_per_subjects.containsKey(entry.getKey())) {
				teachers_per_subjects.get(entry.getKey()).remove(teacher);
			}
		}
		teachers.remove(teacher);
	}

	public void addNewSubjectToTeacher(Teacher teacher, String subject) {
		if (!teachers_per_subjects.containsKey(subject)) {
			teachers_per_subjects.put(subject, new HashSet<Teacher>());
		}
		teachers_per_subjects.get(subject).add(teacher);
	}

	public void removeSubjectFromTeacher(Teacher teacher, String subject) {
		if (teachers_per_subjects.containsKey(subject)) {
			teachers_per_subjects.get(subject).remove(teacher);
		}
	}

	public Set<Teacher> getTeachers() {
		return teachers;
	}

	@Override
	public Map<ITeacher, Set<IAppointment>> searchAvailabilityForSpecificSubject(String subject)
			throws RemoteException {
		Map<ITeacher, Set<IAppointment>> teachers_available = new HashMap<>();
		System.out.println("Searching availability for subject: " + subject);
		for (Teacher teacher : teachers_per_subjects.get(subject)) {
			teachers_available.put(teacher, teacher.checkAvailability(subject));
		}
		return teachers_available;
	}

	

	@Override
	public List<String> getAllSubjects()  throws RemoteException{
		List<String> subjects = new ArrayList<String>();
		for (String subject : teachers_per_subjects.keySet()) {
			subjects.add(subject);
			
		}
		return subjects;
	}
	@Override	
	public List<IAppointment> getAppointmentsForSubjectAndTeacher(String teacher,String subject) throws RemoteException{
		System.out.println(" teacher "+teacher+" subject "+subject);
		List<IAppointment> appointments = new ArrayList<IAppointment>();
		for (Teacher t : teachers) {
			System.out.println(" name "+t.getName());
			if (t.getName().equals(teacher)) {
				for (Appointment a : t.getAppointments()) {
					if (a.getSubject().equals(subject)) {
						appointments.add(a);
					}
				}
			}
		}
		return appointments;
	}
	@Override
	public ITeacher getTeacherByName(String name)throws RemoteException {
		for (Teacher t : teachers) {
			if (t.getName().equals(name)) {
				return t;
			}
		}
		return null;
	}
	@Override
	public String to_string() {
		String string = "";
		for (Map.Entry<String, Set<Teacher>> entry : teachers_per_subjects.entrySet()) {
			string += entry.getKey() + ": ";
			for (Teacher teacher : entry.getValue()) {
				string += teacher + ", ";
			}
			string += "\n";
		}
		return string;
	}

}
