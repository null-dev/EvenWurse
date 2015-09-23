/*
 * Copyright © 2014 - 2015 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Rectangle;
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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

import tk.wurst_client.WurstClient;
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
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		for(int i = 3; i <= 5; i++)
			((GuiButton)buttonList.get(i)).yPosition =
				Math.min(((GuiButton)buttonList.get(i)).yPosition, height - 56);
		
		// news
		newsTicker = "";
		try
		{
			for(int i = 0; i < news.size(); i++)
				newsTicker +=
					news.get(i).getChildrenByName("title").get(0).getContent()
						+ "§e+++§r";
		}catch(ConcurrentModificationException | NullPointerException e)
		{	
			
		}
		newsWidth = fontRendererObj.getStringWidth(newsTicker);
		while(fontRendererObj.getStringWidth(newsTicker) < Math.max(width * 2,
			newsWidth * 2) && !newsTicker.isEmpty())
			newsTicker += newsTicker;
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
		
		// prevents Clean Up from removing drawNews()
		if(!"".isEmpty())
			drawNews(mouseX, mouseY, partialTicks);
		
		// news
		if(!newsTicker.isEmpty())
			drawString(fontRendererObj, newsTicker,
				-(int)(Minecraft.getSystemTime() / 50 % newsWidth),
				height - 10, -1);
		
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
	
	private void drawNews(int mouseX, int mouseY, float partialTicks)
	{
		// redesigned wurst news - unfinished!
		
		// GL settings
		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glShadeModel(GL_SMOOTH);
		
		// sizes
		Rectangle area =
			new Rectangle(width / 2 + 116, 104, width / 2 - 116 - 8,
				height - 104 - 16);
		int titleBarHeight = 14;
		
		// title bar background
		glColor4f(0.03125f, 0.03125f, 0.03125f, 0.5f);
		glBegin(GL_QUADS);
		{
			glVertex2d(area.getMinX(), area.getMinY());
			glVertex2d(area.getMaxX(), area.getMinY());
			glVertex2d(area.getMaxX(), area.getMinY() + titleBarHeight);
			glVertex2d(area.getMinX(), area.getMinY() + titleBarHeight);
		}
		glEnd();
		
		// frame background
		RenderUtil.setColor(new Color(64, 64, 64, 128));
		glBegin(GL_QUADS);
		{
			glVertex2d(area.getMinX(), area.getMinY() + titleBarHeight);
			glVertex2d(area.getMaxX(), area.getMinY() + titleBarHeight);
			glVertex2d(area.getMaxX(), area.getMaxY());
			glVertex2d(area.getMinX(), area.getMaxY());
		}
		glEnd();
		
		// shadows
		RenderUtil.boxShadow(area.getMinX(), area.getMinY(), area.getMaxX(),
			area.getMaxY());
		RenderUtil.downShadow(area.getMinX(), area.getMinY() + titleBarHeight,
			area.getMaxX(), area.getMinY() + titleBarHeight + 1);
		
		// title
		drawString(fontRendererObj, "Wurst News", area.x + 3, area.y + 3,
			0xffffff);
		
		int offsetX = area.x + 2;
		int offsetY = area.y + titleBarHeight + 3;
		ArrayList<String> lines = new ArrayList<>();
		for(int i = 0; i < news.size(); i++)
		{
			ArrayList<String> title =
				lineWrap(news.get(i).getChildrenByName("title").get(0)
					.getContent(), (int)((area.width - 8) / 0.75));
			for(int line = 0; line < title.size(); line++)
				title.set(line, EnumChatFormatting.UNDERLINE + title.get(line));
			lines.addAll(title);
			lines.add("");
		}
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(offsetX, offsetY, 0f);
		GlStateManager.scale(0.75, 0.75, 0.75);
		for(int line = 0; line < lines.size(); line++)
			drawString(fontRendererObj,
				EnumChatFormatting.UNDERLINE + lines.get(line), 0, line * 10,
				0xffffff);
		GlStateManager.popMatrix();
		
		// GL resets
		glEnable(GL_CULL_FACE);
		glDisable(GL_BLEND);
	}
	
	private ArrayList<String> lineWrap(String string, int width)
	{
		ArrayList<String> lines = new ArrayList<>();
		String[] words = string.split(" ");
		
		String line = "";
		String lastLine = "";
		for(String word : words)
		{
			line += lastLine.isEmpty() ? word : " " + word;
			if(fontRendererObj.getStringWidth(line) > width)
			{
				if(lastLine.isEmpty())
				{
					lines.add(line);
					lastLine = "";
				}else
				{
					lines.add(lastLine);
					lastLine = word;
				}
				line = lastLine;
			}else
				lastLine = line;
		}
		lines.add(line);
		return lines;
	}
}
