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

import net.minecraft.client.gui.GuiScreen;

import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.input.Mouse;

public abstract class NavigatorScreen extends GuiScreen
{
	protected int scroll = 0;
	private int scrollKnobPosition = 2;
	private boolean scrolling;
	private int maxScroll;
	protected boolean scrollbarLocked;
	protected int middleX;
	protected boolean hasBackground = true;
	protected int nonScrollableArea = 26;
	
	@Override
	public final void initGui()
	{
		middleX = width / 2;
		
		onResize();
	}
	
	@Override
	public final void keyTyped(char typedChar, int keyCode) throws IOException
	{
		onKeyPress(keyCode);
	}
	
	@Override
	public final void mouseClicked(int x, int y, int button) throws IOException
	{
		// vanilla buttons
		super.mouseClicked(x, y, button);
		
		// scrollbar
		if(new Rectangle(width / 2 + 170, 60, 12, height - 103).contains(x, y))
			scrolling = true;
		
		onMouseClick(x, y, button);
	}
	
	@Override
	public final void mouseClickMove(int mouseX, int mouseY,
		int clickedMouseButton, long timeSinceLastClick)
	{
		// scrollbar
		if(scrolling && !scrollbarLocked && clickedMouseButton == 0)
		{
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
		
		onMouseDrag(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}
	
	@Override
	public final void mouseReleased(int x, int y, int button)
	{
		// vanilla buttons
		super.mouseReleased(x, y, button);
		
		// scrollbar
		scrolling = false;
		
		onMouseRelease(x, y, button);
	}
	
	@Override
	public final void updateScreen()
	{
		// scrollbar
		if(!scrollbarLocked)
		{
			scroll += Mouse.getDWheel() / 10;
			
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
		
		onUpdate();
	}
	
	@Override
	public final void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		// GL settings
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_CULL_FACE);
		glDisable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);
		
		// background
		int bgx1 = middleX - 154;
		int bgx2 = middleX + 154;
		int bgy1 = 60;
		int bgy2 = height - 43;
		if(hasBackground)
		{
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
		}
		
		// scrollbar
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
		
		onRender(mouseX, mouseY, partialTicks);
		
		// GL resets
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
	}
	
	protected abstract void onResize();
	
	protected abstract void onKeyPress(int key);
	
	protected abstract void onMouseClick(int x, int y, int button);
	
	protected abstract void onMouseDrag(int x, int y, int button,
		long timeDragged);
	
	protected abstract void onMouseRelease(int x, int y, int button);
	
	protected abstract void onUpdate();
	
	protected abstract void onRender(int mouseX, int mouseY, float partialTicks);
	
	@Override
	public final boolean doesGuiPauseGame()
	{
		return false;
	}
	
	protected void setContentHeight(int contentHeight)
	{
		maxScroll = height - contentHeight - nonScrollableArea - 120;
		if(maxScroll > 0)
			maxScroll = 0;
	}
}
