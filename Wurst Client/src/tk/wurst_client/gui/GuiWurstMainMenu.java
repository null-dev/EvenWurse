/*
 * Copyright © 2014 - 2015 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;

import javax.net.ssl.HttpsURLConnection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import tk.wurst_client.WurstClient;
import tk.wurst_client.alts.gui.GuiAlts;
import tk.wurst_client.message.gui.GuiMessage;
import tk.wurst_client.utils.MiscUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GuiWurstMainMenu extends GuiMainMenu
{
	private static final ResourceLocation title = new ResourceLocation(
		"wurst/wurst_380.png");
	private static final ResourceLocation santaHat = new ResourceLocation(
		"wurst/santa_hat.png");
	private static final ResourceLocation buttons = new ResourceLocation(
		"wurst/buttons.png");
	private XMLElementList news;
	private String newsTicker;
	private int newsWidth;
	
	public GuiWurstMainMenu()
	{
		super();
		
		if(WurstClient.INSTANCE.options.wurstNews)
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						HttpsURLConnection connection =
							(HttpsURLConnection)new URL(
								"https://www.wurst-client.tk/news/feed.xml")
								.openConnection();
						connection.connect();
						XMLElement xml =
							new XMLParser().parse("",
								connection.getInputStream());
						news =
							xml.getChildrenByName("channel").get(0)
								.getChildrenByName("item");
					}catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}).start();
		
		WurstClient.INSTANCE.analytics.trackPageView("/", "Main Menu");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		
		// adjust position of options, quit & language buttons
		for(int i = 3; i <= 5; i++)
			((GuiButton)buttonList.get(i)).yPosition =
				Math.min(((GuiButton)buttonList.get(i)).yPosition, height - 56);
		
		// social buttons
		for(int i = 0; i < 3; i++)
		{
			GuiButton button =
				new GuiButton(20 + i, 8 + i * 24, height - 36, 20, 20, "");
			buttonList.add(button);
		}
		for(int i = 0; i < 3; i++)
		{
			GuiButton button =
				new GuiButton(25 - i, width - 28 - i * 24, height - 36, 20, 20,
					"");
			buttonList.add(button);
		}
		
		// news
		newsTicker = "";
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// wait for news to load
				while(news == null)
					try
					{
						Thread.sleep(50);
					}catch(InterruptedException e1)
					{
						e1.printStackTrace();
					}
				
				// build news ticker
				try
				{
					for(int i = 0; i < news.size(); i++)
						newsTicker +=
							news.get(i).getChildrenByName("title").get(0)
								.getContent()
								+ "§e+++§r";
				}catch(ConcurrentModificationException e)
				{	
					
				}
				newsWidth = fontRendererObj.getStringWidth(newsTicker);
				while(fontRendererObj.getStringWidth(newsTicker) < Math.max(
					width * 2, newsWidth * 2) && !newsTicker.isEmpty())
					newsTicker += newsTicker;
			}
		}).start();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		
		switch(button.id)
		{
			case 3:
				mc.displayGuiScreen(new GuiAlts(this));
				break;
			case 20:
				MiscUtils.openLink("https://www.wurst-client.tk/youtube");
				break;
			case 21:
				MiscUtils.openLink("https://www.wurst-client.tk/twitter");
				break;
			case 22:
				MiscUtils.openLink("https://www.wurst-client.tk/gplus");
				break;
			case 23:
				MiscUtils.openLink("https://www.wurst-client.tk/github");
				break;
			case 24:
				MiscUtils.openLink("https://www.wurst-client.tk/feedback");
				break;
			case 25:
				MiscUtils.openLink("https://www.wurst-client.tk/fanshop");
				break;
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		// panorama
		GlStateManager.disableAlpha();
		renderSkybox(mouseX, mouseY, partialTicks);
		GlStateManager.enableAlpha();
		drawGradientRect(0, 0, width, height, -2130706433, 16777215);
		drawGradientRect(0, 0, width, height, 0, Integer.MIN_VALUE);
		
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		
		// title image
		GuiScreen.mc.getTextureManager().bindTexture(title);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		double x = width / 2 - 256 / 2;
		double y = 36;
		double h = 64;
		double w = 256;
		double fw = 256;
		double fh = 256;
		double u = 0;
		double v = 0;
		if(GuiMainMenu.splashText.equals("umop-apisdn!"))
		{
			GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-width, (float)(-h - 60), 0.0F);
		}
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertexWithUV(x + 0, y + h, zLevel, (u + 0) / 256D,
			(v + fh) / 256D);
		worldRenderer.addVertexWithUV(x + w, y + h, zLevel, (u + fw) / 256D,
			(v + fh) / 256F);
		worldRenderer.addVertexWithUV(x + w, y + 0, zLevel, (u + fw) / 256D,
			(v + 0) / 256D);
		worldRenderer.addVertexWithUV(x + 0, y + 0, zLevel, (u + 0) / 256D,
			(v + 0) / 256D);
		tessellator.draw();
		if(Calendar.getInstance().get(Calendar.MONTH) == Calendar.DECEMBER)
		{
			mc.getTextureManager().bindTexture(santaHat);
			x = x + 112;
			y = y - 36;
			h = 48;
			w = 48;
			fw = 256;
			fh = 256;
			u = 0;
			v = 0;
			worldRenderer.startDrawingQuads();
			worldRenderer.addVertexWithUV(x + 0, y + h, 0,
				(float)(u + 0) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			worldRenderer.addVertexWithUV(x + w, y + h, 0,
				(float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			worldRenderer.addVertexWithUV(x + w, y + 0, 0,
				(float)(u + fw) * 0.00390625F, (float)(v + 0) * 0.00390625F);
			worldRenderer.addVertexWithUV(x + 0, y + 0, 0,
				(float)(u + 0) * 0.00390625F, (float)(v + 0) * 0.00390625F);
			tessellator.draw();
		}
		if(GuiMainMenu.splashText.equals("umop-apisdn!"))
		{
			GL11.glRotatef(-180.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-width, (float)(-h - 60), 0.0F);
		}
		
		// splash text
		worldRenderer.setColorOpaque_I(0xffffff);
		GlStateManager.pushMatrix();
		GlStateManager.translate(width / 2 + 90, 72.0F, 0.0F);
		GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
		float splashScale =
			1.8F - MathHelper.abs(MathHelper.sin(Minecraft.getSystemTime()
				% 1000L / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
		splashScale =
			splashScale * 100.0F
				/ (fontRendererObj.getStringWidth(splashText) + 32);
		GlStateManager.scale(splashScale, splashScale, splashScale);
		
		drawCenteredString(fontRendererObj, splashText, 0, 0, -256);
		GlStateManager.popMatrix();
		
		// text
		String vMinecraft = "Minecraft 1.8";
		String cMinecraft1 = "Copyright Mojang AB";
		String cMinecraft2 = "Do not distribute!";
		drawString(fontRendererObj, vMinecraft,
			width - fontRendererObj.getStringWidth(vMinecraft) - 8, 8, 0xffffff);
		drawString(fontRendererObj, cMinecraft1,
			width - fontRendererObj.getStringWidth(cMinecraft1) - 8, 18,
			0xffffff);
		drawString(fontRendererObj, cMinecraft2,
			width - fontRendererObj.getStringWidth(cMinecraft2) - 8, 28,
			0xffffff);
		drawString(fontRendererObj, "Wurst Client " + WurstClient.VERSION, 8,
			8, 0xffffff);
		drawString(fontRendererObj, "Copyright Alexander01998", 8, 18, 0xffffff);
		drawString(fontRendererObj, "All rights reserved.", 8, 28, 0xffffff);
		drawCenteredString(fontRendererObj, "§nwww.Wurst-Client.tk", width / 2,
			height - 26, 0xffffff);
		
		// buttons
		for(Object button : buttonList)
			((GuiButton)button).drawButton(mc, mouseX, mouseY);
		
		// social buttons
		mc.getTextureManager().bindTexture(buttons);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		for(int i = 0; i < 6; i++)
		{
			x = i < 3 ? width - 26 - i * 24 : 10 + (5 - i) * 24;
			y = height - 34;
			h = 16;
			w = 16;
			fw = 43;
			fh = 256;
			u = 256 * 5 / 6 - i * 256 / 6;
			v = 0;
			worldRenderer.startDrawingQuads();
			worldRenderer.addVertexWithUV(x + 0, y + h, 0,
				(float)(u + 0) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			worldRenderer.addVertexWithUV(x + w, y + h, 0,
				(float)(u + fw) * 0.00390625F, (float)(v + fh) * 0.00390625F);
			worldRenderer.addVertexWithUV(x + w, y + 0, 0,
				(float)(u + fw) * 0.00390625F, (float)(v + 0) * 0.00390625F);
			worldRenderer.addVertexWithUV(x + 0, y + 0, 0,
				(float)(u + 0) * 0.00390625F, (float)(v + 0) * 0.00390625F);
			tessellator.draw();
		}
		
		// news
		if(!newsTicker.isEmpty())
			drawString(fontRendererObj, newsTicker,
				-(int)(Minecraft.getSystemTime() / 50 % newsWidth),
				height - 10, -1);
		
		// tooltips
		for(int i = 0; i < buttonList.size(); i++)
		{
			GuiButton button = (GuiButton)buttonList.get(i);
			if(button.isMouseOver())
			{
				ArrayList<String> tooltip = new ArrayList<>();
				switch(button.id)
				{
					case 20:
						tooltip.add("Wurst on YouTube");
						break;
					case 21:
						tooltip.add("Wurst on Twitter");
						break;
					case 22:
						tooltip.add("Wurst on Google+");
						break;
					case 23:
						tooltip.add("Wurst on GitHub");
						break;
					case 24:
						tooltip.add("Wurst Feedback");
						break;
					case 25:
						tooltip.add("Wurst Fan Shop");
						break;
				}
				drawHoveringText(tooltip, mouseX, mouseY);
				break;
			}
		}
		
		if(!WurstClient.INSTANCE.startupMessageDisabled)
		{
			if(WurstClient.INSTANCE.updater.isOutdated())
			{
				// updater
				WurstClient.INSTANCE.analytics.trackEvent(
					"updater",
					"update to v"
						+ WurstClient.INSTANCE.updater.getLatestVersion(),
					"from " + WurstClient.INSTANCE.updater.getCurrentVersion());
				WurstClient.INSTANCE.updater.update();
			}else if(Calendar.getInstance().get(Calendar.MONTH) == Calendar.DECEMBER
				&& Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 24)
			{
				// christmas
				mc.displayGuiScreen(new GuiYesNo(this, "Dude, it's Christmas!",
					"What do you want here?", "I want a Christmas song",
					"I want to grief", 1));
				WurstClient.INSTANCE.analytics.trackPageView(
					"/easter-eggs/christmas", "Dude, it's Christmas!");
			}else
				// emergency message
				try
				{
					HttpsURLConnection connection =
						(HttpsURLConnection)new URL(
							"https://www.wurst-client.tk/api/v1/messages.json")
							.openConnection();
					connection.connect();
					
					JsonObject json =
						new JsonParser().parse(
							new InputStreamReader(connection.getInputStream(),
								"UTF-8")).getAsJsonObject();
					
					if(json.get(WurstClient.VERSION) != null)
					{
						System.out.println("Emergency message found!");
						mc.displayGuiScreen(new GuiMessage(json.get(
							WurstClient.VERSION).getAsJsonObject()));
					}
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			
			WurstClient.INSTANCE.startupMessageDisabled = true;
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
		throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		int linkWidth = fontRendererObj.getStringWidth("§nwww.Wurst-Client.tk");
		
		if(mouseButton == 0 && mouseY >= height - 26 && mouseY < height - 16
			&& mouseX > width / 2 - linkWidth / 2
			&& mouseX < width / 2 + linkWidth / 2)
		{
			MiscUtils.openLink("https://www.Wurst-Client.tk/");
			WurstClient.INSTANCE.analytics.trackPageView(
				"/wurst-client-dot-tk", "www.Wurst-Client.tk");
		}
		
		if(news != null && mouseButton == 0 && mouseY >= height - 10)
		{
			MiscUtils.openLink("https://www.wurst-client.tk/news");
			WurstClient.INSTANCE.analytics.trackPageView("/news", "Wurst News");
		}
	}
}
