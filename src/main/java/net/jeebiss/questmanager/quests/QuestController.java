package net.jeebiss.questmanager.quests;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.aufdemrand.denizen.Denizen;
import net.aufdemrand.denizen.npc.DenizenNPC;
import net.aufdemrand.denizen.scripts.ScriptBuilder;
import net.aufdemrand.denizen.scripts.ScriptEngine.QueueType;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.jeebiss.questmanager.QuestManager;
import net.jeebiss.questmanager.quests.QuestChapter.Status;

public class QuestController {
	Denizen denizen = (Denizen) Bukkit.getPluginManager().getPlugin("Denizen");
	ScriptBuilder scriptBuilder = DenizenAPI.getCurrentInstance().getScriptEngine().getScriptBuilder();
	QuestManager	qm = (QuestManager) Bukkit.getPluginManager().getPlugin ("Quest Manager");
	
	Set<String> chapters;
	List<String> requirements;
	List<String> introCommands;
	List<String> goals;
	List<String> conclusionCommands;
	List<ScriptEntry> introScriptEntries;
	
	String currentChapter = null;
	
	public QuestController(String scriptName, String questName, Player player, DenizenNPC npc) {
		dB.echoDebug("Creating a new controller for " + scriptName + " as " + questName);
		
		if (qm == null) {
			throw new RuntimeException ("Unable to locate the QuestManager plugin.");
		}

		//
		// Fetch the player's quest journal.
		//
		QuestJournal qj = qm.getQuestJournal(player);
		
		//get list of chapters
		if (denizen.getScripts().getString(scriptName.toUpperCase() + ".CHAPTERS") != null 
				&& denizen.getScripts().contains(scriptName.toUpperCase() + ".CHAPTERS")) {
			chapters = denizen.getScripts()
					.getConfigurationSection(scriptName.toUpperCase() + ".CHAPTERS").getKeys(false);
		} else  {
			dB.echoDebug("...no Chapters: node found");
			return;
		}
		
		//get current chapter
		currentChapter = getChapter(scriptName);
		
		if (currentChapter == null) {
			dB.echoDebug("...could not find a valid chapter for the given quest.");
			return;
		}
		
		//if Introduction: exists, get the list
		if (denizen.getScripts().contains((scriptName + ".Chapters." + currentChapter + ".Introduction").toUpperCase())) {
			introCommands = denizen.getScripts().getStringList((scriptName + ".Chapters." + currentChapter + ".Introduction").toUpperCase());
			dB.echoDebug("...Introduction: commands acquired.");
		} else dB.echoDebug("...no introduction commands found");
		
		//if Goals: exists, get the list
		if (denizen.getScripts().contains((scriptName + ".Chapters." + currentChapter + ".Goals").toUpperCase())) {
			goals = denizen.getScripts().getStringList((scriptName + ".Chapters." + currentChapter + ".Goals").toUpperCase());
			dB.echoDebug("...Goals: acquired.");
		} else dB.echoDebug("...no goals commands found");
		
		//if Conclusion: exists, get the list
		if (denizen.getScripts().contains((scriptName + ".Chapters." + currentChapter + ".Conclusion").toUpperCase())) {
			conclusionCommands = denizen.getScripts().getStringList((scriptName + ".Chapters." + currentChapter + ".Conclusion").toUpperCase());
			dB.echoDebug("...Conclusion: acquired.");
		} else dB.echoDebug("...no conclusion commands found");
		
		//if we have introCommands, do them before we build all the listeners
		
		//
		// Get the quest from the player's quest journal.  If it does not exist,
		// then we need to create the quest in the player's quest journal.
		//
		Quest	quest = qj.getQuests().get(questName);
		if (quest == null) {
			dB.echoDebug("Creating new quest: " + questName);
			// TODO: get quest description.
			quest = qj.addQuest(questName, scriptName, "");
		}

		//
		// If the chapter does not exist, then create it and added it to the quest
		// that it belongs to in an "uncompleted' status.  If there is an
		// introduction script, then play that script as well.
		//
		QuestChapter chapter = quest.getChapter(currentChapter);
		if (chapter == null) {
			dB.echoDebug ("Creating new chapter: " + currentChapter);
			chapter = quest.addChapter(currentChapter, Status.STARTED);
			GoalBuilder gB = new GoalBuilder(player, goals, chapter);
			if (introCommands != null) {
				//
				// Queue the script in the player's queue.
				//
				scriptBuilder.queueScriptEntries (
						player, 
					scriptBuilder.buildScriptEntries (
							player, 
							npc, 
							introCommands, 
							scriptName, 
							null), 
					QueueType.PLAYER);

				dB.echoDebug("...executing Introduction commands");
				
			}
		}
		
	}
	
	public String getChapter(String scriptName) {
		//parse over chapters
		for (String chapter : chapters) {
			//if there is no requirement list, rrturn the current chapter
			if (!denizen.getScripts().contains(scriptName.toUpperCase() 
					+ "." + chapter + ".REQUIREMENTS.LIST")) {
				dB.echoDebug("...chapter " + chapter + " chosen with no requirments");
				return chapter;
			}
			
			//if there is a List: node, get it.
			requirements = denizen.getScripts().getStringList(scriptName.toUpperCase() + "." + chapter + ".REQUIREMENTS.LIST");
			
			//If the list is empty, return chapter
			if (requirements.isEmpty() || requirements == null ) {
				dB.echoDebug("...chapter " + chapter + " chosen with no requirments");
				return chapter;
			}
							
			//TODO properly handle requirements!
			Boolean requirementsMet = false;
			if (requirementsMet == true) {
				return chapter;
			}	
		}
		return null;
	}
}
