package me.Jeebiss.QuestManager.DenizenListeners;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Jeebiss.QuestManager.DenizenListeners.TravelListenerType.TravelType;
import net.aufdemrand.denizen.listeners.AbstractListener;
import net.aufdemrand.denizen.utilities.arguments.aH;
import net.aufdemrand.denizen.utilities.arguments.aH.ArgumentType;
import net.aufdemrand.denizen.utilities.debugging.dB;

public class TravelListenerInstance extends AbstractListener implements Listener{
	
	private Location endPoint;
	
	private Integer blocksWalked = 0;
	private Integer distance = null;
	
	TravelType TYPE;
	
	@Override
	public void constructed() {
		denizen.getServer().getPluginManager().registerEvents(this, denizen);
	}

	@Override
	public void deconstructed() {
		PlayerMoveEvent.getHandlerList().unregister(this);
	}

	@Override
	public void onBuild(List<String> args) {
		
		for (String arg : args) {
			if (aH.matchesLocation(arg)){
				endPoint = aH.getLocationFrom(arg);
				dB.echoDebug("...ending location set");
			}
			
			else if (aH.matchesValueArg("DISTANCE, D", arg, ArgumentType.Integer)) {
				distance = aH.getIntegerFrom(arg);
				dB.echoDebug("...distance set to: " + distance);
			}
			
			else if (aH.matchesValueArg("TYPE", arg, ArgumentType.Custom)) {
				try {
					TYPE = TravelType.valueOf(aH.getStringFrom(arg));
					dB.echoDebug("...set TYPE to: " + aH.getStringFrom(arg));
				} catch (Exception e) {e.printStackTrace();}
			}
		}
	}

	@Override
	public void onCancel() {
		// nothing to do here
		
	}

	@Override
	public void onFinish() {
		// nothing to do here
		
	}

	@Override
	public void onLoad() {
		TYPE = TravelType.valueOf((String) get("Type"));
		distance = (Integer) get("Distance");
		blocksWalked = (Integer) get("Blocks Walked");
		endPoint = (Location) get("End Location");
	}

	@Override
	public void onSave() {
		store("Type", TYPE.name());
		store("Distance", distance);
		store("Blocks Walked", blocksWalked);
		store("End Location", endPoint);
	}

	@Override
	public String report() {
		// TODO Write a formatted output
		return "Report of TRAVEL LISTENER";
	}

	public void walking(PlayerMoveEvent event) {
		if (!(event.getPlayer() == player)) return;
		
		if (TYPE == TravelType.DISTANCE){
			if (!event.getTo().getBlock().equals(event.getFrom().getBlock())) {
				blocksWalked++;
				dB.echoDebug("..player moved a block");
				check();
			}
		} else if (TYPE == TravelType.TOLOCATION) {
			if (player.getLocation().distance(endPoint) <= 2) {
				dB.echoDebug("...player reached location");
				finish();
			}
		}
		
	}

	private void check() {
		if (blocksWalked >= distance) {
			finish();
		}
	}
}
