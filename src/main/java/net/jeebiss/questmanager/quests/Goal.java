package net.jeebiss.questmanager.quests;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import net.aufdemrand.denizen.utilities.debugging.dB;

public class Goal {

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public enum  Status { INPROGRESS, COMPLETED, CANCELLED }
	
	Status status;
	
	public Goal() {
		status = Status.INPROGRESS;
	}
	 
	public void setStatus (Status status) {
		dB.echoDebug("...firing Goal status change event");
		pcs.firePropertyChange("status", this.status, status);
		this.status = status;
	}
	
	public Status getStatus () {
		return status;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		dB.echoDebug("...adding pcs listener in Goal");
		pcs.addPropertyChangeListener(listener);
	}
}
