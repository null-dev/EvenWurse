/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui.options.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import tk.wurst_client.WurstClient;
import tk.wurst_client.api.Module;
import tk.wurst_client.api.ModuleConfiguration;
import tk.wurst_client.gui.GuiWurstSlot;

import java.util.ArrayList;
import java.util.Map;

public class GuiConfigEntryList extends GuiWurstSlot
{
	public GuiConfigEntryList(Minecraft par1Minecraft, Module m, GuiScreen prevMenu)
	{
		super(par1Minecraft, prevMenu.width, prevMenu.height, 36,
			prevMenu.height - 56, 30);
		mc = par1Minecraft;
		this.module = m;
		WurstClient.INSTANCE.files.saveModuleConfigs();
		elements.addAll(ModuleConfiguration.forModule(m).getConfig().entrySet());
	}

	private Module module;
	private int selectedSlot;
	private Minecraft mc;
	ArrayList<Map.Entry<String, String>> elements = new ArrayList<>();

	@Override
	protected boolean isSelected(int id)
	{
		return selectedSlot == id;
	}
	
	protected int getSelectedSlot()
	{
		return selectedSlot;
	}
	
	@Override
	protected int getSize()
	{
		return elements.size();
	}
	
	@Override
	protected void elementClicked(int var1, boolean var2, int var3, int var4)
	{
		selectedSlot = var1;
	}
	
	@Override
	protected void drawBackground()
	{}
	
	@Override
	protected void drawSlot(int id, int x, int y, int var4, int var5, int var6)
	{
		Map.Entry<String, String> entry = elements.get(id);
		mc.fontRendererObj.drawString("Key: " + entry.getKey(), x, y + 3, 10526880);
		mc.fontRendererObj.drawString("Value: " + entry.getValue(), x, y + 15,
			10526880);
	}
}
