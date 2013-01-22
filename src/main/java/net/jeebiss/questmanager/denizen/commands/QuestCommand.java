package net.jeebiss.questmanager.denizen.commands;

import org.bukkit.entity.Player;

import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.npc.DenizenNPC;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.aufdemrand.denizen.utilities.arguments.aH;
import net.aufdemrand.denizen.utilities.arguments.aH.ArgumentType;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizen.utilities.debugging.dB.Messages;
import net.jeebiss.questmanager.quests.QuestController;

public class QuestCommand extends AbstractCommand{
	private enum QuestType { START, FINISH, FAIL, CONTINUE}
	
	@Override
	public void parseArgs(ScriptEntry scriptEntry)
			throws InvalidArgumentsException {
		String questName = null;
		for (String arg : scriptEntry.getArguments()) {
			if (aH.matchesArg("START, FINISH, FAIL, CONTINUE",  arg)){
				try {
					scriptEntry.addObject("TYPE", QuestType.valueOf(aH.getStringFrom(arg)));
					dB.echoDebug("...set TYPE to: " + aH.getStringFrom(arg));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (aH.matchesScript (arg)) {
				scriptEntry.addObject("scriptName", aH.getStringFrom(arg));
				dB.echoDebug("...set SCRIPT to use '%s'", aH.getStringFrom(arg));
			} else if (aH.matchesValueArg("NAME", arg, ArgumentType.String)) {
				questName = aH.getStringFrom(arg);
				scriptEntry.addObject("questName", questName);
				dB.echoDebug("...set NAME to use '%s'", questName);
			}
			else {
				dB.echoError("Invalid argument: " + arg);
				throw new InvalidArgumentsException(Messages.ERROR_UNKNOWN_ARGUMENT, arg);
			}
		}

		//
		// If we didn't find a quest name, and there's a "STEP" script entry, then
		// use that as the quest name.
		//
		if (questName == null && scriptEntry.getStep() != null) {
			scriptEntry.addObject ("questName", scriptEntry.getStep ());
		}
	}

	@Override
	public void execute(ScriptEntry scriptEntry)
			throws CommandExecutionException {
		switch ((QuestType)scriptEntry.getObject("TYPE")) {
		case START:
			dB.echoDebug("Starting new Quest.");
			new QuestController((String) scriptEntry.getObject("scriptName"), 
					(String) scriptEntry.getObject("questName"), 
					(Player) scriptEntry.getObject("player"), 
					(DenizenNPC) scriptEntry.getObject("npc"));
			break;

		case FINISH:
			break;

		case FAIL:
			break;
		
		//
		// This should move to the next chapter in the quest.
		//
		case CONTINUE:
			dB.echoApproval("Continuing on to the next chapter in quest:  " + (String)scriptEntry.getObject ("questName"));
			new QuestController((String) scriptEntry.getObject("scriptName"), 
					(String) scriptEntry.getObject("questName"), 
					(Player) scriptEntry.getObject("player"), 
					(DenizenNPC) scriptEntry.getObject("npc"));
			break;
		}
		
	}
}
