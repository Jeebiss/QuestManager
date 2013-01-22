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
	private QuestType TYPE = null;
	
	private String scriptName = null;
	private String questName = null;
	
	private Player player;
	
	private DenizenNPC npc;
	
	@Override
	public void parseArgs(ScriptEntry scriptEntry)
			throws InvalidArgumentsException {
		for (String arg : scriptEntry.getArguments()) {
			if (aH.matchesArg("START, FINISH, FAIL, CONTINUE",  arg)){
				try {
					TYPE = QuestType.valueOf(aH.getStringFrom(arg));
					dB.echoDebug("...set TYPE to: " + TYPE.name());
				} catch (Exception e) {e.printStackTrace();}
			} else if (aH.matchesScript (arg)) {
					scriptName = aH.getStringFrom(arg);
					dB.echoDebug("...set SCRIPT to use '%s'", scriptName);
			} else if (aH.matchesValueArg("NAME", arg, ArgumentType.String)) {
					questName = aH.getStringFrom(arg);
					dB.echoDebug("...set NAME to use '%s'", questName);
			}
			else {
				dB.echoError("Invalid argument: " + arg);
				throw new InvalidArgumentsException(Messages.ERROR_UNKNOWN_ARGUMENT, arg);
			}

		}

		player = scriptEntry.getPlayer();
		npc = scriptEntry.getNPC();
		
		scriptEntry.addObject("TYPE", TYPE);
		scriptEntry.addObject("scriptName", scriptName);
		scriptEntry.addObject("questName", questName);
		scriptEntry.addObject("player", player);
		scriptEntry.addObject("npc", npc);
	}

	@Override
	public void execute(ScriptEntry scriptEntry)
			throws CommandExecutionException {
		switch ((QuestType)scriptEntry.getObject("TYPE")) {
		case START:
			dB.echoDebug("Starting new Quest.");
			QuestController questController = new QuestController((String) scriptEntry.getObject("scriptName"), 
					(String) scriptEntry.getObject("questName"), (Player) scriptEntry.getObject("player"), (DenizenNPC) scriptEntry.getObject("npc"));
			break;

		case FINISH:
			break;

		case FAIL:
			break;
		
		//
		// This should move to the next chapter in the quest.
		//
		case CONTINUE:
			dB.echoApproval("Continuing on to the next chapter in quest: " + this.questName);
			break;
		}
		
	}
}
