package net.jeebiss.questmanager.quests;

import java.util.List;

import net.aufdemrand.denizen.exceptions.ScriptEntryCreationException;
import net.aufdemrand.denizen.scripts.ScriptBuilder;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.CommandExecuter;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizen.utilities.arguments.aH;
import net.aufdemrand.denizen.utilities.arguments.aH.ArgumentType;
import net.jeebiss.questmanager.QuestManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * This is a utility class to build a list of goals used by Quest Manager to
 * determine when a player has completed a quest chapter.
 * 
 * @author Jeebiss
 */
public class GoalBuilder {
	public GoalBuilder(Player player, List<String> commands, QuestChapter chapter) {
		ScriptBuilder scriptBuilder = DenizenAPI.getCurrentInstance().getScriptEngine().getScriptBuilder();
		CommandExecuter	executor = DenizenAPI.getCurrentInstance().getScriptEngine().getScriptExecuter();
		String[] args = null;
		QuestManager	qm = (QuestManager) Bukkit.getPluginManager().getPlugin ("Quest Manager");

		//
		// For each goal, build the proper listener.
		//
		for (String goal : commands) {
			//
			// Break the Goal into arguments for the LISTEN command scriptEntry and
			// execute the script.
			//
			try {
				args = scriptBuilder.buildArgs(player, null, goal);
				executor.execute(new ScriptEntry("LISTEN", args, player));
			} catch (ScriptEntryCreationException e) {
				e.printStackTrace();
			}
			
			Goal newGoal = new Goal();
			String listenerId = null;
			for (String arg : args) {
				if (aH.matchesValueArg("ID", arg, ArgumentType.String)) {
					listenerId = arg;
					break;
				}
			}
			
			qm.addGoal(listenerId, newGoal);
			chapter.addGoal(newGoal);
		}
	}
}
