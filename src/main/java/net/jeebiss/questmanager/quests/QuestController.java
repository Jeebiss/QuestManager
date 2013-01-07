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
	
	String playerChapter = null;
	
	
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
		
		playerChapter = findChapter(scriptName);
	}
	
	public String findChapter(String scriptName) {
		String currentChapter = null;
		
		//parse over chapters
		for (String chapter : chapters) {
			if (denizen.getScripts().getString(scriptName.toUpperCase() + "." + chapter + ".REQUIREMENTS") != null) {
				requirements = denizen.getScripts().getStringList(scriptName.toUpperCase() + "." + chapter + ".REQUIREMENTS");
			} else currentChapter = chapter;
		}
		
		return currentChapter;
	}
}
