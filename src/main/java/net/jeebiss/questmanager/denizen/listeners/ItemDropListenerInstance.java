package net.jeebiss.questmanager.denizen.listeners;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.aufdemrand.denizen.listeners.AbstractListener;
import net.aufdemrand.denizen.utilities.Depends;
import net.aufdemrand.denizen.utilities.arguments.aH;
import net.aufdemrand.denizen.utilities.arguments.aH.ArgumentType;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.jeebiss.questmanager.denizen.listeners.ItemDropListenerType.ItemDropType;

public class ItemDropListenerInstance extends AbstractListener implements Listener {
	
	ItemDropType type = null;
	
	ItemStack item = null;
	Location location = null;
	Material block = null;
	EntityType mob = null;
	
	String dropper = null;
	String wgregion = null;
	
	Integer radius = 5;
	Integer dropRate = 100;
	Integer quantity = 1;
	Integer qtyDropped = 0;
	
	Random r = new Random();
	
	@Override
	public void constructed() {
		denizen.getServer().getPluginManager().registerEvents(this, denizen);
		
	}

	@Override
	public void deconstructed() {
		EntityDeathEvent.getHandlerList().unregister(this);
		
	}

	@Override
	public void onBuild(List<String> args) {
		for (String arg : args) {
			if (aH.matchesValueArg("TYPE", arg, ArgumentType.Custom)) {
				try {
					type = ItemDropType.valueOf(aH.getStringFrom(arg));
					dB.echoDebug("...type set to: " + type.name());
					continue;
				} catch (Exception e) { dB.echoDebug("...type " + type.name() + " is not valid."); }
				
			} else if (aH.matchesItem(arg)) {
				item = aH.getItemFrom(arg);
				dB.echoDebug("...item set to: " + item);
				continue;
				
			} else if (aH.matchesValueArg("REGION", arg, ArgumentType.Custom)) {
				wgregion = aH.getStringFrom(arg);
				dB.echoDebug("...region set to: " + wgregion);
				continue;
				
			} else if (aH.matchesLocation(arg)) {
				location = aH.getLocationFrom(arg);
				dB.echoDebug("...location set to: " + location);
				continue;
				
			} else if (aH.matchesValueArg("RADIUS", arg, ArgumentType.Integer)) {
				radius = aH.getIntegerFrom(arg);
				dB.echoDebug("...radius set to: " + radius);
				continue;
				
			} else if (aH.matchesValueArg("DROPRATE", arg, ArgumentType.Integer)) {
				dropRate = aH.getIntegerFrom(arg);
				dB.echoDebug("...drop rate set to: " + dropRate + "/100");
				continue;
				
			} else if (aH.matchesValueArg("DROPSFROM", arg, ArgumentType.Custom)) {
				dropper = aH.getStringFrom(arg);
				dB.echoDebug("...dropper set to: " + dropper);
				continue;
				
			} else if (aH.matchesQuantity(arg)) {
				quantity = aH.getIntegerFrom(arg);
				dB.echoDebug("...quantity set to: " + quantity);
				continue;
			}
		}
			
		if (item == null) {
			dB.echoDebug("...item could not be set");
			cancel();
		}
			
		switch (type) {
		
		case BLOCKPLACE:
			
		case BLOCKBREAK:
			try  { 
				block = Material.valueOf(dropper);
				dB.echoDebug("...DROPSFROM material set");
			} catch (Exception e) { dB.echoDebug("...DROPSFROM is not a valid material"); }
			break;
		
		case MOBKILL:
			if (aH.matchesEntityType("entity:" + dropper)) {
				mob = EntityType.valueOf(dropper.toUpperCase());
				dB.echoDebug("...mob selected from DROPSFROM");
			} else dB.echoDebug("...could not select mob from DROPSFROM");
			break;
			
		default:
			dB.echoDebug("...error setting type");
			cancel();
			break;
		
		}
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String report() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@EventHandler
	public void mobKilled(EntityDeathEvent event) {
		if (type != ItemDropType.MOBKILL) return;
		dB.echoDebug("...checking kill");
		if (event.getEntity().getKiller() != player) return;
		dB.echoDebug("...killed by player");
		if (event.getEntity().getType() != mob) return;
		dB.echoDebug("...proper mob");
		if (location != null) {
			if (location.distance(player.getLocation()) > radius ) return;
		}
		dB.echoDebug("...within range");
		if (wgregion != null) {
			if (!inRegion(player, wgregion)) return;
		}
		dB.echoDebug("...within region");
		
		dB.echoDebug("...trying to drop item");
		if (r.nextInt(101) < dropRate) {
			dB.echoDebug("...item should drop now");
			event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), item);
			qtyDropped++;
			dB.echoDebug("...item dropped");
			check();
		}
	}
	
	@EventHandler
	public void blockMined(BlockBreakEvent event) {
		if (type != ItemDropType.BLOCKBREAK) return;
		dB.echoDebug("...checking blockbreakevent");
		if (event.getPlayer() != player) return;
		dB.echoDebug("...mined by player");
		if (event.getBlock().getType() != block) return;
		dB.echoDebug("...proper block mined");
		if (location != null) {
			if (location.distance(player.getLocation()) > radius ) return;
		}
		dB.echoDebug("...within range");
		if (wgregion != null) {
			if (!inRegion(player, wgregion)) return;
		}
		dB.echoDebug("...within region");
		
		if (r.nextInt(101) < dropRate) {
			event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), item);
			qtyDropped++;
			dB.echoDebug("...item dropped");
			check();
		}
	}
	
	@EventHandler
	public void blockPlaced(BlockPlaceEvent event) {
		if (type != ItemDropType.BLOCKPLACE) return;
		dB.echoDebug("...checking blockplaceevent");
		if (event.getPlayer() != player) return;
		dB.echoDebug("...placed by player");
		if (event.getBlock().getType() != block) return;
		dB.echoDebug("...proper block placed");
		if (location != null) {
			if (location.distance(player.getLocation()) > radius ) return;
		}
		dB.echoDebug("...within range");
		if (wgregion != null) {
			if (!inRegion(player, wgregion)) return;
		}
		dB.echoDebug("...within region");
		
		if (r.nextInt(101) < dropRate) {
			event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), item);
			qtyDropped++;
			dB.echoDebug("...item dropped");
			check();
		}
	}
	
	/*
	 * Checks whether or not a player is in
	 * a given WorldGuard region.
	 */
	public boolean inRegion(Player thePlayer, String region) {
		if (Depends.worldGuard == null) return false;
		boolean inRegion = false;
		ApplicableRegionSet currentRegions = Depends.worldGuard.getRegionManager(thePlayer.getWorld()).getApplicableRegions(thePlayer.getLocation());
		for(ProtectedRegion thisRegion: currentRegions){
			dB.echoDebug("...checking current player region: " + thisRegion.getId());
			if (thisRegion.getId().contains(region)) {
				inRegion = true;
				dB.echoDebug("...matched region");
			} 
		}
			return inRegion;
	}
	
	private void check() {
		dB.echoDebug(qtyDropped + "/" + quantity + " dropped");
		if (quantity == qtyDropped) {
			finish();
		}
	}

}
