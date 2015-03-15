/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class Sneak extends Module implements UpdateListener
{
	public Sneak()
	{
		super(
			"Sneak",
			"Automatically sneaks all the time.",
			Category.MOVEMENT);
	}
	
	@Override
	public void onEnable()
	{
		EventManager.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Client.wurst.modManager.getMod(YesCheat.class)
			.getToggled())
			Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed = true;
		else
			Minecraft.getMinecraft().thePlayer.sendQueue
				.addToSendQueue(new C0BPacketEntityAction(Minecraft
					.getMinecraft().thePlayer, Action.START_SNEAKING));
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeUpdateListener(this);
		Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed = false;
		Minecraft.getMinecraft().thePlayer.sendQueue
			.addToSendQueue(new C0BPacketEntityAction(
				Minecraft.getMinecraft().thePlayer, Action.STOP_SNEAKING));
	}
}
