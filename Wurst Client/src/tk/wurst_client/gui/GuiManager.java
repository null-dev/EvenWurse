/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * Modifications made by Alexander01998 in this Source Code Form are subject
 * to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL
 * was not distributed with this file, You can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
/*
 * Copyright (c) 2013, DarkStorm (darkstorm@evilminecraft.net)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 * this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package tk.wurst_client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.client.Minecraft;

import org.darkstorm.minecraft.gui.AbstractGuiManager;
import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.ComboBox;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.component.Slider;
import org.darkstorm.minecraft.gui.component.basic.BasicButton;
import org.darkstorm.minecraft.gui.component.basic.BasicComboBox;
import org.darkstorm.minecraft.gui.component.basic.BasicFrame;
import org.darkstorm.minecraft.gui.component.basic.BasicLabel;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import org.darkstorm.minecraft.gui.layout.GridLayoutManager;
import org.darkstorm.minecraft.gui.layout.GridLayoutManager.HorizontalGridConstraint;
import org.darkstorm.minecraft.gui.listener.ButtonListener;
import org.darkstorm.minecraft.gui.listener.ComboBoxListener;
import org.darkstorm.minecraft.gui.listener.SliderListener;
import org.darkstorm.minecraft.gui.theme.Theme;

import tk.wurst_client.WurstClient;
import tk.wurst_client.mods.AutoBuildMod;
import tk.wurst_client.mods.Mod;
import tk.wurst_client.mods.Mod.Category;

/**
 * Minecraft GUI API
 *
 * This class is not actually intended for use; rather, you should use this as a
 * template for your actual GuiManager, as the creation of frames is highly
 * implementation-specific.
 *
 * @author DarkStorm (darkstorm@evilminecraft.net)
 * @author Alexander01998
 */
public final class GuiManager extends AbstractGuiManager
{
	private class ModuleFrame extends BasicFrame
	{
		private ModuleFrame()
		{}
		
		private ModuleFrame(String title)
		{
			super(title);
		}
	}
	
	private final AtomicBoolean setup;
	
	public GuiManager()
	{
		setup = new AtomicBoolean();
	}
	
	@Override
	public void setup()
	{
		if(!setup.compareAndSet(false, true))
			return;
		
		final Map<Category, ModuleFrame> categoryFrames =
			new HashMap<Category, ModuleFrame>();
		ModuleFrame settings = new ModuleFrame("Settings");
		settings.setTheme(theme);
		settings.setLayoutManager(new GridLayoutManager(1, 0));
		settings.setVisible(true);
		settings.setClosable(false);
		settings.setMinimized(true);
		settings.setPinnable(true);
		addFrame(settings);
		categoryFrames.put(Category.SETTINGS, settings);
		for(final Mod mod : WurstClient.INSTANCE.modManager.getAllMods())
		{
			ModuleFrame frame = categoryFrames.get(mod.getCategory());
			if(frame == null)
			{
				String name = mod.getCategory().name().toLowerCase();
				if(WurstClient.INSTANCE.fileManager.options.exists())
					WurstClient.INSTANCE.fileManager.loadOptions();
				if(name.equalsIgnoreCase("HIDDEN"))
					continue;
				name =
					Character.toUpperCase(name.charAt(0)) + name.substring(1);
				if(name.equalsIgnoreCase("WIP"))
					name = "WIP";// Corrects the case.
				else if(name.equalsIgnoreCase("AUTOBUILD"))
					name = "AutoBuild";// Corrects the case.
				frame = new ModuleFrame(name);
				frame.setTheme(theme);
				frame.setLayoutManager(new GridLayoutManager(1, 0));
				frame.setVisible(true);
				frame.setClosable(false);
				frame.setMinimized(true);
				frame.setPinnable(true);
				addFrame(frame);
				categoryFrames.put(mod.getCategory(), frame);
			}
			String moduleDescription = mod.getDescription();
			if(moduleDescription.equals(""))
				moduleDescription = "Error! This is a bug. Please report it.";
			final Mod updateModule = mod;
			Button button = new BasicButton(mod.getName(), moduleDescription)
			{
				@Override
				public void update()
				{
					setForegroundColor(updateModule.isEnabled() ? Color.BLACK
						: Color.WHITE);
					setBackgroundColor(updateModule.isEnabled() ? new Color(0,
						255, 0, 128) : new Color(0, 0, 0, 0));
				}
			};
			button.addButtonListener(new ButtonListener()
			{
				@Override
				public void onButtonPress(Button button)
				{
					updateModule.toggle();
				}
			});
			frame.add(button);
			if(!mod.getSliders().isEmpty())
				for(BasicSlider slider : mod.getSliders())
				{
					slider.addSliderListener(new SliderListener()
					{
						@Override
						public void onSliderValueChanged(Slider slider)
						{
							ArrayList<BasicSlider> moduleSliders =
								mod.getSliders();
							if(moduleSliders.contains(slider))
							{
								int id = moduleSliders.indexOf(slider);
								moduleSliders.set(id, (BasicSlider)slider);
								WurstClient.INSTANCE.fileManager.saveSliders();
							}
							mod.setSliders(moduleSliders);
							mod.updateSettings();
						}
					});
					settings.add(slider);
				}
		}
		
		// AutoBuild
		ModuleFrame autobuild = categoryFrames.get(Category.AUTOBUILD);
		ComboBox autoBuildBox =
			new BasicComboBox(
				AutoBuildMod.names.toArray(new String[AutoBuildMod.names.size()]));
		autoBuildBox.addComboBoxListener(new ComboBoxListener()
		{
			@Override
			public void onComboBoxSelectionChanged(ComboBox comboBox)
			{
				WurstClient.INSTANCE.options.autobuildMode =
					comboBox.getSelectedIndex();
				WurstClient.INSTANCE.fileManager.saveOptions();
			}
		});
		autoBuildBox.setSelectedIndex(WurstClient.INSTANCE.options.autobuildMode);
		autobuild.add(autoBuildBox, HorizontalGridConstraint.CENTER);
		
		// Target
		ModuleFrame combatFrame = categoryFrames.get(Category.COMBAT);
		combatFrame.add(new BasicLabel("Target"),
			HorizontalGridConstraint.CENTER);
		ComboBox targetBox =
			new BasicComboBox("All", "Players", "Mobs", "Animals", "Monsters");
		targetBox.addComboBoxListener(new ComboBoxListener()
		{
			@Override
			public void onComboBoxSelectionChanged(ComboBox comboBox)
			{
				WurstClient.INSTANCE.options.targetMode = comboBox.getSelectedIndex();
				WurstClient.INSTANCE.fileManager.saveOptions();
			}
		});
		targetBox.setSelectedIndex(WurstClient.INSTANCE.options.targetMode);
		combatFrame.add(targetBox, HorizontalGridConstraint.CENTER);
		
		if(!WurstClient.INSTANCE.fileManager.sliders.exists())
			WurstClient.INSTANCE.fileManager.saveSliders();
		else
			WurstClient.INSTANCE.fileManager.loadSliders();
		resizeComponents();
		Minecraft minecraft = Minecraft.getMinecraft();
		Dimension maxSize = recalculateSizes();
		int offsetX = 5, offsetY = 5;
		int scale = minecraft.gameSettings.guiScale;
		if(scale == 0)
			scale = 1000;
		int scaleFactor = 0;
		while(scaleFactor < scale
			&& minecraft.displayWidth / (scaleFactor + 1) >= 320
			&& minecraft.displayHeight / (scaleFactor + 1) >= 240)
			scaleFactor++;
		for(Frame frame : getFrames())
		{
			frame.setX(offsetX);
			frame.setY(offsetY);
			offsetX += maxSize.width + 5;
			if(offsetX + maxSize.width + 5 > minecraft.displayWidth
				/ scaleFactor)
			{
				offsetX = 5;
				offsetY += maxSize.height + 5;
			}
		}
		if(WurstClient.INSTANCE.fileManager.gui.exists())
			WurstClient.INSTANCE.fileManager.loadGUI(getFrames());
	}
	
