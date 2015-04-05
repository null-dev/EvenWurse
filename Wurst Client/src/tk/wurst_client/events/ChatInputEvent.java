/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.events;

import java.util.List;

import net.minecraft.client.gui.ChatLine;
import net.minecraft.util.IChatComponent;

public class ChatInputEvent extends CancellableEvent
{
	private IChatComponent component;
	private List<ChatLine> chatLines;
	
	public ChatInputEvent(IChatComponent component, List<ChatLine> chatLines)
	{
		this.component = component;
		this.chatLines = chatLines;
	}
	
	public IChatComponent getComponent()
	{
		return component;
	}
	
	public void setComponent(IChatComponent component)
	{
		this.component = component;
	}
	
	public List<ChatLine> getChatLines()
	{
		return chatLines;
	}
	
	@Override
	public String getAction()
	{
		return "receiving chat message";
	}
	
	@Override
	public String getComment()
	{
		return "Message: `" + component.getUnformattedText() + "`";
	}
}
