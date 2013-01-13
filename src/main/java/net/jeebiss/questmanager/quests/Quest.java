package net.jeebiss.questmanager.quests;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Quest object that is used to represent a quest in the Quest
 * Manager plugin.
 * 
 * @author dbixler
 */
public class Quest {
	private	String	name;
	private	String	scriptName;
	private	List<QuestChapter>	chapters;
	
	/**
	 * Creates a new Quest object using the name of the quest and the quest
	 * manager/denizen script name.
	 * 
	 * @param name
	 * @param scriptName
	 */
	public Quest (String name, String scriptName) {
		this.name = name;
		this.scriptName = scriptName;
		this.chapters = new ArrayList<QuestChapter> ();
	}
	
	/**
	 * Returns the name of the quest.
	 * 
	 * @return	The name of the quest.
	 */
	public String getName () {
		return this.name;
	}
	
	/**
	 * Returns the script name of this quest.
	 * 
	 * @return	The script name of the quest.
	 */
	public String getScriptName () {
		return this.scriptName;
	}
	
	/**
	 * Returns a list of all chapters in a quest that have been discovered so
	 * far.
	 * 
	 * @return	The list of chapters in a quest.
	 */
	public List<QuestChapter> getChapters () {
		return this.chapters;
	}
}
