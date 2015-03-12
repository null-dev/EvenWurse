/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.event;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import tk.wurst_client.event.events.Event;
import tk.wurst_client.event.events.UpdateEvent;
import tk.wurst_client.event.listeners.UpdateListener;

public class EventManager
{
	private static Set<UpdateListener> updateListeners = Collections
		.synchronizedSet(new HashSet<UpdateListener>());
	private static Queue<Runnable> queue =
		new ConcurrentLinkedQueue<Runnable>();
	
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
			for(Runnable task; (task = queue.poll()) != null;)
				task.run();
		}
	}
	
	public synchronized static void addUpdateListener(
		final UpdateListener listener)
	{
		queue.add(new Runnable()
		{
			@Override
			public void run()
			{
				updateListeners.add(listener);
			}
		});
	}
	
	public synchronized static void removeUpdateListener(
		final UpdateListener listener)
	{
		queue.add(new Runnable()
		{
			@Override
			public void run()
			{
				updateListeners.remove(listener);
			}
		});
	}
}
