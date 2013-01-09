package net.jeebiss.questmanager.denizen.listeners;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.aufdemrand.denizen.listeners.AbstractListener;
import net.aufdemrand.denizen.utilities.arguments.aH;
import net.aufdemrand.denizen.utilities.arguments.aH.ArgumentType;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.jeebiss.questmanager.denizen.listeners.TravelListenerType.TravelType;

public class TravelListenerInstance extends AbstractListener implements Listener{
	
	NPC target;
	
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
			
			else if (aH.matchesValueArg("TARGET", arg, ArgumentType.LivingEntity)) {
				if ((CitizensAPI.getNPCRegistry().getNPC(aH.getLivingEntityFrom(arg)) != null &&
						CitizensAPI.getNPCRegistry().isNPC(aH.getLivingEntityFrom(arg)))){
					target = CitizensAPI.getNPCRegistry().getNPC(aH.getLivingEntityFrom(arg));
				}
				dB.echoDebug("...NPC set to: " + target.getId());
			}
		}
		
		if (TYPE == null) {
			dB.echoError("Missing TYPE argument! Valid: DISTANCE, TOLOCATION, TONPC");
			cancel();
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
		if (TYPE == TravelType.DISTANCE){
			return player.getName() + "has traveled " + blocksWalked + " blocks out of " + distance;
		} else if (TYPE == TravelType.TOLOCATION) {
			return player.getName() + " is traveling to " + endPoint;
		} else if (TYPE == TravelType.TONPC) {
			return player.getName() + " is traveling to NPC " + target.getId();
		}
		return "Failed to create detailed report";
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
		} else if (TYPE == TravelType.TONPC) {
			if (player.getLocation().distance(target.getBukkitEntity().getLocation()) <= 2) {
				dB.echoDebug("...player reached NPC");
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
