package tk.wurst_client.module.modules;

import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntityFishHook;

public class AutoFish extends Module
{
	
	public AutoFish()
	{
		super
		(
			"AutoFish",
			"Automatically catches fish.",
			0,
			Category.MISC
		);
	}
	
	private boolean catching = false;
	
	@Override
	public void onUpdate()
	{
		if
		(
			this.getToggled()
			&& Minecraft.getMinecraft().thePlayer.fishEntity != null
			&& isHooked(Minecraft.getMinecraft().thePlayer.fishEntity)
			&& !catching
		)
		{
			catching = true;
			Minecraft.getMinecraft().rightClickMouse();
			new Thread("AutoFish")
			{
				public void run()
				{
					try
					{
						Thread.sleep(1000);
					}catch(InterruptedException e)
					{
						e.printStackTrace();
					}
					Minecraft.getMinecraft().rightClickMouse();
					catching = false;
				}
			}.start();
		}
	}
	
	private boolean isHooked(EntityFishHook hook)
	{
		return (hook.motionX == 0.0D) && (hook.motionZ == 0.0D) && (hook.motionY != 0.0D);
	}
}