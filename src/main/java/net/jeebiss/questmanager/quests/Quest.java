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
	private	String	description;
	private	List<QuestChapter>	chapters;
	
	/**
	 * Creates a new Quest object using the name of the quest and the quest
	 * manager/denizen script name.
	 * 
	 * @param name	The name of the quest.
	 * @param scriptName	The script name of the quest.
	 * @param	description	The description of the quest.
	 */
	public Quest (String name, String scriptName, String description) {
		this.name = name;
		this.scriptName = scriptName;
		this.description = description;
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
	 * Returns the description of the quest.
	 * 
	 * @return	The description of the quest.
	 */
	public String getDescription () {
		return this.description;
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
	
	/**
	 * Creates a new quest chapter with the specified name and completion status.
	 * 
	 * @param chapterName	The name of the chapter.
	 * @param complete	True if the chapter is complete, false otherwise.
	 * 
	 * @return	The newly created quest chapter.
	 */
	public QuestChapter addChapter (String chapterName, boolean complete) {
		QuestChapter	chapter	= new QuestChapter (chapterName, complete);
		this.chapters.add(chapter);
		return chapter;
	}
	
	/**
	 * Returns the chapter that has the given chapter name, or null if the
	 * chapter name does not exist in the quest.
	 * 
	 * @param chapterName	The name of the chapter to get.
	 * 
	 * @return	The chapter associated to the specified name, or null if it is
	 * 					not contained in the quest.
	 */
	public QuestChapter getChapter (String chapterName) {
		for (QuestChapter chapter : this.chapters) {
			if (chapter.getName().equalsIgnoreCase(chapterName)) {
				return chapter;
			}
		}
		return null;
	}
}
