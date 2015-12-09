/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.EntityUtils;

@Info(category = Category.RENDER,
	description = "Allows you to see the world as someone else.\n"
		+ "Use the .rv command to make it target a specific entity.",
	name = "RemoteView")
public class RemoteViewMod extends Mod implements UpdateListener
{
	private EntityPlayerSP newView = null;
	private double oldX;
	private double oldY;
	private double oldZ;
	private float oldYaw;
	private float oldPitch;
	private EntityLivingBase otherView = null;
	private static UUID otherID = null;
	private boolean wasInvisible;
	
	@Override
	public void onEnable()
	{
		if(EntityUtils.getClosestEntityRaw(false) == null)
		{
			WurstClient.INSTANCE.chat.message("There is no nearby entity.");
			setEnabled(false);
			return;
		}
		oldX = Minecraft.getMinecraft().thePlayer.posX;
		oldY = Minecraft.getMinecraft().thePlayer.posY;
		oldZ = Minecraft.getMinecraft().thePlayer.posZ;
		oldYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		oldPitch = Minecraft.getMinecraft().thePlayer.rotationPitch;
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
		fakePlayer
			.copyLocationAndAnglesFrom(Minecraft.getMinecraft().thePlayer);
		fakePlayer.rotationYawHead =
			Minecraft.getMinecraft().thePlayer.rotationYawHead;
		Minecraft.getMinecraft().theWorld.addEntityToWorld(-69, fakePlayer);
		WurstClient.INSTANCE.chat.message("Now viewing " + otherView.getName()
			+ ".");
		WurstClient.INSTANCE.events.addUpdateListener(this);
	}
	
	public static void onEnabledByCommand(String viewName)
	{
		try
		{
			if(otherID == null && !viewName.equals(""))
				otherID =
					EntityUtils.searchEntityByNameRaw(viewName).getUniqueID();
			WurstClient.INSTANCE.mods.remoteViewMod.toggle();
		}catch(NullPointerException e)
		{
			WurstClient.INSTANCE.chat.error("Entity not found.");
		}
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
		WurstClient.INSTANCE.events.removeUpdateListener(this);
		if(otherView != null)
		{
			WurstClient.INSTANCE.chat.message("No longer viewing "
				+ otherView.getName() + ".");
			otherView.setInvisible(wasInvisible);
			Minecraft.getMinecraft().thePlayer.noClip = false;
			Minecraft.getMinecraft().thePlayer.setPositionAndRotation(oldX,
				oldY, oldZ, oldYaw, oldPitch);
			Minecraft.getMinecraft().theWorld.removeEntityFromWorld(-69);
		}
		newView = null;
		otherView = null;
		otherID = null;
	}
}
