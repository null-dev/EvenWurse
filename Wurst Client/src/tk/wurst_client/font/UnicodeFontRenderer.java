/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.font;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import org.newdawn.slick.TrueTypeFont;

public class UnicodeFontRenderer extends FontRenderer
{
	private final TrueTypeFont font;
	
	public UnicodeFontRenderer(Font awtFont)
	{
		super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
		
		font = new TrueTypeFont(awtFont, false);
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
		FONT_HEIGHT = font.getHeight(alphabet) / 2;
	}
	
	@Override
	public int drawString(String string, int x, int y, int color)
	{
		if(string == null)
			return 0;
		glPushMatrix();
		glScaled(0.5, 0.5, 0.5);
		
		boolean blend = glIsEnabled(GL_BLEND);
		boolean lighting = glIsEnabled(GL_LIGHTING);
		boolean texture = glIsEnabled(GL_TEXTURE_2D);
		if(!blend)
			glEnable(GL_BLEND);
		if(lighting)
			glDisable(GL_LIGHTING);
		if(!texture)
			glEnable(GL_TEXTURE_2D);
		x *= 2;
		y *= 2;
		
		font.drawString(x, y, string, new org.newdawn.slick.Color(color));
		
		if(texture)
			glEnable(GL_TEXTURE_2D);
		if(lighting)
			glEnable(GL_LIGHTING);
		if(!blend)
			glDisable(GL_BLEND);
		glPopMatrix();
		return x;
	}
	
	@Override
	public int drawStringWithShadow(String string, float x, float y, int color)
	{
		return drawString(string, (int)x, (int)y, color);
	}
	
	@Override
	public int getCharWidth(char c)
	{
		return getStringWidth(Character.toString(c));
	}
	
	@Override
	public int getStringWidth(String string)
	{
		return font.getWidth(string) / 2;
	}
	
	public int getStringHeight(String string)
	{
		return font.getHeight(string) / 2;
	}
}
