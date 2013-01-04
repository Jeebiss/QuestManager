package net.jeebiss.questmanager.denizencommands;

import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.aufdemrand.denizen.utilities.arguments.aH;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizen.utilities.debugging.dB.Messages;

public class QuestCommand extends AbstractCommand{
	private enum QuestType { START, FINISH, FAIL }
	private QuestType TYPE = null;
	
	@Override
	public void parseArgs(ScriptEntry scriptEntry)
			throws InvalidArgumentsException {
		for (String arg : scriptEntry.getArguments()) {
			if (aH.matchesArg("START, FINISH, FAIL",  arg)){
				try {
					TYPE = QuestType.valueOf(aH.getStringFrom(arg));
					dB.echoDebug("...set TYPE to: " + TYPE.name());
				} catch (Exception e) {e.printStackTrace();}
			} else throw new InvalidArgumentsException(Messages.ERROR_UNKNOWN_ARGUMENT, arg);
		}
		
		scriptEntry.addObject("TYPE", TYPE);
	}

	@Override
	public void execute(ScriptEntry scriptEntry)
			throws CommandExecutionException {
		switch ((QuestType)scriptEntry.getObject("TYPE")) {
		case START:
			break;

		case FINISH:
			break;

		case FAIL:
			break;
		}
		
	}
}
