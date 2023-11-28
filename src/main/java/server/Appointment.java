package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import interfaces.IAppointment;
import interfaces.IStudent;
import interfaces.ITeacher;

public class Appointment extends UnicastRemoteObject implements IAppointment {

	final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private LocalDateTime initial_time;
	private LocalDateTime final_time;
	private String subject;
	private ITeacher teacher;
	private IStudent student;

	protected Appointment(LocalDateTime initial_time, LocalDateTime final_time, String subject, ITeacher teacher)
			throws RemoteException {
		super();
		this.initial_time = initial_time;
		this.final_time = final_time;
		this.subject = subject;
		this.teacher = teacher;
		this.student = null;
	}

	public IStudent getStudent() {
		return student;
	}
	
	// public void setInitial_time(LocalDateTime initial_time) {
	// 	this.initial_time = initial_time;
	// }

	public LocalDateTime getFinalTime() {
		return final_time;
	}

	// public void setFinal_time(LocalDateTime final_time) {
	// 	this.final_time = final_time;
	// }

	@Override
	public LocalDateTime getInitialTime() {
		return initial_time;
	}

	@Override
	public String bookAppointment(IStudent student) throws RemoteException {
		if (this.student != null) {
			return "Appointment already booked.";
		}
		this.student = student;
		student.addAppointment(this);
		String message = "Appointment for " + subject + " with teacher " + teacher.getName() + " booked for student: "
				+ student.getName() + " on " + initial_time.format(CUSTOM_FORMATTER) + " to "
				+ final_time.format(CUSTOM_FORMATTER);
		System.err.println(message);
		return message;
	}

	@Override
	public void cancelAppointment() throws RemoteException {
		this.student = null;
	}

	@Override
	public String to_string() throws RemoteException {
		if (student != null)
			return "Appointment [initial_time=" + initial_time.format(CUSTOM_FORMATTER) + ", final_time="
					+ final_time.format(CUSTOM_FORMATTER) + ", subject=" + subject
					+ ",\nteacher=" + teacher.getName() + ", student=" + student.getName() + "]";
		else
			return "Appointment [initial_time=" + initial_time.format(CUSTOM_FORMATTER) + ", final_time="
					+ final_time.format(CUSTOM_FORMATTER) + ", subject=" + subject
					+ ",\nteacher=" + teacher.getName() + ", student= No student]";
	}

	@Override
	public String getSubject() throws RemoteException{
		return subject;
	}

	@Override
	public ITeacher getTeacher() throws RemoteException{
		return teacher;
	}

	
}
