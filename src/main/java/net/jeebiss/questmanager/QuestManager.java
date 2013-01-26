package net.jeebiss.questmanager;

import net.aufdemrand.denizen.events.ListenerCancelEvent;
import net.aufdemrand.denizen.events.ListenerFinishEvent;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.citizensnpcs.Citizens;
import net.jeebiss.questmanager.commands.QMCommandHandler;
import net.jeebiss.questmanager.denizen.commands.QuestCommand;
import net.jeebiss.questmanager.denizen.commands.QuestVarCommand;
import net.jeebiss.questmanager.denizen.listeners.ItemDropListenerInstance;
import net.jeebiss.questmanager.denizen.listeners.ItemDropListenerType;
import net.jeebiss.questmanager.denizen.listeners.TravelListenerInstance;
import net.jeebiss.questmanager.denizen.listeners.TravelListenerType;
import net.jeebiss.questmanager.denizen.requirements.QuestRequirement;
import net.jeebiss.questmanager.quests.Goal;
import net.jeebiss.questmanager.quests.QuestJournal;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuestManager extends JavaPlugin implements Listener {
	QMCommandHandler commandHandler;
	Citizens citizens;
	
	//
	// This is a map that maps Players to their quest journals.
	//
	private	Map<Player,QuestJournal>	playerQuestJournals = new HashMap<Player,QuestJournal> ();
	private Map<Player,Map<String,Goal>> playerGoalMap = new HashMap<Player,Map<String,Goal>> ();
	
	/**
	 * Returns a player's quest journal.  If the player does not have a quest
	 * journal, then this will create one for them and return that to the caller.
	 * 
	 * @param player	The player to get the quest journal for.
	 * 
	 * @return	The player's quest journal.
	 */
	public QuestJournal getQuestJournal (Player player) {
		QuestJournal qj = this.playerQuestJournals.get(player);
		if (qj == null) {
			qj = new QuestJournal();
			this.playerQuestJournals.put(player, qj);
		}
		return qj;
	}

	@Override
	public void onEnable() {
		//load saves.yml
		reloadSaves();
		
		//Register Citizens 2 command handling
		citizens = (Citizens) Bukkit.getServer().getPluginManager().getPlugin("Citizens");
		commandHandler = new QMCommandHandler(citizens);
		
		//Register DSCRIPT LISTENERs with Denizen
		new TravelListenerType().activate().as("TRAVEL").withClass(TravelListenerInstance.class);
		new ItemDropListenerType().activate().as("ITEMDROP").withClass(ItemDropListenerInstance.class);
		
		//Register DSCRIPT COMMANDS with Denizen
		new QuestCommand().activate().as("QUEST").withOptions("To Be Determined", 1);
		new QuestVarCommand().activate().as("QUESTVAR").withOptions("[NAME:Quest Script Name] [VARIABLE:Variable Name] [VALUE:Value]", 3);
		
		//Register DSCRIPT REQUIREMENTS with Denizen
		new QuestRequirement().activate().as("QUEST").withOptions("[STARTED|FAILED|FINISHED|CONTINUE] [Quest(.Chapter)]", 2);

		//
		// Register to receive events.
		//
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {
		Citizens citizens = (Citizens) getServer().getPluginManager().getPlugin("Citizens");
		return citizens.onCommand(sender, cmd, cmdName, args);
	}

	@Override
	public void onDisable() {
		saveSaves();
	}

	// Initialize Bukkit File Configuration Fields
	private FileConfiguration savesConfig = null;
	private File savesConfigFile = null;

	/**
	 * Syncs the current copy in memory of 'saves' from the disk. Does not invoke
	 * a save first, so any changes that are in memory that have not yet been
	 * saved to disk may be lost.
	 *
	 */
	public void reloadSaves() {
		if (savesConfigFile == null) {
			savesConfigFile = new File(getDataFolder(), "saves.yml");
		}
		savesConfig = YamlConfiguration.loadConfiguration(savesConfigFile);
	}

	/**
	 * Retrieves a manageable keyed collection of the contents of the 'saves'
	 * in memory.
	 *
	 * @return the current saves
	 */
	public FileConfiguration getSaves() {
		if (savesConfig == null) {
			reloadSaves();
		}
		return savesConfig;
	}

	/**
	 * Saves the copy of 'saves' in memory to the disk as 'saves.yml'. If any changes
	 * in the disk saves.yml have not been synced with the copy in memory with
	 * {@link #reloadSaves()}, they may be lost.
	 *
	 */
	public void saveSaves() {
		if (savesConfig == null || savesConfigFile == null) {
			return;
		}
		try {
			savesConfig.save(savesConfigFile);
		} catch (IOException ex) {
			Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save config to " + savesConfigFile, ex);
		}
	}

	/**
	 * This method will add a goal to the goal listener map and use the listener
	 * ID as the key in the map.
	 * 
	 * @param player	The player the goal is for.
	 * @param listenerId	The listener ID.
	 * @param goal	The goal to be listened for.
	 */
	public void addGoal (Player player, String listenerId, Goal goal) {
		dB.echoDebug ("QuestManager:  adding goal with listener id of: " + listenerId);
		Map<String,Goal> playerGoals = this.playerGoalMap.get (player);
		if (playerGoals == null) {
			playerGoals = new HashMap<String,Goal> ();
			this.playerGoalMap.put (player, playerGoals);
		}
		
		playerGoals.put (listenerId, goal);
	}

  /**
   * This method will be called when a player finishes a goal.
   * 
   * @param finishedevent	The finished event.
   */
  @EventHandler
  public void goalReached (ListenerFinishEvent finishedEvent) {
	  String listenerID = finishedEvent.getId();
		Map<String,Goal> playerGoals = this.playerGoalMap.get (finishedEvent.getPlayer());
	  Goal g = playerGoals.remove (listenerID);
	  if (g != null) {
		  g.setStatus(Goal.Status.COMPLETED);
	  } else {
	  	dB.echoError("The goal with listener ID " + listenerID + " was not found.");
	  }
  	dB.echoDebug ("goalReached (" + finishedEvent + ")");
  }
	
  /**
   * This method will be called if a listener event is cancelled.
   * 
   * @param cancelledEvent	The canceled event
   */
  @EventHandler
  public void goalCancelled (ListenerCancelEvent cancelledEvent) {
	  String listenerID = cancelledEvent.getId();
		Map<String,Goal> playerGoals = this.playerGoalMap.get (cancelledEvent.getPlayer());
	  Goal g = playerGoals.remove (listenerID);
	  if (g != null) {
	  	g.setStatus(Goal.Status.CANCELLED);
	  } else {
	  	dB.echoError("The goal with listener ID " + listenerID + " was not found.");
	  }
  	dB.echoDebug ("goalCancelled (" + cancelledEvent + ")");
  }
}
