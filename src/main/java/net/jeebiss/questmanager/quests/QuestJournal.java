package net.jeebiss.questmanager.quests;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.jeebiss.questmanager.quests.QuestChapter.Status;

public class QuestJournal {
	private	Map<String,Quest>	quests;

	/**
	 * Create a new quest journal.
	 */
	public QuestJournal() {
		quests = new HashMap<String,Quest>  ();
	}

	/**
	 * Returns the map of Quest Names to quest Objects.  The returned object is
	 * unmodifiable.
	 * 
	 * @return	The map of quest names to quest objects.
	 */
	public Map<String,Quest> getQuests () {
		return Collections.unmodifiableMap(this.quests);
	}
	
	/**
	 * Returns whether or not the quest journal contains a particular quest.
	 * 
	 * @param questName	The name of the quest to check for.
	 * 
	 * @return	True if the quest journal contains the quest, false otherwise.
	 */
	public boolean hasQuest (String questName) {
		if (questName == null) {
			return false;
		}
		return this.quests.containsKey(questName.toUpperCase());
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
		Quest	quest = new Quest (questName.toUpperCase(), questScriptName, questDescription);
		this.quests.put (questName.toUpperCase(), quest);
		return quest;
	}
	
	/**
	 * Returns whether or not a player has started a particular quest chapter.
	 * A quest chapter is considered "Started" if the chapter is in any of the
	 * possible states:  Started, Failed, or Finished.
	 * 
	 * @param questName	The name of the quest
	 * @param chapterName	The chapter name to check.
	 * 
	 * @return	True if the quest and quest chapter have been started.
	 */
	public boolean isQuestStarted (String questName, String chapterName) {
		if (questName == null || chapterName == null) {
			return false;
		}
		
		if (this.quests.containsKey(questName.toUpperCase())	&&
				 this.quests.get(questName.toUpperCase()).getChapter(chapterName.toUpperCase()) != null) {
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
	public boolean isQuestFinished (String questName, String chapterName) {
		if (questName == null || chapterName == null) {
			return false;
		}
		
		if (this.quests.containsKey(questName.toUpperCase())		&&
				this.quests.get(questName.toUpperCase()).getChapter(chapterName.toUpperCase()) != null) {
			return this.quests.get(questName.toUpperCase()).getChapter (chapterName.toUpperCase()).getStatus() == Status.FINISHED;
		}
		return false;
	}
}
