package net.jeebiss.questmanager.quests;

import java.util.Set;

import org.bukkit.Bukkit;

import net.aufdemrand.denizen.Denizen;
import net.aufdemrand.denizen.utilities.debugging.dB;

public class QuestController {

	Denizen denizen = (Denizen) Bukkit.getPluginManager().getPlugin("Denizen");
	Set<String> chapters;
	
	public QuestController(String scriptName, String questName) {
		dB.echoDebug("Creating a controller for " + scriptName + " as " + questName);
		
		//get list of chapters
		if (denizen.getScripts().getString(scriptName.toUpperCase() + ".CHAPTERS") != null 
				&& denizen.getScripts().contains(scriptName.toUpperCase() + ".CHAPTERS")) {
			chapters = denizen.getScripts()
					.getConfigurationSection(scriptName.toUpperCase() + ".CHAPTERS").getKeys(false);
		}
	}
}
