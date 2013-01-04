package net.jeebiss.questmanager;

import net.citizensnpcs.Citizens;
import net.jeebiss.questmanager.commands.QMCommandHandler;
import net.jeebiss.questmanager.denizencommands.QuestCommand;
import net.jeebiss.questmanager.denizencommands.QuestVarCommand;
import net.jeebiss.questmanager.denizenlisteners.TravelListenerInstance;
import net.jeebiss.questmanager.denizenlisteners.TravelListenerType;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class QuestManager extends JavaPlugin {
	
	QMCommandHandler commandHandler;
	Citizens citizens;
	
	@Override
	public void onEnable() {
		
		//Register Citizens 2 command handling
		citizens = (Citizens) Bukkit.getServer().getPluginManager().getPlugin("Citizens");
		commandHandler = new QMCommandHandler(citizens);
		
		//Register LISTENERs with Denizen
		new TravelListenerType().activate().as("KILL").withClass(TravelListenerInstance.class);
		
		//Register DSCRIPT COMMANDS with Denizen
		new QuestCommand().activate().as("QUEST").withOptions("To Be Determined", 1);
		new QuestVarCommand().activate().as("QUESTVAR").withOptions("[NAME:Quest Script Name] [VARIABLE:Variable Name] [VALUE:Value]", 3);


	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {
		Citizens citizens = (Citizens) getServer().getPluginManager().getPlugin("Citizens");
		return citizens.onCommand(sender, cmd, cmdName, args);
	}
	
	@Override
	public void onDisable() {
		
	}
}
