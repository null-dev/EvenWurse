/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import tk.wurst_client.command.Command;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.GUIRenderListener;
import tk.wurst_client.event.listeners.UpdateListener;

public class Taco extends Command implements GUIRenderListener, UpdateListener
{
	private static final ResourceLocation tacoTexture1 = new ResourceLocation(
		"wurst/dancingtaco1.png");
	private static final ResourceLocation tacoTexture2 = new ResourceLocation(
		"wurst/dancingtaco2.png");
	private static final ResourceLocation tacoTexture3 = new ResourceLocation(
		"wurst/dancingtaco3.png");
	private static final ResourceLocation tacoTexture4 = new ResourceLocation(
		"wurst/dancingtaco4.png");
	private static final ResourceLocation[] tacoTextures = {tacoTexture1,
		tacoTexture2, tacoTexture3, tacoTexture4};
	private int ticks = 0;
	private boolean toggled;
	
	public Taco()
	{
		super("taco",
			"\"I love that little guy. So cute!\" -WiZARDHAX",
			"§o.taco§r");
	}
	
	@Override
	public void execute(String input, String[] args)
	{
		toggled = !toggled;
		if(toggled)
		{
			EventManager.guiRender.addListener(this);
			EventManager.update.addListener(this);
		}else
		{
			EventManager.guiRender.removeListener(this);
			EventManager.update.removeListener(this);
		}
	}
	
	@Override
	public void onRenderGUI()
	{
		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		Tessellator var3 = Tessellator.getInstance();
		WorldRenderer var4 = var3.getWorldRenderer();
		ScaledResolution screenRes =
			new ScaledResolution(Minecraft.getMinecraft(),
				Minecraft.getMinecraft().displayWidth,
				Minecraft.getMinecraft().displayHeight);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if(ticks >= 32)
			ticks = 0;
		Minecraft.getMinecraft().getTextureManager()
			.bindTexture(tacoTextures[ticks / 8]);
		double x = screenRes.getScaledWidth() / 2 - 32 + 76;
		double y = screenRes.getScaledHeight() - 32 - 19;
		double h = 32;
		double w = 64;
		double fw = 256;
		double fh = 256;
		double u = 0;
		double v = 0;
		var4.startDrawingQuads();
		var4.addVertexWithUV(x + 0, y + h, 0, (float)(u + 0) * 0.00390625F,
			(float)(v + fh) * 0.00390625F);
		var4.addVertexWithUV(x + w, y + h, 0, (float)(u + fw) * 0.00390625F,
			(float)(v + fh) * 0.00390625F);
		var4.addVertexWithUV(x + w, y + 0, 0, (float)(u + fw) * 0.00390625F,
			(float)(v + 0) * 0.00390625F);
		var4.addVertexWithUV(x + 0, y + 0, 0, (float)(u + 0) * 0.00390625F,
			(float)(v + 0) * 0.00390625F);
		var3.draw();
		glEnable(GL_CULL_FACE);
		glDisable(GL_BLEND);
	}
	
	@Override
	public void onUpdate()
	{
		ticks++;
	}
}
