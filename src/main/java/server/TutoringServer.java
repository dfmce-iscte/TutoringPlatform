package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

import interfaces.IAppointment;
import interfaces.ITeacher;
import interfaces.ITutoringServer;

public class TutoringServer extends UnicastRemoteObject implements ITutoringServer {

	private HashMap<String, Set<Teacher>> teachers_per_subjects;
	private int last_teacher_id_used;

	protected TutoringServer() throws RemoteException {
		super();
		this.teachers_per_subjects = new HashMap<String, Set<Teacher>>();
		this.last_teacher_id_used = 0;
	}

	@Override
	public Map<ITeacher,Set<IAppointment>> search_availability_for_specific_subject(String subject) throws RemoteException {
		Map<ITeacher,Set<IAppointment>> teachers_available = new HashMap<>();

		for (Teacher teacher : teachers_per_subjects.get(subject)) {
			teachers_available.put(teacher,teacher.check_availability(subject));
		}
		return teachers_available;
	}

	
	// agora o professor pode ter mais do que uma disciplina mas  se for dorpdown tem que se inicial disciplinas.
	public Teacher add_teacher(Map<String,Double> rates, String name) throws RemoteException {
		Teacher new_teacher = new Teacher(last_teacher_id_used, rates, name);
		last_teacher_id_used++;
		for (Map.Entry<String, Double> entry : rates.entrySet()) {
			if (!teachers_per_subjects.containsKey(entry.getKey())) {
				teachers_per_subjects.put(entry.getKey(), new HashSet<Teacher>());
			}
			teachers_per_subjects.get(entry.getKey()).add(new_teacher);
		}
		return new_teacher;

	}

	public void remove_teacher(Teacher teacher) {
		Map<String, Double> subjects_with_rates_of_teacher = teacher.getSubjects_with_rates();
		for (Map.Entry<String, Double> entry : subjects_with_rates_of_teacher.entrySet()) {
			if (teachers_per_subjects.containsKey(entry.getKey())) {
				teachers_per_subjects.get(entry.getKey()).remove(teacher);
			}
		}
	}

	public void add_new_subject_to_teacher(Teacher teacher,String subject){
		if (!teachers_per_subjects.containsKey(subject)) {
			teachers_per_subjects.put(subject, new HashSet<Teacher>());
		}
		teachers_per_subjects.get(subject).add(teacher);
	}

	public void remove_subject_from_teacher(Teacher teacher,String subject){
		if (teachers_per_subjects.containsKey(subject)) {
			teachers_per_subjects.get(subject).remove(teacher);
		}
	}
	@Override
	public String to_string() throws RemoteException {
		String string = "";
		for (Map.Entry<String, Set<Teacher>> entry : teachers_per_subjects.entrySet()) {
			string += entry.getKey() + ": ";
			for (Teacher teacher : entry.getValue()) {
				string += teacher.to_string() + ", ";
			}
			string += "\n";
		}
		return string;
	}

}
