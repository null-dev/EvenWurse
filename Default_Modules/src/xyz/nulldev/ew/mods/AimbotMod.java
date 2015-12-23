package xyz.nulldev.ew.mods;

/**
 * Project: EvenWurse
 * Created: 23/12/15
 * Author: nulldev
 */

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import tk.wurst_client.WurstClient;
import tk.wurst_client.api.Module;
import tk.wurst_client.api.ModuleConfiguration;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod;
import tk.wurst_client.utils.EntityUtils;

/**
 * Aimbot mod based off of Colony hacked client's aimbot
 */
@Module.ModuleInfo(version = 1.00f, minVersion = 130, usesConfig = true)
@Mod.Info(name = "Aimbot", description = "Automatically aims to the nearest entity.", category = Mod.Category.COMBAT)
public class AimbotMod extends Mod implements UpdateListener {

    @Override
    public void onEnable() {
        super.onEnable();
        WurstClient.INSTANCE.events.add(UpdateListener.class, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
    }

    public void faceEntityBow(EntityLivingBase var0, float var1, float var2 )
    {
            double var3 = 0.0D;
            double var5 = var0.posX - Minecraft.getMinecraft().thePlayer.posX;
            double var7 = var0.posZ - Minecraft.getMinecraft().thePlayer.posZ;
            double var9 = ( var0.posY - Minecraft.getMinecraft().thePlayer.posY ) + 1.2D;

            if ( ( var7 > 0.0D ) && ( var5 > 0.0D ) )
            {
                var3 = Math.toDegrees( -Math.atan( var5 / var7 ) );
            }
            else if ( ( var7 > 0.0D ) && ( var5 < 0.0D ) )
            {
                var3 = Math.toDegrees( -Math.atan( var5 / var7 ) );
            }
            else if ( ( var7 < 0.0D ) && ( var5 > 0.0D ) )
            {
                var3 = -90.0D + Math.toDegrees( Math.atan( var7 / var5 ) );
            }
            else if ( ( var7 < 0.0D ) && ( var5 < 0.0D ) )
            {
                var3 = 90.0D + Math.toDegrees( Math.atan( var7 / var5 ) );
            }

            float var11 = (float) Math.sqrt( ( var7 * var7 ) + ( var5 * var5 ) );
            float var12 = (float) ( -Math.toDegrees( Math.atan( var9 / var11 ) ) );
        Minecraft.getMinecraft().thePlayer.rotationPitch = var12 - 3.0F;
        Minecraft.getMinecraft().thePlayer.rotationYaw = (float) var3;
    }

    public void aimWithNoBow(EntityLivingBase e)
    {
        EntityUtils.faceEntityClient(e);
    }

    @Override
    public void onUpdate() {
        EntityLivingBase en = EntityUtils.getClosestEntity(true);
        if(en == null) return;
        if(Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) <= ModuleConfiguration.forModule(this).getFloat("Range", 4.25F))
        {
            int curItem = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
            ItemStack item =
                    Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(curItem);
            if(item != null && item.getItem() instanceof ItemBow) {
                faceEntityBow(en, 100, 100);
            } else {
                aimWithNoBow(en);
            }
        }
    }
}
