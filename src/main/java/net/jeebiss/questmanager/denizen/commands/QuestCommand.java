package net.jeebiss.questmanager.denizen.commands;

import org.bukkit.entity.Player;

import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
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
					QuestType type = QuestType.valueOf(aH.getStringFrom(arg));
					scriptEntry.addObject("TYPE", type);
					dB.echoDebug("...set TYPE to: " + type.name());
				} catch (Exception e) {e.printStackTrace();}
			} else if (aH.matchesScript (arg)) {
					String scriptName = aH.getStringFrom(arg);
					scriptEntry.addObject("scriptName", scriptName);
					dB.echoDebug("...set SCRIPT to use '%s'", scriptName);
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
					scriptEntry.getPlayer(),
					scriptEntry.getNPC());
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
			new QuestController(scriptEntry.getScript().getName(), // <--- This getScript() doesnt account for the command outside the quest
					(String) scriptEntry.getObject("questName"), 
					scriptEntry.getPlayer(),
					scriptEntry.getNPC());
			break;
		}
		
	}
}
