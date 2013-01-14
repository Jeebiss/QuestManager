package net.jeebiss.questmanager.quests;

public class Goal {

	private String id;
	private Boolean completed;

	public Goal() {
		id = null;
		completed = false;
	}

	public void cancel () {
		
	}
	
	public void setID (String id) {
		this.id = id;
	}
	
	public void setCompleted (Boolean completed) {
		this.completed = completed;
	}
	
	public String getID () {
		return id;
	}
	
	public Boolean isCompleted () {
		return completed;
	}
}
