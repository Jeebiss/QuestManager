package net.jeebiss.questmanager;

import net.citizensnpcs.Citizens;
import net.jeebiss.questmanager.commands.QMCommandHandler;
import net.jeebiss.questmanager.denizen.commands.QuestCommand;
import net.jeebiss.questmanager.denizen.commands.QuestVarCommand;
import net.jeebiss.questmanager.denizen.listeners.TravelListenerInstance;
import net.jeebiss.questmanager.denizen.listeners.TravelListenerType;
import net.jeebiss.questmanager.denizen.requirements.QuestRequirement;
import net.jeebiss.questmanager.quests.QuestJournal;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuestManager extends JavaPlugin {
	
	QMCommandHandler commandHandler;
	Citizens citizens;
	
	private	Map<Player,QuestJournal>	playerQuestJournals = new HashMap<Player,QuestJournal> ();
	
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
		new TravelListenerType().activate().as("KILL").withClass(TravelListenerInstance.class);
		
		//Register DSCRIPT COMMANDS with Denizen
		new QuestCommand().activate().as("QUEST").withOptions("To Be Determined", 1);
		new QuestVarCommand().activate().as("QUESTVAR").withOptions("[NAME:Quest Script Name] [VARIABLE:Variable Name] [VALUE:Value]", 3);
		
		//Register DSCRIPT REQUIREMENTS with Denizen
		new QuestRequirement().activate().as("FLAGGED").withOptions("NO ARGS", 0);
		
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

}
