package net.jeebiss.questmanager.quests;

import java.util.Set;

import org.bukkit.Bukkit;

import net.aufdemrand.denizen.Denizen;
import net.aufdemrand.denizen.utilities.debugging.dB;

public class QuestController {

	Denizen denizen = (Denizen) Bukkit.getPluginManager().getPlugin("Denizen");
	
	public QuestController(String scriptName) {
		dB.echoDebug("Creating a controller for: " + scriptName);
		//denizen.getScripts();
	}
	
}
