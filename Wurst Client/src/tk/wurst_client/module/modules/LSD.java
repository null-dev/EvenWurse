package tk.wurst_client.module.modules;

import java.awt.Color;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class LSD extends Module
{
	public LSD()
	{
		super
		(
			"LSD",
			"Thousands of colors!",
			0,
			Category.FUN
		);
	}
	
	private static float speed = 2;
	private static long currentMS = 0L;
	private static long lastMS = -1L;
	private static Color color = Color.WHITE;
	
	public void onEnable()
	{
		Minecraft.getMinecraft().entityRenderer.activateLSD();
	}
	
	public void onToggle()
	{
		if(!OpenGlHelper.shadersSupported)
			Minecraft.getMinecraft().renderGlobal.loadRenderers();
	}
	
	public void onUpdate()
	{
		if(!this.getToggled())
			return;
		if(!OpenGlHelper.shadersSupported)
			Minecraft.getMinecraft().thePlayer.addPotionEffect(new PotionEffect(Potion.confusion.getId(), 10801220));
		Minecraft.getMinecraft().gameSettings.smoothCamera = true;
	}
	
	public void onDisable()
	{
		Minecraft.getMinecraft().thePlayer.removePotionEffect(Potion.confusion.getId());
		Minecraft.getMinecraft().gameSettings.smoothCamera = false;
		if(Minecraft.getMinecraft().entityRenderer.theShaderGroup != null)
		{
			Minecraft.getMinecraft().entityRenderer.theShaderGroup.deleteShaderGroup();
			Minecraft.getMinecraft().entityRenderer.theShaderGroup = null;
		}
		Tessellator.shouldRenderLSD = false;
	}
	
	public static Color randomColor()
	{
		currentMS = System.currentTimeMillis();
		if(currentMS >= lastMS + (long)(1000 / speed))
		{
			color = Color.WHITE;
			lastMS = System.currentTimeMillis();
		}
		while(color == Color.WHITE)
		{
			color = new Color
			(
				new Random().nextInt(256),
				new Random().nextInt(256),
				new Random().nextInt(256),
				new Random().nextInt(256)
			);
		}
		return color;
	}
}
