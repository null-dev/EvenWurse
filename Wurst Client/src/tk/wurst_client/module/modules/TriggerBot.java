package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import tk.wurst_client.Client;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.EntityUtils;

public class TriggerBot extends Module
{
	public TriggerBot()
	{
		super
		(
			"TriggerBot",
			"Automatically attacks the entity you're looking at.",
			0,
			Category.COMBAT
		);
	}
	
	public void onEnable()
	{
		if(Client.Wurst.moduleManager.getModuleFromClass(Killaura.class).getToggled())
			Client.Wurst.moduleManager.getModuleFromClass(Killaura.class).setToggled(false);
		if(Client.Wurst.moduleManager.getModuleFromClass(KillauraLegit.class).getToggled())
			Client.Wurst.moduleManager.getModuleFromClass(KillauraLegit.class).setToggled(false);
		if(Client.Wurst.moduleManager.getModuleFromClass(MultiAura.class).getToggled())
			Client.Wurst.moduleManager.getModuleFromClass(MultiAura.class).setToggled(false);
	}
	
	public void onUpdate()
	{
		if
		(
			this.getToggled()
			&& Minecraft.getMinecraft().objectMouseOver != null
			&& Minecraft.getMinecraft().objectMouseOver.entityHit != null
		)
		{
			updateMS();
			if(hasTimePassedS(Killaura.realSpeed))
			{
				EntityLivingBase en = (EntityLivingBase)Minecraft.getMinecraft().objectMouseOver.entityHit;
				if
				(
					Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) <= Killaura.realRange
					&& EntityUtils.isCorrectEntity(en, true)
				)
				{
					Criticals.doCritical();
					Minecraft.getMinecraft().thePlayer.swingItem();
					Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().thePlayer, en);
					updateLastMS();
				}
			}
		}
	}
}
