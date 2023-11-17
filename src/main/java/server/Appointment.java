package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;

import interfaces.IAppointment;
import interfaces.IStudent;
import interfaces.ITeacher;

public class Appointment extends UnicastRemoteObject implements IAppointment {
	
	private LocalDateTime initial_time;
	private LocalDateTime final_time;
	private String subject;
	private ITeacher teacher;
	private IStudent student;
	
	
	protected Appointment(LocalDateTime initial_time, LocalDateTime final_time, String subject, ITeacher teacher) throws RemoteException {
		super();
		this.initial_time = initial_time;
		this.final_time = final_time;
		this.subject = subject;
		this.teacher = teacher;
		this.student = null;
	}
	
	@Override
	public LocalDateTime getInitial_time() {
		return initial_time;
	}

	public IStudent get_student() {
		return student;
	}

	public String get_subject() {
		return subject;
	}

	public ITeacher get_teacher() {
		return teacher;
	}

	public void setInitial_time(LocalDateTime initial_time) {
		this.initial_time = initial_time;
	}

	public LocalDateTime getFinal_time() {
		return final_time;
	}

	public void setFinal_time(LocalDateTime final_time) {
		this.final_time = final_time;
	}

	@Override
	public void book_appointment(IStudent student) throws RemoteException {
		this.student = student;
	}

	@Override
	public void cancel_appointment() throws RemoteException {
		this.student=null;
	}

	@Override
	public String to_string() throws RemoteException{
		return "Appointment [initial_time=" + initial_time + ", final_time=" + final_time + ", subject=" + subject
				+ ", teacher=" + teacher + ", student=" + student + "]";
	}
}


