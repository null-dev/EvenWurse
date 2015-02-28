/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui.options;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;

import tk.wurst_client.Client;
import tk.wurst_client.gui.GuiWurstSlot;
import tk.wurst_client.module.Module;

public class GuiKeybindList extends GuiWurstSlot
{
	public GuiKeybindList(Minecraft par1Minecraft, GuiScreen prevMenu)
	{
		super(par1Minecraft, prevMenu.width, prevMenu.height, 36, prevMenu.height - 56, 30);
		mc = par1Minecraft;
	}
	
	private int selectedSlot;
	private Minecraft mc;
	@Deprecated
	public static ArrayList<Module> modules = new ArrayList<Module>();
	
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
		return modules.size();
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
		Module module = modules.get(id);
		String bind = Keyboard.getKeyName(module.getBind());
		mc.fontRendererObj.drawString("Key: " + bind, x + 3, y + 3, 10526880);
		String category = module.getCategory().name();
		if(!category.equals("WIP"))
			category = category.charAt(0) + category.substring(1).toLowerCase();
		mc.fontRendererObj.drawString("Command: " + module.getName(), x + 3, y + 15, 10526880);
	}
}
