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
import tk.wurst_client.event.events.UpdateEvent;
import tk.wurst_client.event.listeners.UpdateListener;

public class EventManager
{
	private static HashSet<UpdateListener> updateListeners =
		new HashSet<UpdateListener>();
	
	public synchronized static void fireEvent(Event event)
	{
		if(event instanceof UpdateEvent)
		{
			Iterator<UpdateListener> itr = updateListeners.iterator();
			while(itr.hasNext())
			{
				UpdateListener listener = itr.next();
				listener.onUpdate();
			}
		}
	}

	public synchronized static void addUpdateListener(UpdateListener listener)
	{
		updateListeners.add(listener);
	}
	
	public synchronized static void removeUpdateListener(UpdateListener listener)
	{
		updateListeners.remove(listener);
	}
}
