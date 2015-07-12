/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.FUN,
	description = "Thousands of colors!",
	name = "LSD",
	noCheatCompatible = true)
public class LsdMod extends Mod implements UpdateListener
{
	@Override
	public void onToggle()
	{
		if(!OpenGlHelper.shadersSupported)
			Minecraft.getMinecraft().renderGlobal.loadRenderers();
	}
	
	@Override
	public void onEnable()
	{
		if(OpenGlHelper.shadersSupported)
			if(Minecraft.getMinecraft().func_175606_aa() instanceof EntityPlayer)
			{
				if(Minecraft.getMinecraft().entityRenderer.theShaderGroup != null)
					Minecraft.getMinecraft().entityRenderer.theShaderGroup
						.deleteShaderGroup();
				
				Minecraft.getMinecraft().entityRenderer.shaderIndex = 19;
				
				if(Minecraft.getMinecraft().entityRenderer.shaderIndex != EntityRenderer.shaderCount)
					Minecraft.getMinecraft().entityRenderer
						.func_175069_a(EntityRenderer.shaderResourceLocations[19]);
				else
					Minecraft.getMinecraft().entityRenderer.theShaderGroup =
						null;
			}
		WurstClient.INSTANCE.eventManager.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(!OpenGlHelper.shadersSupported)
			Minecraft.getMinecraft().thePlayer
				.addPotionEffect(new PotionEffect(Potion.confusion.getId(),
					10801220));
		Minecraft.getMinecraft().gameSettings.smoothCamera = isEnabled();
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.eventManager.remove(UpdateListener.class, this);
		Minecraft.getMinecraft().thePlayer.removePotionEffect(Potion.confusion
			.getId());
		if(Minecraft.getMinecraft().entityRenderer.theShaderGroup != null)
		{
			Minecraft.getMinecraft().entityRenderer.theShaderGroup
				.deleteShaderGroup();
			Minecraft.getMinecraft().entityRenderer.theShaderGroup = null;
		}
		Minecraft.getMinecraft().gameSettings.smoothCamera = false;
	}
}
