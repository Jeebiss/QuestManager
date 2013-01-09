package net.jeebiss.questmanager.denizen.requirements;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.aufdemrand.denizen.exceptions.RequirementCheckException;
import net.aufdemrand.denizen.npc.DenizenNPC;
import net.aufdemrand.denizen.scripts.requirements.AbstractRequirement;
import net.aufdemrand.denizen.utilities.arguments.aH;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.jeebiss.questmanager.QuestManager;

public class QuestRequirement extends AbstractRequirement{
	private enum ScriptType { FINISHED, STARTED, FAILED }
	@Override
	public boolean check(Player player, DenizenNPC npc, String scriptName,
			List<String> args) throws RequirementCheckException {
		
		// Requirement will check if full Quest, or specific
		// Quest Chapter is FINISHED, STARTED, or FAILED and
		// will be compatible with the denizen scripts. Using
		// a hyphen will denote that the requirement NOT be met.
		
		QuestManager QM = (QuestManager) Bukkit.getServer().getPluginManager().getPlugin("Quest Manager");
		
		ScriptType TYPE = ScriptType.FINISHED;
		
		Boolean outcome = false;
		Integer quantity = 1;
		String[] path;
		String quest = null;
		String chapter = null;
		String playerName = player.getName();
		String questStatus = null;
		String chapterStatus = null;
		
		for (String arg : args) {
			if (aH.matchesArg("FINISHED, STARTED, FAILED", arg)) {
				try {
					TYPE = ScriptType.valueOf(aH.getStringFrom(arg));
					dB.echoDebug("...set TYPE to: " + TYPE.name());
				} catch (Exception e) {e.printStackTrace();}
			} else if (aH.matchesQuantity(arg)) {
				quantity = aH.getIntegerFrom(arg);
				dB.echoDebug("...set quantity to: " + quantity);
			} else {
				if (arg.contains(".")) {
					path = arg.split("\\.");
					quest = path[0];
					chapter = path[1];
				} else quest = arg;
			}
		
		}
		
		// if the quest doesnt exist in the Player's
		// node in the saves.yml, its neither FINISHED,
		// FAILED, or STARTED.
		
		if (QM.getSaves().contains("Quests." + playerName + "." + quest)) {
			//if 'quest' exists, then it has a Status:
			questStatus = QM.getSaves().getString("Quests." + playerName + "." + quest + ".Status");
			
			//if 'chapter' is null, then the user only require the 'quest' status
			if (chapter == null) {
				//if 'questStatus' is equal to the type of check
				// then the requirement is met, and return the
				// outcome of the requirement
				if (questStatus == TYPE.name()) outcome = true;
				return outcome;
			// If the chapter exists, then it has a status to check
			} else if (QM.getSaves().contains("Quests." + playerName + "." + quest + "." + chapter)) {
				// if 'chapter' exists, then it has a Status:
				chapterStatus = QM.getSaves().getString("Quests." + playerName + "." + quest + "." + chapter + ".Status");
				
				// if the 'chapterStatus' is equal to the type of
				// check, then the requirement is met. Then return
				// the outcome.
				if (chapterStatus == TYPE.name()) outcome = true;
			} 
			
		} else outcome = false;
		
		return outcome;
	}

}
