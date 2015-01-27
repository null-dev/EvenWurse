package tk.wurst_client.module.modules;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class XRay extends Module
{
	public XRay()
	{
		super
		(
			"X-Ray",
			"Allows you to see ores through walls.",
			Keyboard.KEY_X,
			Category.RENDER
		);
	}
	
	public static ArrayList<Block> xrayBlocks = new ArrayList<Block>();
	
	public String getRenderName()
	{
		return "X-Wurst";
	}
	
	public void onEnable()
	{
		Block.isXRayEnabled = true;
		Minecraft.getMinecraft().renderGlobal.loadRenderers();
	}
	
	public void onDisable()
	{
		Block.isXRayEnabled = false;
		Minecraft.getMinecraft().renderGlobal.loadRenderers();
	}
}
