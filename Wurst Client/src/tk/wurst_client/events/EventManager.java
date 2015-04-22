/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.events;

import java.util.Collections;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.client.Minecraft;
import tk.wurst_client.error.gui.GuiError;
import tk.wurst_client.events.listeners.*;

public abstract class EventManager<E extends Event, L extends Listener>
{
	private final Set<L> listeners = Collections
		.synchronizedSet(new HashSet<L>());
	private static final Queue<Runnable> listenerQueue =
		new ConcurrentLinkedQueue<Runnable>();
	
	public static final EventManager<ChatInputEvent, ChatInputListener> chatInput =
		new EventManager<ChatInputEvent, ChatInputListener>()
		{
			@Override
			protected void listen(ChatInputListener listener,
				ChatInputEvent event) throws Exception
			{
				listener.onReceivedMessage(event);
			}
		};
	
	public static final EventManager<ChatOutputEvent, ChatOutputListener> chatOutput =
		new EventManager<ChatOutputEvent, ChatOutputListener>()
		{
			@Override
			protected void listen(ChatOutputListener listener,
				ChatOutputEvent event) throws Exception
			{
				listener.onSentMessage(event);
			}
		};
	
	public static final EventManager<DeathEvent, DeathListener> death =
		new EventManager<DeathEvent, DeathListener>()
		{
			@Override
			protected void listen(DeathListener listener, DeathEvent event)
				throws Exception
			{
				listener.onDeath();
			}
		};
	
	public static final EventManager<GUIRenderEvent, GUIRenderListener> guiRender =
		new EventManager<GUIRenderEvent, GUIRenderListener>()
		{
			@Override
			protected void listen(GUIRenderListener listener,
				GUIRenderEvent event) throws Exception
			{
				listener.onRenderGUI();
			}
		};
	
	public static final EventManager<LeftClickEvent, LeftClickListener> leftClick =
		new EventManager<LeftClickEvent, LeftClickListener>()
		{
			@Override
			protected void listen(LeftClickListener listener,
				LeftClickEvent event) throws Exception
			{
				listener.onLeftClick();
			}
		};
	
	public static final EventManager<PacketInputEvent, PacketInputListener> packetInput =
		new EventManager<PacketInputEvent, PacketInputListener>()
		{
			@Override
			protected void listen(PacketInputListener listener,
				PacketInputEvent event) throws Exception
			{
				listener.onReceivedPacket(event);
			}
		};
	
	public static final EventManager<RenderEvent, RenderListener> render =
		new EventManager<RenderEvent, RenderListener>()
		{
			@Override
			protected void listen(RenderListener listener, RenderEvent event)
				throws Exception
			{
				listener.onRender();
			}
		};
	
	public static final EventManager<UpdateEvent, UpdateListener> update =
		new EventManager<UpdateEvent, UpdateListener>()
		{
			@Override
			protected void listen(UpdateListener listener, UpdateEvent event)
				throws Exception
			{
				listener.onUpdate();
			}
		};
	
	public synchronized final void fireEvent(final E event)
	{
		try
		{
			for(L listener : listeners)
			{
				try
				{
					listen(listener, event);
				}catch(Exception e)
				{
					handleException(e, listener, event.getAction(),
						event.getComment());
				}
			}
			for(Runnable task; (task = listenerQueue.poll()) != null;)
				task.run();
		}catch(Exception e)
		{
			handleException(e, this, "processing events", "Event type: "
				+ event.getClass().getSimpleName());
		}
	}
	
	public static final void handleException(final Exception e,
		final Object cause, final String action, final String comment)
	{
		if(e.getMessage() != null
			&& e.getMessage().equals(
				"No OpenGL context found in the current thread."))
			return;
		update.addListener(new UpdateListener()
		{
			@Override
			public void onUpdate()
			{
				Minecraft.getMinecraft().displayGuiScreen(
					new GuiError(e, cause, action, comment));
				EventManager.update.removeListener(this);
			}
		});
	}
	
	public static final void init()
	{
		for(Runnable task; (task = listenerQueue.poll()) != null;)
			task.run();
	}
	
	protected abstract void listen(L listener, E event) throws Exception;
	
	public final void addListener(final L listener)
	{
		listenerQueue.add(new Runnable()
		{
			@Override
			public void run()
			{
				listeners.add(listener);
			}
		});
	}
	
	public final void removeListener(final L listener)
	{
		listenerQueue.add(new Runnable()
		{
			@Override
			public void run()
			{
				listeners.remove(listener);
			}
		});
	}
}
