/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module;

import java.util.ArrayList;

import net.minecraft.network.Packet;

import org.darkstorm.minecraft.gui.component.basic.BasicSlider;

import tk.wurst_client.Client;

public class Module
{
	private String moduleName;
	private String moduleDescription;
	private Category moduleCategory;
	private boolean isToggled;
	protected ArrayList<BasicSlider> moduleSliders =
		new ArrayList<BasicSlider>();
	private long currentMS = 0L;
	protected long lastMS = -1L;
	
	public Module(String moduleName, String moduleDescription,
		Category moduleCategory)
	{
		this.moduleName = moduleName;
		this.moduleDescription = moduleDescription;
		this.moduleCategory = moduleCategory;
		initSliders();
	}
	
	public String getName()
	{
		return moduleName;
	}
	
	public String getRenderName()
	{
		return moduleName;
	}
	
	public String getDescription()
	{
		return moduleDescription;
	}
	
	public Category getCategory()
	{
		return moduleCategory;
	}
	
	public boolean getToggled()
	{
		return isToggled;
	}
	
	public void setToggled(boolean shouldToggle)
	{
		onToggle();
		if(shouldToggle)
		{
			onEnable();
			isToggled = true;
		}else
		{
			onDisable();
			isToggled = false;
		}
		Client.wurst.fileManager.saveModules();
	}
	
	public void toggleModule()
	{
		setToggled(!getToggled());
	}
	
	public ArrayList<BasicSlider> getSliders()
	{
		return moduleSliders;
	}
	
	public void setSliders(ArrayList<BasicSlider> newSliders)
	{
		moduleSliders = newSliders;
	}
	
	public void noCheatMessage()
	{
		Client.wurst.chat.warning(moduleName + " cannot bypass NoCheat+.");
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
	
	public void onRenderGUI()
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
	
	public void onReceivedMessage(String message)
	{}
	
	public void onPacket(Packet packet)
	{}
}
