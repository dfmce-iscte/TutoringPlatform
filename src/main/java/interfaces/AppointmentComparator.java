package interfaces;

import java.rmi.RemoteException;
import java.util.Comparator;

public class AppointmentComparator implements Comparator<IAppointment> {
	public int compare(IAppointment a1, IAppointment a2) {
		try {
			return a1.getInitial_time().compareTo(a2.getInitial_time());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
