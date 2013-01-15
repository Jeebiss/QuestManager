package net.jeebiss.questmanager.quests;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Goal {

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public enum  Status { INPROGRESS, COMPLETED, CANCELLED }
	
	Status status;
	
	public Goal() {
		status = Status.INPROGRESS;
	}
	 
	public void setStatus (Status status) {
		pcs.firePropertyChange("status", this.status, status);
		this.status = status;
	}
	
	public Status getStatus () {
		return status;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
}
