package net.jeebiss.questmanager.commands;
import org.bukkit.command.CommandSender;

import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.command.CommandContext;
import net.citizensnpcs.command.exception.CommandException;


public class QMCommandHandler{
	
	private final Citizens plugin;

	public QMCommandHandler(Citizens plugin) {
		this.plugin = plugin;
	}
	
	@net.citizensnpcs.command.Command(
			aliases = { "qm" }, usage = "version", 
			desc = "Displays current Quest Manager version", flags = "", modifiers = { "version" },
			min = 1, max = 1, permission = "")
	public void assignment(CommandContext args, CommandSender sender, NPC npc) throws CommandException {
		
	}
}
