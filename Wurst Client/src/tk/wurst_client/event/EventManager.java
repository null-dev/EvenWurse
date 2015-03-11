/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.event;

import java.util.HashSet;
import java.util.Iterator;

import tk.wurst_client.event.events.Event;
import tk.wurst_client.event.listeners.Listener;

public class EventManager
{
	private static HashSet<Listener> listeners = new HashSet<Listener>();
	
	public synchronized static void addListener(Listener listener)
	{
		listeners.add(listener);
	}

	public synchronized static void removeListener(Listener listener)
	{
		listeners.remove(listener);
	}
	
	public synchronized static void fireEvent(Event event)
	{
		Iterator<Listener> itr = listeners.iterator();
		while(itr.hasNext())
		{
			Listener listener = itr.next();
			//listener.onEvent(event);
		}
	}
}
