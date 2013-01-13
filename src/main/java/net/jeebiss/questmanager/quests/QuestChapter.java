package net.jeebiss.questmanager.quests;

/**
 * Represents one chapter in a quest.
 * 
 * @author dbixler
 */
public class QuestChapter {
	private	String	name;
	private	boolean	complete;

	/**
	 * Create a new quest chapter that has not yet been completed.
	 * 
	 * @param name	The name of the chapter to create.
	 */
	public QuestChapter (String name) {
		this.name = name;
		this.complete = false;
	}
	
	/**
	 * Create a new quest chapter with a specified completion status.
	 * 
	 * @param name	The name of the quest chapter.
	 * @param complete	True if the chapter is completed, false if not.
	 */
	public QuestChapter (String name, boolean complete) {
		this (name);
		this.complete = complete;
	}
	
	/**
	 * Returns the name of the quest chapter.
	 * 
	 * @return	The name of the quest chapter.
	 */
	public String getName () {
		return this.name;
	}
	
	/**
	 * Sets the completion status of the quest chapter.
	 * 
	 * @param complete	True if the chapter is completed, false otherwise.
	 */
	public void setComplete (boolean complete) {
		this.complete = complete;
	}
	
	/**
	 * Returns the completion status of this quest chapter.
	 * 
	 * @return	True if the chapter is complete, false otherwise.
	 */
	public boolean isComplete () {
		return this.complete;
	}
}
