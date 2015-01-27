package tk.wurst_client.module.modules;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.RenderUtils;

public class ProphuntESP extends Module
{
	public ProphuntESP()
	{
		super
		(
			"ProphuntESP",
			"Allows you to see fake blocks in Prophunt.",
			0,
			Category.RENDER
		);
	}
	
	public void onRender()
	{
		if(!this.getToggled())
			return;
		for(Object entity : Minecraft.getMinecraft().theWorld.loadedEntityList)
		{
			if
			(
				entity instanceof EntityLiving
				&& ((Entity)entity).isInvisible()
			)
			{
				double x = ((Entity)entity).posX;
				double y = ((Entity)entity).posY;
				double z = ((Entity)entity).posZ;
				Color color;
				if(Minecraft.getMinecraft().thePlayer.getDistanceToEntity(((Entity)entity)) >= 0.5)
					color = new Color(1F, 0F, 0F, 0.5F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000.0F * (float)Math.PI * 1.0F) * 0.3F));
				else
					color = new Color(0, 0, 0, 0);
				RenderUtils.box(x - 0.5, y - 0.1, z - 0.5, x + 0.5, y + 0.9, z + 0.5, color);
			}
		}
	}
}
