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
	private String name;
	private String description;
	private Category category;
	private boolean toggled;
	protected ArrayList<BasicSlider> sliders = new ArrayList<BasicSlider>();
	private long currentMS = 0L;
	protected long lastMS = -1L;
	
	public Mod(String moduleName, String moduleDescription,
		Category moduleCategory)
	{
		this.name = moduleName;
		this.description = moduleDescription;
		this.category = moduleCategory;
		initSliders();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Info
	{	
		String name();
		String description();
		Category category();
	}
	
	public String getName()
	{
		return getClass().getAnnotation(Info.class).name();
	}
	
	public String getRenderName()
	{
		return getName();
	}
	
	public String getDescription()
	{
		return getClass().getAnnotation(Info.class).description();
	}
	
	public Category getCategory()
	{
		return getClass().getAnnotation(Info.class).category();
	}
	
	public boolean getToggled()
	{
		return toggled;
	}
	
	public void setToggled(boolean shouldToggle)
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
	
	public void toggleModule()
	{
		setToggled(!getToggled());
	}
	
	public ArrayList<BasicSlider> getSliders()
	{
		return sliders;
	}
	
	public void setSliders(ArrayList<BasicSlider> newSliders)
	{
		sliders = newSliders;
	}
	
	public void noCheatMessage()
	{
		Client.wurst.chat.warning(name + " cannot bypass NoCheat+.");
	}
	
	public void updateMS()
	{
		currentMS = System.currentTimeMillis();
	}
	
	public void updateLastMS()
	{
		lastMS = System.currentTimeMillis();
	}
	
	public boolean hasTimePassedM(long MS)
	{
		return currentMS >= lastMS + MS;
	}
	
	public boolean hasTimePassedS(float speed)
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
