/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.events;

import java.util.EventListener;
import java.util.HashSet;

import javax.swing.event.EventListenerList;

import net.minecraft.client.Minecraft;
import tk.wurst_client.events.listeners.*;
import tk.wurst_client.gui.error.GuiError;

public final class EventManager
{
	private static final EventListenerList listenerList =
		new EventListenerList();
	private static final HashSet<ChatInputListener> chatInputListeners =
		new HashSet<>();
	private static final HashSet<ChatOutputListener> chatOutputListeners =
		new HashSet<>();
	private static final HashSet<DeathListener> deathListeners =
		new HashSet<>();
	private static final HashSet<GUIRenderListener> guiRenderListeners =
		new HashSet<>();
	private static final HashSet<LeftClickListener> leftClickListeners =
		new HashSet<>();
	private static final HashSet<PacketInputListener> packetInputListeners =
		new HashSet<>();
	private static final HashSet<RenderListener> renderListeners =
		new HashSet<>();
	private static final HashSet<UpdateListener> updateListeners =
		new HashSet<>();
	
	public void addChatInputListener(ChatInputListener listener)
	{
		chatInputListeners.add(listener);
	}
	
	public void addChatOutputListener(ChatOutputListener listener)
	{
		chatOutputListeners.add(listener);
	}
	
	public void addDeathListener(DeathListener listener)
	{
		deathListeners.add(listener);
	}
	
	public void addGuiRenderListener(GUIRenderListener listener)
	{
		guiRenderListeners.add(listener);
	}
	
	public void addLeftClickListener(LeftClickListener listener)
	{
		leftClickListeners.add(listener);
	}
	
	public void addPacketInputListener(PacketInputListener listener)
	{
		packetInputListeners.add(listener);
	}
	
	public void addRenderListener(RenderListener listener)
	{
		renderListeners.add(listener);
	}
	
	public void addUpdateListener(UpdateListener listener)
	{
		updateListeners.add(listener);
	}
	
	public void removeChatInputListener(ChatInputListener listener)
	{
		chatInputListeners.remove(listener);
	}
	
	public void removeChatOutputListener(ChatOutputListener listener)
	{
		chatOutputListeners.remove(listener);
	}
	
	public void removeDeathListener(DeathListener listener)
	{
		deathListeners.remove(listener);
	}
	
	public void removeGuiRenderListener(GUIRenderListener listener)
	{
		guiRenderListeners.remove(listener);
	}
	
	public void removeLeftClickListener(LeftClickListener listener)
	{
		leftClickListeners.remove(listener);
	}
	
	public void removePacketInputListener(PacketInputListener listener)
	{
		packetInputListeners.remove(listener);
	}
	
	public void removeRenderListener(RenderListener listener)
	{
		renderListeners.remove(listener);
	}
	
	public void removeUpdateListener(UpdateListener listener)
	{
		updateListeners.remove(listener);
	}
	
	public void fireChatInputEvent(ChatInputEvent event)
	{
		chatInputListeners.forEach((listener) -> listener
			.onReceivedMessage(event));
	}
	
	public void fireChatOutputEvent(ChatOutputEvent event)
	{
		chatOutputListeners
			.forEach((listener) -> listener.onSentMessage(event));
	}
	
	public void fireDeathEvent()
	{
		deathListeners.forEach((listener) -> listener.onDeath());
	}
	
	public void fireGuiRenderEvent()
	{
		guiRenderListeners.forEach((listener) -> listener.onRenderGUI());
	}
	
	public void fireLeftClickEvent()
	{
		leftClickListeners.forEach((listener) -> listener.onLeftClick());
	}
	
	public void firePacketInputEvent(PacketInputEvent event)
	{
		packetInputListeners.forEach((listener) -> listener
			.onReceivedPacket(event));
	}
	
	public void fireRenderEventEvent()
	{
		renderListeners.forEach((listener) -> listener.onRender());
	}
	
	public void fireUpdateEventEvent()
	{
		updateListeners.forEach((listener) -> listener.onUpdate());
	}
	
	// XXX: Outdated methods
	
	public void handleException(final Exception e, final Object cause,
		final String action, final String comment)
	{
		if(e.getMessage() != null
			&& e.getMessage().equals(
				"No OpenGL context found in the current thread."))
			return;
		add(UpdateListener.class, new UpdateListener()
		{
			@Override
			public void onUpdate()
			{
				Minecraft.getMinecraft().displayGuiScreen(
					new GuiError(e, cause, action, comment));
				remove(UpdateListener.class, this);
			}
		});
	}
	
	public <T extends EventListener> void add(Class<T> type, T listener)
	{
		listenerList.add(type, listener);
	}
	
	public <T extends EventListener> void remove(Class<T> type, T listener)
	{
		listenerList.remove(type, listener);
	}
	
	public synchronized <T extends Event> void fireEvent(Class<T> type, T event)
	{
		try
		{
			// TODO: A more efficient way to process the type
			if(type == GUIRenderEvent.class)
				fireGuiRenderEvent();
			else if(type == RenderEvent.class)
				fireRenderEventEvent();
			else if(type == PacketInputEvent.class)
				firePacketInputEvent((PacketInputEvent)event);
			else if(type == UpdateEvent.class)
				fireUpdateEventEvent();
			else if(type == ChatInputEvent.class)
				fireChatInputEvent((ChatInputEvent)event);
			else if(type == ChatOutputEvent.class)
				fireChatOutputEvent((ChatOutputEvent)event);
			else if(type == LeftClickEvent.class)
				fireLeftClickEvent();
			else if(type == DeathEvent.class)
				fireDeathEvent();
			else
				throw new IllegalArgumentException("Invalid event type: "
					+ type.getName());
		}catch(Exception e)
		{
			handleException(e, this, "processing events", "Event type: "
				+ event.getClass().getSimpleName());
		}
	}
}
