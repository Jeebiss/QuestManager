package net.jeebiss.questmanager.denizen.commands;

import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.aufdemrand.denizen.utilities.arguments.aH;
import net.aufdemrand.denizen.utilities.arguments.aH.ArgumentType;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizen.utilities.debugging.dB.Messages;

public class QuestVarCommand extends AbstractCommand{
	
	String variable;
	String scriptName;
	
	@Override
	public void parseArgs(ScriptEntry scriptEntry)
			throws InvalidArgumentsException {
		for (String arg : scriptEntry.getArguments()) {
			if (aH.matchesValueArg("NAME", arg, ArgumentType.String)){
				scriptName = aH.getStringFrom(arg);
				dB.echoDebug("...script name: " + scriptName);
			} else if (aH.matchesValueArg("VARIABLE, VAR", arg, ArgumentType.String)) {
				variable = aH.getStringFrom(arg);
				dB.echoDebug("...variable set to: " + variable);
			} else if (aH.matchesValueArg("VALUE, VAL", arg, ArgumentType.Custom)) {
				try{
					Integer value = Integer.valueOf (aH.getIntegerFrom(arg));
					//Is this the proper way to add 'value'?
					scriptEntry.addObject("value", value);
					dB.echoDebug("...variable value set to: " + value);
				}catch (NumberFormatException nfe) {
					String value = aH.getStringFrom(arg);
					//Is this the proper way to add 'value'?
					scriptEntry.addObject("value", value);
					dB.echoDebug("...variable value set to: " + value);
				}
			} else throw new InvalidArgumentsException(Messages.ERROR_UNKNOWN_ARGUMENT, arg);
		}

		scriptEntry.addObject("Name", scriptName);
		scriptEntry.addObject("Variable", variable);
	}

	@Override
	public void execute(ScriptEntry scriptEntry)
			throws CommandExecutionException {
		// not entirely sure what to do with the variables, but they
		// are atleast set in parseArgs().
	}
}
