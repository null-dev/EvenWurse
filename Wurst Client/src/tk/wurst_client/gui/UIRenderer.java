/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.util.LinkedList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;

import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;
import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.opengl.GL11;

import tk.wurst_client.WurstClient;
import tk.wurst_client.events.GUIRenderEvent;
import tk.wurst_client.font.Fonts;
import tk.wurst_client.mods.ClickGuiMod;
import tk.wurst_client.mods.Mod;

public class UIRenderer
{
	private static final ResourceLocation wurstLogo = new ResourceLocation(
		"wurst/wurst_128.png");
	
	private static void renderModList()
	{
		if(WurstClient.INSTANCE.options.modListMode == 2)
			return;
		LinkedList<String> modList = new LinkedList<String>();
		for(Mod mod : WurstClient.INSTANCE.modManager.getAllMods())
		{
			if(mod instanceof ClickGuiMod)
				continue;
			if(mod.isActive())
				modList.add(mod.getRenderName());
		}
		ScaledResolution sr =
			new ScaledResolution(Minecraft.getMinecraft(),
				Minecraft.getMinecraft().displayWidth,
				Minecraft.getMinecraft().displayHeight);
		int yCount = 19;
		if(yCount + modList.size() * 9 > sr.getScaledHeight()
			|| WurstClient.INSTANCE.options.modListMode == 1)
		{
			String tooManyMods = "";
			if(modList.isEmpty())
				return;
			else if(modList.size() > 1)
				tooManyMods = modList.size() + " mods active";
			else
				tooManyMods = "1 mod active";
			Fonts.segoe18.drawString(tooManyMods, 3, yCount + 1, 0xFF000000);
			Fonts.segoe18.drawString(tooManyMods, 2, yCount, 0xFFFFFFFF);
		}else
			for(String name; (name = modList.poll()) != null;)
			{
				Fonts.segoe18.drawString(name, 3, yCount + 1, 0xFF000000);
				Fonts.segoe18.drawString(name, 2, yCount, 0xFFFFFFFF);
				yCount += 9;
			}
	}
	
	public static void renderUI(float zLevel)
	{
		// GL settings
		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		glDisable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		RenderUtil.setColor(new Color(255, 255, 255, 128));
		
		// get version string
		String version =
			"v"
				+ WurstClient.VERSION
				+ (WurstClient.INSTANCE.updater.isOutdated() ? " (outdated)"
					: "");
		
		// draw version background
		glBegin(GL_QUADS);
		{
			glVertex2d(0, 6);
			glVertex2d(Fonts.segoe22.getStringWidth(version) + 78, 6);
			glVertex2d(Fonts.segoe22.getStringWidth(version) + 78, 18);
			glVertex2d(0, 18);
		}
		glEnd();
		
		// draw version string
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		Fonts.segoe22.drawString(version, 74, 4, 0xFF000000);
		
		// mod list & pinned frames
		renderModList();
		UIRenderer.renderPinnedFrames();
		
		// Wurst logo
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(wurstLogo);
		Tessellator ts = Tessellator.getInstance();
		WorldRenderer wr = ts.getWorldRenderer();
		double x = 0;
		double y = 3;
		double h = 18;
		double w = 72;
		double fw = 256;
		double fh = 256;
		double u1 = 0;
		double v1 = 0;
		wr.startDrawingQuads();
		wr.addVertexWithUV(x + 0, y + h, zLevel, (float)(u1 + 0) / 256D,
			(float)(v1 + fh) / 256D);
		wr.addVertexWithUV(x + w, y + h, zLevel, (float)(u1 + fw) / 256D,
			(float)(v1 + fh) / 256D);
		wr.addVertexWithUV(x + w, y + 0, zLevel, (float)(u1 + fw) / 256D,
			(float)(v1 + 0) / 256D);
		wr.addVertexWithUV(x + 0, y + 0, zLevel, (float)(u1 + 0) / 256D,
			(float)(v1 + 0) / 256D);
		ts.draw();
		
		// GUI render event
		WurstClient.INSTANCE.eventManager.fireEvent(GUIRenderEvent.class,
			new GUIRenderEvent());
		
		// GL resets
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		// is this needed?
		GL11.glPushMatrix();
		GL11.glPopMatrix();
	}
	
	public static void renderPinnedFrames()
	{
		for(Frame moduleFrame : WurstClient.INSTANCE.guiManager.getFrames())
			if(moduleFrame.isPinned()
				&& !(Minecraft.getMinecraft().currentScreen instanceof GuiManagerDisplayScreen))
				moduleFrame.render();
	}
}
