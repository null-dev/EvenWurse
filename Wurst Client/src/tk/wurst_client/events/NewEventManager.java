/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.events;

import java.util.EventListener;

import javax.swing.event.EventListenerList;

import net.minecraft.client.Minecraft;
import tk.wurst_client.error.gui.GuiError;
import tk.wurst_client.events.listeners.*;

public final class NewEventManager
{
	private static final EventListenerList listenerList =
		new EventListenerList();
	
	public synchronized <T extends Event> void fireEvent(Class<T> type, T event)
	{
		try
		{
			if(type == UpdateListener.class)
				fireUpdate();
			else
				throw new IllegalArgumentException("Invalid event type: " + type.getName());
		}catch(Exception e)
		{
			handleException(e, this, "processing events", "Event type: "
				+ event.getClass().getSimpleName());
		}
	}

	private void fireUpdate()
	{
		Object[] listeners = listenerList.getListenerList();
		for(int i = listeners.length - 2; i >= 0; i -= 2)
			if(listeners[i] == UpdateListener.class)
				((UpdateListener)listeners[i + 1]).onUpdate();
	}
	
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
}
