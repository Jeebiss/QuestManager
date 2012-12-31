package me.Jeebiss.QuestManager;
import me.Jeebiss.QuestManager.Commands.QMCommandHandler;
import me.Jeebiss.QuestManager.DenizenListeners.TravelListenerInstance;
import me.Jeebiss.QuestManager.DenizenListeners.TravelListenerType;
import net.citizensnpcs.Citizens;

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
