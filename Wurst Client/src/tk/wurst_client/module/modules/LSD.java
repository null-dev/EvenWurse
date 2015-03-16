/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import java.awt.Color;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.module.Mod;
import tk.wurst_client.module.Mod.Category;
import tk.wurst_client.module.Mod.Info;

@Info(category = Category.FUN, description = "Thousands of colors!", name = "LSD")
public class LSD extends Mod implements UpdateListener
{
	private static float speed = 2;
	private static long currentMS = 0L;
	private static long lastMS = -1L;
	private static Color color = Color.WHITE;
	
	@Override
	public void onToggle()
	{
		if(!OpenGlHelper.shadersSupported)
			Minecraft.getMinecraft().renderGlobal.loadRenderers();
	}
	
	@Override
	public void onEnable()
	{
		Minecraft.getMinecraft().entityRenderer.activateLSD();
		EventManager.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(!OpenGlHelper.shadersSupported)
			Minecraft.getMinecraft().thePlayer
				.addPotionEffect(new PotionEffect(Potion.confusion.getId(),
					10801220));
		Minecraft.getMinecraft().gameSettings.smoothCamera = true;
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeUpdateListener(this);
		Minecraft.getMinecraft().thePlayer.removePotionEffect(Potion.confusion
			.getId());
		Minecraft.getMinecraft().gameSettings.smoothCamera = false;
		if(Minecraft.getMinecraft().entityRenderer.theShaderGroup != null)
		{
			Minecraft.getMinecraft().entityRenderer.theShaderGroup
				.deleteShaderGroup();
			Minecraft.getMinecraft().entityRenderer.theShaderGroup = null;
		}
		Tessellator.shouldRenderLSD = false;
	}
	
	public static Color randomColor()
	{
		currentMS = System.currentTimeMillis();
		if(currentMS >= lastMS + (long)(1000 / speed))
		{
			color = Color.WHITE;
			lastMS = System.currentTimeMillis();
		}
		while(color == Color.WHITE)
			color = new Color
				(
					new Random().nextInt(256),
					new Random().nextInt(256),
					new Random().nextInt(256),
					new Random().nextInt(256)
				);
		return color;
	}
}
