package net.jeebiss.questmanager.denizenrequirements;

import java.util.List;

import org.bukkit.entity.Player;

import net.aufdemrand.denizen.exceptions.RequirementCheckException;
import net.aufdemrand.denizen.npc.DenizenNPC;
import net.aufdemrand.denizen.scripts.requirements.AbstractRequirement;
import net.aufdemrand.denizen.utilities.arguments.aH;
import net.aufdemrand.denizen.utilities.debugging.dB;

public class QuestRequirement extends AbstractRequirement{
	private enum ScriptType { FINISHED, STARTED, FAILED }
	@Override
	public boolean check(Player player, DenizenNPC npc, String scriptName,
			List<String> args) throws RequirementCheckException {
		
		// Requirement will check if full Quest, or specific
		// Quest Chapter is FINISHED, STARTED, or FAILED and
		// will be compatible with the denizen scripts. Using
		// a hyphen will denote that the requirement NOT be met.
		
		Boolean outcome = false;
		ScriptType TYPE = ScriptType.FINISHED;
		Integer quantity = 1;
		String[] path;
		String quest;
		String chapter;
		
		for (String arg : args) {
			if (aH.matchesArg("FINISHED,  STARTED, FAILED", arg)) {
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
		
		return outcome;
	}

}
