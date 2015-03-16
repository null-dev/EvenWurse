/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import net.minecraft.network.Packet;

import org.darkstorm.minecraft.gui.component.basic.BasicSlider;

import tk.wurst_client.Client;

public class Mod
{
	private final String name = getClass().getAnnotation(Info.class).name();
	private final String description = getClass().getAnnotation(Info.class).description();
	private final Category category = getClass().getAnnotation(Info.class).category();
	private boolean toggled;
	protected ArrayList<BasicSlider> sliders = new ArrayList<BasicSlider>();
	private long currentMS = 0L;
	protected long lastMS = -1L;

	public enum Category
	{
		AUTOBUILD,
		BLOCKS,
		CHAT,
		COMBAT,
		FUN,
		HIDDEN,
		RENDER,
		MISC,
		MOVEMENT,
		SETTINGS,
		WIP;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Info
	{	
		String name();
		String description();
		Category category();
	}
	
	public final String getName()
	{
		return name;
	}
	
	public String getRenderName()
	{
		return name;
	}
	
	public final String getDescription()
	{
		return description;
	}
	
	public final Category getCategory()
	{
		return category;
	}
	
	public final boolean getToggled()
	{
		return toggled;
	}
	
	public final void setToggled(boolean shouldToggle)
	{
		onToggle();
		if(shouldToggle)
		{
			onEnable();
			toggled = true;
		}else
		{
			onDisable();
			toggled = false;
		}
		Client.wurst.fileManager.saveMods();
	}
	
	public final void toggleModule()
	{
		setToggled(!getToggled());
	}
	
	public final ArrayList<BasicSlider> getSliders()
	{
		return sliders;
	}
	
	public final void setSliders(ArrayList<BasicSlider> newSliders)
	{
		sliders = newSliders;
	}
	
	public final void noCheatMessage()
	{
		Client.wurst.chat.warning(name + " cannot bypass NoCheat+.");
	}
	
	public final void updateMS()
	{
		currentMS = System.currentTimeMillis();
	}
	
	public final void updateLastMS()
	{
		lastMS = System.currentTimeMillis();
	}
	
	public final boolean hasTimePassedM(long MS)
	{
		return currentMS >= lastMS + MS;
	}
	
	public final boolean hasTimePassedS(float speed)
	{
		return currentMS >= lastMS + (long)(1000 / speed);
	}
	
	public void onToggle()
	{}
	
	public void onEnable()
	{}
	
	public void onDisable()
	{}
	
	public void onDeath()
	{}
	
	/**
	 * Note: This runs before the swing animation.
	 */
	public void onLeftClick()
	{}
	
	public void initSliders()
	{}
	
	public void updateSettings()
	{}
	
	public void onPacket(Packet packet)
	{}
}
