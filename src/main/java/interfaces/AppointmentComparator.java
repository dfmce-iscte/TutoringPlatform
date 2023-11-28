package interfaces;

import java.rmi.RemoteException;
import java.util.Comparator;

public class AppointmentComparator implements Comparator<IAppointment> {
	public int compare(IAppointment a1, IAppointment a2) {
		try {
			return a1.getInitialTime().compareTo(a2.getInitialTime());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
