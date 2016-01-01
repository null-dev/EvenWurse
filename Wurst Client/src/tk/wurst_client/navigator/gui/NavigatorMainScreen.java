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
import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.input.Mouse;

import tk.wurst_client.WurstClient;
import tk.wurst_client.font.Fonts;
import tk.wurst_client.navigator.Navigator;
import tk.wurst_client.navigator.NavigatorItem;

public class NavigatorMainScreen extends GuiScreen
{
	private int scroll = 0;
	private static ArrayList<NavigatorItem> navigatorDisplayList =
		new ArrayList<>();
	private GuiTextField searchBar;
	private int hoveredItem = -1;
	private int clickTimer = -1;
	private boolean expanding = false;
	private int scrollKnobPosition = 2;
	private boolean scrolling;
	
	public NavigatorMainScreen()
	{
		searchBar = new GuiTextField(0, Fonts.segoe22, 0, 32, 200, 20);
		searchBar.setEnableBackgroundDrawing(false);
		searchBar.setMaxStringLength(128);
		searchBar.setFocused(true);
		
		WurstClient.INSTANCE.navigator.copyNavigatorList(navigatorDisplayList);
	}
	
	@Override
	public void initGui()
	{
		searchBar.xPosition = width / 2 - 100;
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
		
		if(button == 0 && clickTimer == -1 && hoveredItem != -1)
			expanding = true;
		if(new Rectangle(width / 2 + 170, 60, 12, height - 103).contains(x, y))
			scrolling = true;
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY,
		int clickedMouseButton, long timeSinceLastClick)
	{
		if(scrolling && clickedMouseButton == 0 && clickTimer == -1)
		{
			int maxScroll =
				-navigatorDisplayList.size() / 3 * 20 + height - 120;
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
		{
			if(clickTimer == -1)
				mc.displayGuiScreen((GuiScreen)null);
		}
		
		if(clickTimer == -1)
		{
			String oldText = searchBar.getText();
			searchBar.textboxKeyTyped(typedChar, keyCode);
			String newText = searchBar.getText();
			Navigator navigator = WurstClient.INSTANCE.navigator;
			if(newText.isEmpty())
				navigator.copyNavigatorList(navigatorDisplayList);
			else if(!newText.equals(oldText))
			{
				newText = newText.toLowerCase();
				navigator.getSearchResults(navigatorDisplayList, newText);
				WurstClient.INSTANCE.navigator.analytics.trackEvent("search",
					"query searched", newText);
				if(navigatorDisplayList.isEmpty())
					WurstClient.INSTANCE.navigator.analytics.trackEvent(
						"search", "no results", newText);
			}
		}
	}
	
	@Override
	public void updateScreen()
	{
		if(clickTimer == -1)
		{
			scroll += Mouse.getDWheel() / 10;
			
			int maxScroll =
				-navigatorDisplayList.size() / 3 * 20 + height - 120;
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
		
		searchBar.updateCursorCounter();
		
		if(expanding)
			if(clickTimer < 4)
				clickTimer++;
			else
			{
				mc.displayGuiScreen(new NavigatorFeatureScreen(
					navigatorDisplayList.get(hoveredItem), this));
				String query = searchBar.getText();
				if(!query.isEmpty())
					WurstClient.INSTANCE.navigator.analytics.trackEvent(
						"search", "result clicked", query.toLowerCase(),
						hoveredItem);
			}
		else if(!expanding && clickTimer > -1)
			clickTimer--;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		boolean clickTimerNotRunning = clickTimer == -1;
		
		// search bar
		if(clickTimerNotRunning)
		{
			Fonts.segoe22.drawString("Search: ", width / 2 - 150, 32, 0xffffff);
			searchBar.drawTextBox();
		}
		
		// GL settings
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_CULL_FACE);
		glDisable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);
		
		// feature list
		int x = width / 2 - 50;
		if(clickTimerNotRunning)
			hoveredItem = -1;
		RenderUtil.scissorBox(0, 59, width, height - 42);
		glEnable(GL_SCISSOR_TEST);
		for(int i = Math.max(-scroll * 3 / 20 - 3, 0); i < navigatorDisplayList
			.size(); i++)
		{
			// y position
			int y = 60 + i / 3 * 20 + scroll;
			if(y < 40)
				continue;
			if(y > height - 40)
				break;
			
			// x position
			int xi = 0;
			switch(i % 3)
			{
				case 0:
					xi = x - 104;
					break;
				case 1:
					xi = x;
					break;
				case 2:
					xi = x + 104;
					break;
			}
			
			// item & area
			NavigatorItem item = navigatorDisplayList.get(i);
			Rectangle area = new Rectangle(xi, y, 100, 16);
			
			// color
			boolean hovering =
				area.contains(mouseX, mouseY) && clickTimerNotRunning;
			if(hovering)
				hoveredItem = i;
			if(item.isEnabled() && clickTimerNotRunning)
				if(item.isBlocked())
					glColor4f(hovering ? 1F : 0.875F, 0F, 0F, 0.5F);
				else
					glColor4f(0F, hovering ? 1F : 0.875F, 0F, 0.5F);
			else if(hovering)
				glColor4f(0.375F, 0.375F, 0.375F, 0.5F);
			else
				glColor4f(0.25F, 0.25F, 0.25F, 0.5F);
			
			// click animation
			if(!clickTimerNotRunning)
			{
				if(i != hoveredItem)
					continue;
				
				float factor;
				if(expanding)
					if(clickTimer == 4)
						factor = 1F;
					else
						factor = (clickTimer + partialTicks) / 4F;
				else if(clickTimer == 0)
					factor = 0F;
				else
					factor = (clickTimer - partialTicks) / 4F;
				float antiFactor = 1 - factor;
				
				area.x =
					(int)(area.x * antiFactor + (width / 2 - 154) * factor);
				area.y = (int)(area.y * antiFactor + 60 * factor);
				area.width = (int)(area.width * antiFactor + 308 * factor);
				area.height =
					(int)(area.height * antiFactor + (height - 103) * factor);
			}
			
			// box & shadow
			glBegin(GL_QUADS);
			{
				glVertex2d(area.x, area.y);
				glVertex2d(area.x + area.width, area.y);
				glVertex2d(area.x + area.width, area.y + area.height);
				glVertex2d(area.x, area.y + area.height);
			}
			glEnd();
			RenderUtil.boxShadow(area.x, area.y, area.x + area.width, area.y
				+ area.height);
			
			// text
			if(clickTimerNotRunning)
			{
				glEnable(GL_TEXTURE_2D);
				try
				{
					String buttonText = item.getName();
					Fonts.segoe15.drawString(
						buttonText,
						area.x
							+ (area.width - Fonts.segoe15
								.getStringWidth(buttonText)) / 2, area.y + 2,
						0xffffff);
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				glDisable(GL_TEXTURE_2D);
			}
		}
		glDisable(GL_SCISSOR_TEST);
		
		// scroll bar
		Rectangle area = new Rectangle(width / 2 + 170, 60, 12, height - 103);
		glColor4f(0.25F, 0.25F, 0.25F, 0.5F);
		glBegin(GL_QUADS);
		{
			glVertex2d(area.x, area.y);
			glVertex2d(area.x + area.width, area.y);
			glVertex2d(area.x + area.width, area.y + area.height);
			glVertex2d(area.x, area.y + area.height);
		}
		glEnd();
		RenderUtil.boxShadow(area.x, area.y, area.x + area.width, area.y
			+ area.height);
		
		// scroll knob
		area.x += 2;
		area.y += scrollKnobPosition;
		area.width = 8;
		area.height = 24;
		glColor4f(0.25F, 0.25F, 0.25F, 0.5F);
		glBegin(GL_QUADS);
		{
			glVertex2d(area.x, area.y);
			glVertex2d(area.x + area.width, area.y);
			glVertex2d(area.x + area.width, area.y + area.height);
			glVertex2d(area.x, area.y + area.height);
		}
		glEnd();
		RenderUtil.boxShadow(area.x, area.y, area.x + area.width, area.y
			+ area.height);
		RenderUtil.downShadow(area.x + 1, area.y + 8, area.x + area.width - 1,
			area.y + 9);
		RenderUtil.downShadow(area.x + 1, area.y + 12, area.x + area.width - 1,
			area.y + 13);
		RenderUtil.downShadow(area.x + 1, area.y + 16, area.x + area.width - 1,
			area.y + 17);
		
		// GL resets
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
	}
	
	public void setExpanding(boolean expanding)
	{
		this.expanding = expanding;
	}
}