	@Override
	protected void resizeComponents()
	{
		Theme theme = getTheme();
		Frame[] frames = getFrames();
		Button enable = new BasicButton("Enable", "");
		Button disable = new BasicButton("Disable", "");
		Dimension enableSize =
			theme.getUIForComponent(enable).getDefaultSize(enable);
		Dimension disableSize =
			theme.getUIForComponent(disable).getDefaultSize(disable);
		int buttonWidth = Math.max(enableSize.width, disableSize.width);
		int buttonHeight = Math.max(enableSize.height, disableSize.height);
		for(Frame frame : frames)
			if(frame instanceof ModuleFrame)
				for(Component component : frame.getChildren())
					if(component instanceof Button)
					{
						component.setWidth(buttonWidth);
						component.setHeight(buttonHeight);
					}
		recalculateSizes();
	}
	
	private Dimension recalculateSizes()
	{
		Frame[] frames = getFrames();
		int maxWidth = 0, maxHeight = 0;
		for(Frame frame : frames)
		{
			if(frame.getTitle().equalsIgnoreCase("settings"))
				continue;
			Dimension defaultDimension =
				frame.getTheme().getUIForComponent(frame).getDefaultSize(frame);
			maxWidth = Math.max(maxWidth, defaultDimension.width);
			frame.setHeight(defaultDimension.height);
			if(frame.isMinimized())
				for(Rectangle area : frame.getTheme().getUIForComponent(frame)
					.getInteractableRegions(frame))
					maxHeight = Math.max(maxHeight, area.height);
			else
				maxHeight = Math.max(maxHeight, defaultDimension.height);
		}
		for(Frame frame : frames)
		{
			if(frame.getTitle().equalsIgnoreCase("settings"))
				continue;
			frame.setWidth(maxWidth);
			if(frame.getTitle().equals("AutoBuild"))
				frame.setWidth((int)(maxWidth * 1.2));
			frame.layoutChildren();
		}
		Frame[] frames1 = getFrames();
		int maxWidth1 = 0, maxHeight1 = 0;
		for(Frame frame : frames1)
		{
			if(!frame.getTitle().equalsIgnoreCase("settings"))
				continue;
			Dimension defaultDimension =
				frame.getTheme().getUIForComponent(frame).getDefaultSize(frame);
			maxWidth1 = Math.max(maxWidth1, defaultDimension.width);
			frame.setHeight(defaultDimension.height);
			if(frame.isMinimized())
				for(Rectangle area : frame.getTheme().getUIForComponent(frame)
					.getInteractableRegions(frame))
					maxHeight1 = Math.max(maxHeight1, area.height);
			else
				maxHeight1 = Math.max(maxHeight1, defaultDimension.height);
		}
		for(Frame frame : frames1)
		{
			if(!frame.getTitle().equalsIgnoreCase("settings"))
				continue;
			frame.setWidth(maxWidth1);
			frame.layoutChildren();
		}
		return new Dimension(maxWidth, maxHeight);
	}
}
