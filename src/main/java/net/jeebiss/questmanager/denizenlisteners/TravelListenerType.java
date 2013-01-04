package net.jeebiss.questmanager.denizenlisteners;

import net.aufdemrand.denizen.listeners.AbstractListenerType;

public class TravelListenerType extends AbstractListenerType{
	
	enum TravelType { DISTANCE, TOLOCATION, TONPC }
	
	@Override
	public void onEnable() {
		// nothing to do here
	}

}
