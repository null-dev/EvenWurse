package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends Gui
{
	protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
	
	/** Button width in pixels */
	protected int width;
	
	/** Button height in pixels */
	protected int height;
	
	/** The x position of this control. */
	public int xPosition;
	
	/** The y position of this control. */
	public int yPosition;
	
	/** The string displayed on this control. */
	public String displayString;
	public int id;
	
	/** True if this control is enabled, false to disable. */
	public boolean enabled;
	
	/** Hides the button completely if false. */
	public boolean visible;
	protected boolean hovered;
	
	public GuiButton(int buttonId, int x, int y, String buttonText)
	{
		this(buttonId, x, y, 200, 20, buttonText);
	}
	
	public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
	{
		width = 200;
		height = 20;
		enabled = true;
		visible = true;
		id = buttonId;
		xPosition = x;
		yPosition = y;
		width = widthIn;
		height = heightIn;
		displayString = buttonText;
	}
	
	/**
	 * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over
	 * this button and 2 if it IS hovering over
	 * this button.
	 */
	protected int getHoverState(boolean mouseOver)
	{
		byte var2 = 1;
		
		if(!enabled)
			var2 = 0;
		else if(mouseOver)
			var2 = 2;
		
		return var2;
	}
	
	/**
	 * Draws this button to the screen.
	 */
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
	{
		if(visible)
		{
			FontRenderer var4 = mc.fontRendererObj;
			mc.getTextureManager().bindTexture(buttonTextures);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
			int var5 = getHoverState(hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			
			/*
			 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
			 * 
			 * This Source Code Form is subject to the terms of the Mozilla
			 * Public
			 * License, v. 2.0. If a copy of the MPL was not distributed with
			 * this
			 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
			 */
			{
				drawTexturedModalRect(xPosition, yPosition, 0, 46 + var5 * 20, width / 2, height / 2);
				drawTexturedModalRect(xPosition + width / 2, yPosition, 200 - width / 2, 46 + var5 * 20, width / 2, height / 2);
				drawTexturedModalRect(xPosition, yPosition + height / 2, 0, 46 + var5 * 20 + 20 - height / 2, width / 2, height / 2);
				drawTexturedModalRect(xPosition + width / 2, yPosition + height / 2, 200 - width / 2, 46 + var5 * 20 + 20 - height / 2, width / 2, height / 2);
			}
			
			mouseDragged(mc, mouseX, mouseY);
			int var6 = 14737632;
			
			if(!enabled)
				var6 = 10526880;
			else if(hovered)
				var6 = 16777120;
			
			drawCenteredString(var4, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, var6);
		}
	}
	
	/**
	 * Fired when the mouse button is dragged. Equivalent of
	 * MouseListener.mouseDragged(MouseEvent e).
	 */
	protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
	{}
	
	/**
	 * Fired when the mouse button is released. Equivalent of
	 * MouseListener.mouseReleased(MouseEvent e).
	 */
	public void mouseReleased(int mouseX, int mouseY)
	{}
	
	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of
	 * MouseListener.mousePressed(MouseEvent
	 * e).
	 */
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		return enabled && visible && mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
	}
	
	/**
	 * Whether the mouse cursor is currently over the button.
	 */
	public boolean isMouseOver()
	{
		return hovered;
	}
	
	public void drawButtonForegroundLayer(int mouseX, int mouseY)
	{}
	
	public void playPressSound(SoundHandler soundHandlerIn)
	{
		soundHandlerIn.playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0F));
	}
	
	public int getButtonWidth()
	{
		return width;
	}
	
	public void func_175211_a(int p_175211_1_)
	{
		width = p_175211_1_;
	}
}
