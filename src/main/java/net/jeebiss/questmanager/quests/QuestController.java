package net.jeebiss.questmanager.quests;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.aufdemrand.denizen.Denizen;
import net.aufdemrand.denizen.scripts.ScriptBuilder;
import net.aufdemrand.denizen.scripts.ScriptEngine.QueueType;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizen.utilities.debugging.dB;

public class QuestController {

	Denizen denizen = (Denizen) Bukkit.getPluginManager().getPlugin("Denizen");
	ScriptBuilder scriptBuilder = DenizenAPI.getCurrentInstance().getScriptEngine().getScriptBuilder();
	Set<String> chapters;
	List<String> requirements;
	List<String> introCommands;
	List<String> goals;
	List<String> conclusionCommands;
	List<ScriptEntry> introScriptEntries;
	
	String currentChapter = null;
	
	
	public QuestController(String scriptName, String questName, Player player) {
		dB.echoDebug("Creating a new controller for " + scriptName + " as " + questName);
		
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
		if (denizen.getScripts().contains(scriptName.toUpperCase() + "." + currentChapter + ".Introduction")) {
			introCommands = denizen.getScripts().getStringList(scriptName.toUpperCase() + "." + currentChapter + ".Introduction");
			dB.echoDebug("...Introduction: commands acquired.");
		}
		
		//if Goals: exists, get the list
		if (denizen.getScripts().contains(scriptName.toUpperCase() + "." + currentChapter + ".Goals")) {
			goals = denizen.getScripts().getStringList(scriptName.toUpperCase() + "." + currentChapter + ".Goals");
			dB.echoDebug("...Goals: acquired.");
		}
		
		//if Conclusion: exists, get the list
		if (denizen.getScripts().contains(scriptName.toUpperCase() + "." + currentChapter + ".Conclusion")) {
			conclusionCommands = denizen.getScripts().getStringList(scriptName.toUpperCase() + "." + currentChapter + ".Conclusion");
			dB.echoDebug("...Conclusion: acquired.");
		}
		
		//if we have introCommands, do them before we build all the listeners
		if (introCommands != null) {
			introScriptEntries = scriptBuilder.buildScriptEntries(player, introCommands, scriptName);
			scriptBuilder.queueScriptEntries(player, introScriptEntries, QueueType.PLAYER_TASK);
			dB.echoDebug("...executing Introduction commands");
		}
		
		// time to build all the goals for the
		// chapter. Still need some sort of 
		// finished event.
		//
		//create a new GoalBuilder
		
	}
	
	public String getChapter(String scriptName) {
		//parse over chapters
		for (String chapter : chapters) {
			if (denizen.getScripts().getStringList(scriptName.toUpperCase() + "." + chapter + ".REQUIREMENTS") != null) {
				//if there are requirements, check them.
				requirements = denizen.getScripts().getStringList(scriptName.toUpperCase() + "." + chapter + ".REQUIREMENTS");
				
				if (requirements != null) {
					//send the requirement list to the denizen requirement checker
					dB.echoDebug("...checking requirements for " + chapter);
				}
				
				//TODO properly handle requirements!
				Boolean requirementsMet = false;
				if (requirementsMet == true) {
					return chapter;
				}
			// If there are no requirements, return this chapter.	
			} else {
				return chapter;
			}
		}
		return null;
	}
}
