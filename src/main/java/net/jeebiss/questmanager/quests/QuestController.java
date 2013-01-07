package net.jeebiss.questmanager.quests;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;

import net.aufdemrand.denizen.Denizen;
import net.aufdemrand.denizen.utilities.debugging.dB;

public class QuestController {

	Denizen denizen = (Denizen) Bukkit.getPluginManager().getPlugin("Denizen");
	
	Set<String> chapters;
	List<String> requirements;
	
	String currentChapter = null;
	
	
	public QuestController(String scriptName, String questName) {
		dB.echoDebug("Creating a controller for " + scriptName + " as " + questName);
		
		//get list of chapters
		if (denizen.getScripts().getString(scriptName.toUpperCase() + ".CHAPTERS") != null 
				&& denizen.getScripts().contains(scriptName.toUpperCase() + ".CHAPTERS")) {
			chapters = denizen.getScripts()
					.getConfigurationSection(scriptName.toUpperCase() + ".CHAPTERS").getKeys(false);
		} else  {
			dB.echoDebug("...no Chapters: node found");
			return;
		}
		
		currentChapter = findChapter(scriptName);
	}
	
	public String findChapter(String scriptName) {
		//parse over chapters
		for (String chapter : chapters) {
			if (denizen.getScripts().getString(scriptName.toUpperCase() + "." + chapter + ".REQUIREMENTS") != null) {
				requirements = denizen.getScripts().getStringList(scriptName.toUpperCase() + "." + chapter + ".REQUIREMENTS");
				
				for (String requirement : requirements) {
					// check requirements at some point
				}
			} else return chapter;
		}
		return null;
	}
}
