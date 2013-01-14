package net.jeebiss.questmanager.quests;

import java.util.ArrayList;
import java.util.List;

import net.aufdemrand.denizen.exceptions.ScriptEntryCreationException;
import net.aufdemrand.denizen.scripts.ScriptBuilder;
import net.aufdemrand.denizen.scripts.ScriptEngine.QueueType;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.utilities.DenizenAPI;

import org.bukkit.entity.Player;

public class GoalBuilder {
	
	String listenerType;
	String[] args;
	
	List<ScriptEntry> goalList = new ArrayList<ScriptEntry> ();
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
			
			/*
			 * Add the scriptEntry to the list.
			 */
			goalList.add(scriptEntry);
		
		} 
		
		/*
		 * Ues build scriptEntry to build the
		 * listener for the goal.
		 */
		DenizenAPI.getCurrentInstance().getScriptEngine().addToQue(player, goalList, QueueType.PLAYER_TASK);
	}

}
