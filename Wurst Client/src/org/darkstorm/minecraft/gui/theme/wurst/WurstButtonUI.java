/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.darkstorm.minecraft.gui.theme.wurst;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import net.minecraft.client.Minecraft;

import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;
import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;
import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.input.Mouse;

import tk.wurst_client.WurstClient;
import tk.wurst_client.mods.Mod;
import tk.wurst_client.mods.Mod.Category;

public class WurstButtonUI extends AbstractComponentUI<Button>
{
	private final WurstTheme theme;
	private long lastMS;
	private Button describedButton;
	private Button rightButton;
	
	WurstButtonUI(WurstTheme theme)
	{
		super(Button.class);
		this.theme = theme;
		
		foreground = Color.WHITE;
		background = new Color(0, 0, 0, 0);
	}
	
	@Override
	protected void renderComponent(Button button)
	{
		translateComponent(button, false);
		
		// area
		Rectangle area = button.getArea();
		area.width = button.getParent().getWidth() - 4;
		
		// mouse location
		Point mouse = RenderUtil.calculateMouseLocation();
		Component parent = button.getParent();
		while(parent != null)
		{
			mouse.x -= parent.getX();
			mouse.y -= parent.getY();
			parent = parent.getParent();
		}
		Point rawMouse = RenderUtil.calculateMouseLocation();
		
		// GL settings
		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		glDisable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);
		
		// background
		RenderUtil.setColor(button.getBackgroundColor());
		glBegin(GL_QUADS);
		{
			glVertex2d(0, 1);
			glVertex2d(area.width, 1);
			glVertex2d(area.width, area.height - 1);
			glVertex2d(0, area.height - 1);
		}
		glEnd();
		RenderUtil.boxShadow(0, 1, area.width, area.height - 1);
		
		// hover overlay
		if(area.contains(mouse)
			&& Minecraft.getMinecraft().currentScreen instanceof GuiManagerDisplayScreen)
		{
			glColor4f(0.0f, 0.0f, 0.0f, Mouse.isButtonDown(0) ? 0.3f : 0.2f);
			glBegin(GL_QUADS);
			{
				glVertex2d(0, 1);
				glVertex2d(area.width, 1);
				glVertex2d(area.width, area.height - 1);
				glVertex2d(0, area.height - 1);
			}
			glEnd();
		}
		
		// text
		String text = button.getText();
		theme.getFontRenderer().drawString(text,
			area.width / 2 - theme.getFontRenderer().getStringWidth(text) / 2,
			area.height / 2 - theme.getFontRenderer().FONT_HEIGHT / 2 - 1,
			RenderUtil.toRGBA(button.getForegroundColor()));
		
		translateComponent(button, true);
		
		// tooltip
		// TODO: clean up
		if(area.contains(mouse) && describedButton != button)
		{
			lastMS = 0L;
			describedButton = button;
		}
		if(lastMS == 0L && describedButton != null
			&& isRightButton(button, describedButton))
		{
			lastMS = System.currentTimeMillis();
			rightButton = button;
			return;
		}
		if(System.currentTimeMillis() < lastMS + 500)
			return;
		if(describedButton != null
			&& rightButton == button
			&& Minecraft.getMinecraft().currentScreen instanceof GuiManagerDisplayScreen)
		{
			String[] lines = describedButton.getDescription().split("\n");
			int textWidth = 0;
			for(String line : lines)
			{
				int lineWidth = theme.getFontRenderer().getStringWidth(line);
				if(lineWidth > textWidth)
					textWidth = lineWidth;
			}
			int textHeight =
				(theme.getFontRenderer().FONT_HEIGHT + 2) * lines.length;
			Rectangle dArea = describedButton.getArea();
			dArea.width = describedButton.getParent().getWidth() - 4;
			Mod mod =
				WurstClient.INSTANCE.modManager.getModByName(button.getText());
			for(Frame frame : WurstClient.INSTANCE.guiManager.getFrames())
				if(frame.getTitle().equalsIgnoreCase(mod.getCategory().name()))
					WurstClient.INSTANCE.guiManager.bringForward(frame);
			int scale = Minecraft.getMinecraft().gameSettings.guiScale;
			if(scale == 0)
				scale = 1000;
			int scaleFactor = 0;
			while(scaleFactor < scale
				&& Minecraft.getMinecraft().displayWidth / (scaleFactor + 1) >= 320
				&& Minecraft.getMinecraft().displayHeight / (scaleFactor + 1) >= 240)
				scaleFactor++;
			if(rawMouse.x + 8 + textWidth > Minecraft.getMinecraft().displayWidth
				/ scaleFactor)
				rawMouse.x -= textWidth + 16;
			if(rawMouse.y - 5 + textHeight > Minecraft.getMinecraft().displayHeight
				/ scaleFactor)
				rawMouse.y -= textHeight - 8;
			glEnable(GL_BLEND);
			glDisable(GL_CULL_FACE);
			glDisable(GL_TEXTURE_2D);
			glColor4f(0.125f, 0.125f, 0.125f, 0.5f);
			glBegin(GL_QUADS);
			{
				glVertex2d(rawMouse.x + 6, rawMouse.y - 5);
				glVertex2d(rawMouse.x + textWidth + 11, rawMouse.y - 5);
				glVertex2d(rawMouse.x + textWidth + 11, rawMouse.y - 5
					+ textHeight);
				glVertex2d(rawMouse.x + 6, rawMouse.y - 5 + textHeight);
			}
			glEnd();
			RenderUtil.boxShadow(rawMouse.x + 6, rawMouse.y - 5, rawMouse.x
				+ textWidth + 11, rawMouse.y - 5 + textHeight);
			for(int i = 0; i < lines.length; i++)
				theme.getFontRenderer().drawString(
					lines[i],
					rawMouse.x + 8,
					rawMouse.y - 5 + (theme.getFontRenderer().FONT_HEIGHT + 2)
						* i, RenderUtil.toRGBA(Color.WHITE));
		}
		if(!area.contains(mouse) && describedButton == button)
		{
			lastMS = 0L;
			describedButton = null;
			rightButton = null;
		}
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
	}
	
	private boolean isRightButton(Button button, Button dButton)
	{
		try
		{
			Category buttonCategory = null;
			Category dButtonCategory = null;
			buttonCategory =
				WurstClient.INSTANCE.modManager.getModByName(button.getText())
					.getCategory();
			dButtonCategory =
				WurstClient.INSTANCE.modManager.getModByName(dButton.getText())
					.getCategory();
			boolean isRightFrame =
				buttonCategory == dButtonCategory && buttonCategory != null;
			if(!isRightFrame)
				return false;
			boolean isLastButton = false;
			for(Mod mod : WurstClient.INSTANCE.modManager.getAllMods())
				if(buttonCategory == mod.getCategory())
					if(button.getText().equals(mod.getName()))
						isLastButton = true;
					else
						isLastButton = false;
			return isLastButton;
		}catch(NullPointerException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	protected Dimension getDefaultComponentSize(Button component)
	{
		return new Dimension(theme.getFontRenderer().getStringWidth(
			component.getText()) + 4, theme.getFontRenderer().FONT_HEIGHT + 4);
	}
	
	@Override
	protected Rectangle[] getInteractableComponentRegions(Button component)
	{
		return new Rectangle[]{new Rectangle(0, 0, component.getWidth(),
			component.getHeight())};
	}
	
	@Override
	protected void handleComponentInteraction(Button component, Point location,
		int button)
	{
		if(location.x <= component.getWidth()
			&& location.y <= component.getHeight() && button == 0)
			component.press();
	}
}
