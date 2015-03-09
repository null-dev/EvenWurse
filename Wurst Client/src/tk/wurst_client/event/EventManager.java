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

import tk.wurst_client.event.events.WurstEvent;
import tk.wurst_client.event.listeners.WurstListener;

public class EventManager
{
	private static HashSet<WurstListener> listeners = new HashSet<WurstListener>();
	
	public synchronized static void addListener(WurstListener listener)
	{
		listeners.add(listener);
	}

	public synchronized static void removeListener(WurstListener listener)
	{
		listeners.remove(listener);
	}
	
	public synchronized static void fireEvent(WurstEvent event)
	{
		Iterator<WurstListener> itr = listeners.iterator();
		while(itr.hasNext())
		{
			WurstListener listener = itr.next();
			listener.onEvent(event);
		}
	}
}
