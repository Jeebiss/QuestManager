package net.jeebiss.questmanager.quests;

import java.util.List;

import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.exceptions.ScriptEntryCreationException;
import net.aufdemrand.denizen.scripts.ScriptBuilder;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.core.ListenCommand;
import net.aufdemrand.denizen.utilities.DenizenAPI;

import org.bukkit.entity.Player;

public class GoalBuilder {
	
	String listenerType;
	String[] args;
	
	ScriptEntry scriptEntry;
	ScriptBuilder scriptBuilder = DenizenAPI.getCurrentInstance().getScriptEngine().getScriptBuilder();
	
	public GoalBuilder(Player player, List<String> commands) {
		// will parse list of goals, and build the proper listeners
		for (String goal : commands) {
			// Break the Goal into arguments for the
			// LISTEN command scriptEntry
			args = scriptBuilder.buildArgs(player, null, goal);
			
			// Build neew scriptEntry with the args
			try {
				scriptEntry = new ScriptEntry("LISTEN", args, player);
			} catch (ScriptEntryCreationException e) {
				e.printStackTrace();
			}
			
			try {
				// Ideally this will parse the args of the given scriptEntry
				DenizenAPI.getCurrentInstance().getCommandRegistry().get(ListenCommand.class).parseArgs(scriptEntry);
				// Does this send the modified scriptEntry from parseArgs into execute?
				DenizenAPI.getCurrentInstance().getCommandRegistry().get(ListenCommand.class).parseArgs(scriptEntry);
			} catch (InvalidArgumentsException e) {
				e.printStackTrace();
			}
		} 
		
	}

}
