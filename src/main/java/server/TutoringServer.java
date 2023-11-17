package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import interfaces.ITeacher;
import interfaces.ITutoringServer;

public class TutoringServer extends UnicastRemoteObject implements ITutoringServer {

	private HashMap<String, Set<Teacher>> teachers_per_subjects;

	protected TutoringServer() throws RemoteException {
		super();
		this.teachers_per_subjects = new HashMap<String, Set<Teacher>>();
	}

	@Override
	public Set<ITeacher> search_for_teachers_for_specific_subject(String subject) throws RemoteException {
		Set<ITeacher> teachers_available = new HashSet<>();
		for (Teacher teacher : teachers_per_subjects.get(subject)) {
			teachers_available.add(teacher);
		}
		return teachers_available;
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

	public void add_teacher(Teacher teacher) {
		Map<String, Double> subjects_with_rates_of_teacher = teacher.getSubjects_with_rates();
		for (Map.Entry<String, Double> entry : subjects_with_rates_of_teacher.entrySet()) {
			if (!teachers_per_subjects.containsKey(entry.getKey())) {
				teachers_per_subjects.put(entry.getKey(), new HashSet<Teacher>());
			}
			teachers_per_subjects.get(entry.getKey()).add(teacher);
		}

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

}
