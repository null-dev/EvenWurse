/*
 * Copyright � 2014 - 2015 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.navigator.gui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.input.Mouse;

import tk.wurst_client.WurstClient;
import tk.wurst_client.font.Fonts;
import tk.wurst_client.navigator.NavigatorPossibleKeybind;

public class NavigatorRemoveKeybindScreen extends GuiScreen
{
	private NavigatorFeatureScreen parent;
	private TreeMap<String, NavigatorPossibleKeybind> existingKeybinds;
	private String hoveredKeybind = "";
	private String selectedKey = "";
	private int scroll = 0;
	private int scrollKnobPosition = 2;
	private boolean scrolling;
	private int contentHeight;
	private String text = "Select the keybind you want to remove.";
	private GuiButton removeButton;
	
	public NavigatorRemoveKeybindScreen(
		TreeMap<String, NavigatorPossibleKeybind> existingKeybinds,
		NavigatorFeatureScreen parent)
	{
		this.existingKeybinds = existingKeybinds;
		this.parent = parent;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		// OK button
		removeButton =
			new GuiButton(0, width / 2 - 151, height - 65, 149, 18, "Remove");
		removeButton.enabled = !selectedKey.isEmpty();
		buttonList.add(removeButton);
		
		// cancel button
		buttonList.add(new GuiButton(1, width / 2 + 2, height - 65, 149, 18,
			"Cancel"));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(!button.enabled)
			return;
		
		switch(button.id)
		{
			case 0:
				WurstClient wurst = WurstClient.INSTANCE;
				wurst.keybinds.remove(selectedKey);
				wurst.files.saveKeybinds();
				mc.displayGuiScreen(parent);
				break;
			case 1:
				mc.displayGuiScreen(parent);
				break;
		}
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException
	{
		super.mouseClicked(x, y, button);
		
		// scrollbar
		if(new Rectangle(width / 2 + 170, 60, 12, height - 103).contains(x, y))
		{
			scrolling = true;
			return;
		}
		
		// commands
		if(!hoveredKeybind.isEmpty())
		{
			selectedKey = hoveredKeybind;
			removeButton.enabled = true;
		}
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY,
		int clickedMouseButton, long timeSinceLastClick)
	{
		if(clickedMouseButton != 0)
			return;
		if(scrolling)
		{
			int maxScroll = -contentHeight + height - 146;
			if(maxScroll > 0)
				maxScroll = 0;
			
			if(maxScroll == 0)
				scroll = 0;
			else
				scroll =
					(int)((mouseY - 72) * (float)maxScroll / (height - 131));
			
			if(scroll > 0)
				scroll = 0;
			else if(scroll < maxScroll)
				scroll = maxScroll;
		}
	}
	
	@Override
	public void mouseReleased(int x, int y, int button)
	{
		super.mouseReleased(x, y, button);
		
		scrolling = false;
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(keyCode == 1)
			mc.displayGuiScreen(parent);
	}
	
	@Override
	public void updateScreen()
	{
		// content height
		contentHeight = existingKeybinds.size() * 24 - 10;
		
		// scroll
		scroll += Mouse.getDWheel() / 10;
		
		int maxScroll = -contentHeight + height - 146;
		if(maxScroll > 0)
			maxScroll = 0;
		
		if(scroll > 0)
			scroll = 0;
		else if(scroll < maxScroll)
			scroll = maxScroll;
		
		if(maxScroll == 0)
			scrollKnobPosition = 0;
		else
			scrollKnobPosition =
				(int)((height - 131) * scroll / (float)maxScroll);
		scrollKnobPosition += 2;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		int middleX = width / 2;
		
		// GL settings
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_CULL_FACE);
		glShadeModel(GL_SMOOTH);
		
		// title bar
		drawCenteredString(Fonts.segoe22, "Remove Keybind", middleX, 32,
			0xffffff);
		glDisable(GL_TEXTURE_2D);
		
		// background
		int bgx1 = middleX - 154;
		int bgx2 = middleX + 154;
		int bgy1 = 60;
		int bgy2 = height - 43;
		glColor4f(0.25F, 0.25F, 0.25F, 0.5F);
		glBegin(GL_QUADS);
		{
			glVertex2i(bgx1, bgy1);
			glVertex2i(bgx2, bgy1);
			glVertex2i(bgx2, bgy2);
			glVertex2i(bgx1, bgy2);
		}
		glEnd();
		RenderUtil.boxShadow(bgx1, bgy1, bgx2, bgy2);
		
		// scroll bar
		{
			// bar
			int x1 = bgx2 + 16;
			int x2 = x1 + 12;
			int y1 = bgy1;
			int y2 = bgy2;
			glColor4f(0.25F, 0.25F, 0.25F, 0.5F);
			glBegin(GL_QUADS);
			{
				glVertex2i(x1, y1);
				glVertex2i(x2, y1);
				glVertex2i(x2, y2);
				glVertex2i(x1, y2);
			}
			glEnd();
			RenderUtil.boxShadow(x1, y1, x2, y2);
			
			// knob
			x1 += 2;
			x2 -= 2;
			y1 += scrollKnobPosition;
			y2 = y1 + 24;
			glColor4f(0.25F, 0.25F, 0.25F, 0.5F);
			glBegin(GL_QUADS);
			{
				glVertex2i(x1, y1);
				glVertex2i(x2, y1);
				glVertex2i(x2, y2);
				glVertex2i(x1, y2);
			}
			glEnd();
			RenderUtil.boxShadow(x1, y1, x2, y2);
			int i;
			for(x1++, x2--, y1 += 8, y2 -= 15, i = 0; i < 3; y1 += 4, y2 += 4, i++)
				RenderUtil.downShadow(x1, y1, x2, y2);
		}
		
		// scissor box
		RenderUtil.scissorBox(bgx1, bgy1, bgx2, bgy2
			- (buttonList.isEmpty() ? 0 : 24));
		glEnable(GL_SCISSOR_TEST);
		
		// possible keybinds
		hoveredKeybind = "";
		int yi = bgy1 - 12 + scroll;
		for(Entry<String, NavigatorPossibleKeybind> entry : existingKeybinds
			.entrySet())
		{
			String key = entry.getKey();
			NavigatorPossibleKeybind keybind = entry.getValue();
			yi += 24;
			
			// positions
			int x1 = bgx1 + 2;
			int x2 = bgx2 - 2;
			int y1 = yi;
			int y2 = y1 + 20;
			
			// color
			if(mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2)
			{
				hoveredKeybind = key;
				if(key.equals(selectedKey))
					glColor4f(0F, 1F, 0F, 0.375F);
				else
					glColor4f(0.25F, 0.25F, 0.25F, 0.375F);
			}else if(key.equals(selectedKey))
				glColor4f(0F, 1F, 0F, 0.25F);
			else
				glColor4f(0.25F, 0.25F, 0.25F, 0.25F);
			
			// button
			glBegin(GL_QUADS);
			{
				glVertex2i(x1, y1);
				glVertex2i(x2, y1);
				glVertex2i(x2, y2);
				glVertex2i(x1, y2);
			}
			glEnd();
			RenderUtil.boxShadow(x1, y1, x2, y2);
			
			// text
			drawString(Fonts.segoe15, key + ": " + keybind.getDescription()
				+ "\n" + keybind.getCommand(), x1 + 1, y1 - 1, 0xffffff);
			glDisable(GL_TEXTURE_2D);
		}
		
		// text
		drawString(Fonts.segoe15, text, bgx1 + 2, bgy1 + scroll, 0xffffff);
		
		// scissor box
		glDisable(GL_SCISSOR_TEST);
		
		// buttons below scissor box
		for(int i = 0; i < buttonList.size(); i++)
		{
			GuiButton button = (GuiButton)buttonList.get(i);
			
			// positions
			int x1 = button.xPosition;
			int x2 = x1 + button.getButtonWidth();
			int y1 = button.yPosition;
			int y2 = y1 + 18;
			
			// color
			if(!button.enabled)
				glColor4f(0F, 0F, 0F, 0.25F);
			else if(mouseX >= x1 && mouseX <= x2 && mouseY >= y1
				&& mouseY <= y2)
				glColor4f(0.375F, 0.375F, 0.375F, 0.25F);
			else
				glColor4f(0.25F, 0.25F, 0.25F, 0.25F);
			
			// button
			glDisable(GL_TEXTURE_2D);
			glBegin(GL_QUADS);
			{
				glVertex2i(x1, y1);
				glVertex2i(x2, y1);
				glVertex2i(x2, y2);
				glVertex2i(x1, y2);
			}
			glEnd();
			RenderUtil.boxShadow(x1, y1, x2, y2);
			
			// text
			drawCenteredString(Fonts.segoe18, button.displayString,
				(x1 + x2) / 2, y1 + 2, 0xffffff);
		}
		
		// GL resets
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
	}
}
