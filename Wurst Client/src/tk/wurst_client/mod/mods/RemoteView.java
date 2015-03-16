/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mod.mods;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.mod.Mod;
import tk.wurst_client.mod.Mod.Category;
import tk.wurst_client.mod.Mod.Info;
import tk.wurst_client.utils.EntityUtils;

@Info(category = Category.RENDER,
	description = "Allows you to see the world as someone else.\n"
		+ "Use the .rv command to make it target a specific entity.",
	name = "RemoteView")
public class RemoteView extends Mod implements UpdateListener
{
	private EntityPlayerSP newView = null;
	private double oldX;
	private double oldY;
	private double oldZ;
	private EntityLivingBase otherView = null;
	private static UUID otherID = null;
	private boolean wasInvisible;
	
	@Override
	public void onEnable()
	{
		if(EntityUtils.getClosestEntityRaw(false) == null)
		{
			Client.wurst.chat.message("There is no nearby entity.");
			setEnabled(false);
			return;
		}
		oldX = Minecraft.getMinecraft().thePlayer.posX;
		oldY = Minecraft.getMinecraft().thePlayer.posY;
		oldZ = Minecraft.getMinecraft().thePlayer.posZ;
		Minecraft.getMinecraft().thePlayer.noClip = true;
		if(otherID == null)
			otherID = EntityUtils.getClosestEntityRaw(false).getUniqueID();
		otherView = EntityUtils.searchEntityByIdRaw(otherID);
		wasInvisible =
			otherView.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);
		EntityOtherPlayerMP fakePlayer =
			new EntityOtherPlayerMP(Minecraft.getMinecraft().theWorld,
				Minecraft.getMinecraft().thePlayer.getGameProfile());
		fakePlayer.clonePlayer(Minecraft.getMinecraft().thePlayer, true);
		fakePlayer.posY -= 1.62;
		fakePlayer.rotationYawHead =
			Minecraft.getMinecraft().thePlayer.rotationYawHead;
		Minecraft.getMinecraft().theWorld.addEntityToWorld(-69, fakePlayer);
		Client.wurst.chat.message("Now viewing " + otherView.getName() + ".");
		EventManager.addUpdateListener(this);
	}
	
	public static void onEnabledByCommand(String viewName)
	{
		if(otherID == null && !viewName.equals(""))
			otherID = EntityUtils.searchEntityByNameRaw(viewName).getUniqueID();
		Client.wurst.modManager.getModByClass(RemoteView.class)
			.toggle();
	}
	
	@Override
	public void onUpdate()
	{
		if(EntityUtils.searchEntityByIdRaw(otherID) == null)
		{
			setEnabled(false);
			return;
		}
		newView = Minecraft.getMinecraft().thePlayer;
		otherView = EntityUtils.searchEntityByIdRaw(otherID);
		newView.copyLocationAndAnglesFrom(otherView);
		Minecraft.getMinecraft().thePlayer.motionX = 0;
		Minecraft.getMinecraft().thePlayer.motionY = 0;
		Minecraft.getMinecraft().thePlayer.motionZ = 0;
		Minecraft.getMinecraft().thePlayer = newView;
		otherView.setInvisible(true);
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeUpdateListener(this);
		if(otherView != null)
		{
			Client.wurst.chat.message("No longer viewing "
				+ otherView.getName() + ".");
			otherView.setInvisible(wasInvisible);
			Minecraft.getMinecraft().thePlayer.noClip = false;
			Minecraft.getMinecraft().thePlayer.setPositionAndRotation(oldX,
				oldY, oldZ, Minecraft.getMinecraft().thePlayer.rotationYaw,
				Minecraft.getMinecraft().thePlayer.rotationPitch);
			Minecraft.getMinecraft().theWorld.removeEntityFromWorld(-69);
		}
		newView = null;
		otherView = null;
		otherID = null;
	}
}
