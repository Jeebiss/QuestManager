package net.jeebiss.questmanager.quests;

import java.util.HashMap;
import java.util.Map;

public class QuestJournal {
	private	Map<String,Quest>	quests;

	/**
	 * Create a new quest journal.
	 */
	public QuestJournal() {
		quests = new HashMap<String,Quest>  ();
	}

	/**
	 * Returns the map of Quest Names to quest Objects.
	 * 
	 * @return	The map of quest names to quest objects.
	 */
	public Map<String,Quest> getQuests () {
		// TODO:  This should be made immutable.
		return this.quests;
	}
	
	/**
	 * Adds a new quest to the journal and returns the quest object.
	 * 
	 * @param questName	The name of the quest to add.
	 * @param questScriptName	The quest's script name to add
	 * 
	 * @return	The newly created quest object.
	 */
	public Quest addQuest (String questName, String questScriptName, String questDescription) {
		Quest	quest = new Quest (questName, questScriptName, questDescription);
		this.quests.put (questName, quest);
		return quest;
	}
	
	/**
	 * Returns whether or not a player has started a particular quest chapter.
	 * 
	 * @param questName	The name of the quest
	 * @param chapterName	The chapter name to check.
	 * 
	 * @return	True if the quest and quest chapter have been started.
	 */
	public boolean isQuestStarted (String questName, String chapterName) {
		if (this.quests.containsKey(questName)		&&
				 this.quests.get(questName).getChapter(chapterName) != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns whether or not a player has completed a particular quest chapter.
	 * 
	 * @param questName	The name of the quest.
	 * @param chapterName	The chapter name of the quest.
	 * 
	 * @return	True if the player has completed the quest, false otherwise.
	 */
	public boolean isQuestFinshed (String questName, String chapterName) {
		if (this.quests.containsKey(questName)		&&
				this.quests.get(questName).getChapter(chapterName) != null) {
			return this.quests.get(questName).getChapter (chapterName).isComplete();
		}
		return false;
	}
}
