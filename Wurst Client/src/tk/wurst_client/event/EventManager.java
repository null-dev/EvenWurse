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

import tk.wurst_client.event.events.ChatInputEvent;
import tk.wurst_client.event.events.ChatOutputEvent;
import tk.wurst_client.event.events.Event;
import tk.wurst_client.event.events.GUIRenderEvent;
import tk.wurst_client.event.events.RenderEvent;
import tk.wurst_client.event.events.UpdateEvent;
import tk.wurst_client.event.listeners.ChatInputListener;
import tk.wurst_client.event.listeners.ChatOutputListener;
import tk.wurst_client.event.listeners.GUIRenderListener;
import tk.wurst_client.event.listeners.RenderListener;
import tk.wurst_client.event.listeners.UpdateListener;

public class EventManager
{
	private static Set<ChatInputListener> chatInputListeners = Collections
		.synchronizedSet(new HashSet<ChatInputListener>());
	private static Set<ChatOutputListener> chatOutputListeners = Collections
		.synchronizedSet(new HashSet<ChatOutputListener>());
	private static Set<GUIRenderListener> guiRenderListeners = Collections
		.synchronizedSet(new HashSet<GUIRenderListener>());
	private static Set<RenderListener> renderListeners = Collections
		.synchronizedSet(new HashSet<RenderListener>());
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
		}else if(event instanceof RenderEvent)
		{
			Iterator<RenderListener> itr = renderListeners.iterator();
			while(itr.hasNext())
			{
				RenderListener listener = itr.next();
				listener.onRender();
			}
		}else if(event instanceof GUIRenderEvent)
		{
			Iterator<GUIRenderListener> itr = guiRenderListeners.iterator();
			while(itr.hasNext())
			{
				GUIRenderListener listener = itr.next();
				listener.onRenderGUI();
			}
		}else if(event instanceof ChatInputEvent)
		{
			Iterator<ChatInputListener> itr = chatInputListeners.iterator();
			while(itr.hasNext())
			{
				ChatInputListener listener = itr.next();
				listener.onReceivedMessage((ChatInputEvent)event);
			}
		}else if(event instanceof ChatOutputEvent)
		{
			Iterator<ChatOutputListener> itr = chatOutputListeners.iterator();
			while(itr.hasNext())
			{
				ChatOutputListener listener = itr.next();
				listener.onSentMessage((ChatOutputEvent)event);
			}
		}
		for(Runnable task; (task = queue.poll()) != null;)
			task.run();
	}
	
	public synchronized static void addChatInputListener(
		final ChatInputListener listener)
	{
		queue.add(new Runnable()
		{
			@Override
			public void run()
			{
				chatInputListeners.add(listener);
			}
		});
	}
	
	public synchronized static void removeChatInputListener(
		final ChatInputListener listener)
	{
		queue.add(new Runnable()
		{
			@Override
			public void run()
			{
				chatInputListeners.remove(listener);
			}
		});
	}
	
	public synchronized static void addChatOutputListener(
		final ChatOutputListener listener)
	{
		queue.add(new Runnable()
		{
			@Override
			public void run()
			{
				chatOutputListeners.add(listener);
			}
		});
	}
	
	public synchronized static void removeChatOutputListener(
		final ChatOutputListener listener)
	{
		queue.add(new Runnable()
		{
			@Override
			public void run()
			{
				chatOutputListeners.remove(listener);
			}
		});
	}
	
	public synchronized static void addGUIRenderListener(
		final GUIRenderListener listener)
	{
		queue.add(new Runnable()
		{
			@Override
			public void run()
			{
				guiRenderListeners.add(listener);
			}
		});
	}
	
	public synchronized static void removeGUIRenderListener(
		final GUIRenderListener listener)
	{
		queue.add(new Runnable()
		{
			@Override
			public void run()
			{
				guiRenderListeners.remove(listener);
			}
		});
	}
	
	public synchronized static void addRenderListener(
		final RenderListener listener)
	{
		queue.add(new Runnable()
		{
			@Override
			public void run()
			{
				renderListeners.add(listener);
			}
		});
	}
	
	public synchronized static void removeRenderListener(
		final RenderListener listener)
	{
		queue.add(new Runnable()
		{
			@Override
			public void run()
			{
				renderListeners.remove(listener);
			}
		});
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
