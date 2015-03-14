/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.event.events;

import java.util.List;

import net.minecraft.client.gui.ChatLine;

public class ChatInputEvent extends ChatEvent
{
	private List<ChatLine> chatLines;
	
	public ChatInputEvent(String message, List<ChatLine> chatLines)
	{
		super(message);
	}
	
	public List<ChatLine> getChatLines()
	{
		return chatLines;
	}
}
