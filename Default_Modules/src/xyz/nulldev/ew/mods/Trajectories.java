package xyz.nulldev.ew.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import tk.wurst_client.WurstClient;
import tk.wurst_client.api.Module;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod;
import xyz.nulldev.ew.GLHelper;

/**
 * Project: EvenWurse
 * Created: 23/12/15
 * Author: nulldev
 */
/**
 * Trajectories mod based off of Colony hacked client's trajectories
 */
//TODO PREDICT OTHER PLAYER'S ARROWS
@Module.ModuleInfo(version = 1.01f, minVersion = 131)
@Mod.Info(name = "Trajectories", description = "Predicts the path of arrows and other throwable stuff.", category = Mod.Category.RENDER)
public class Trajectories extends Mod implements UpdateListener, RenderListener {

    private double x, y, z;
    private double motionX, motionY, motionZ;
    private double r, g, b;

    @Override
    public void onEnable() {
        super.onEnable();
        WurstClient.INSTANCE.events.add(UpdateListener.class, this);
        WurstClient.INSTANCE.events.add(RenderListener.class, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
        WurstClient.INSTANCE.events.remove(RenderListener.class, this);
    }

    public void enableDefaults( ) {
//        GL11.glDisable( GL11.GL_LIGHTING );
        GL11.glEnable( GL11.GL_LINE_SMOOTH );
        GL11.glBlendFunc( 770, 771 );
        GL11.glEnable( 3042 );
        GL11.glDisable( 3553 );
        GL11.glDisable( 2929 );
        GL11.glEnable( GL13.GL_MULTISAMPLE );
        GL11.glDepthMask( false );
    }

    public void disableDefaults( ) {
        GL11.glDisable( 3042 );
        GL11.glEnable( 3553 );
        GL11.glEnable( 2929 );
        GL11.glDisable( GL13.GL_MULTISAMPLE );
        GL11.glDepthMask( true );
        GL11.glDisable( GL11.GL_LINE_SMOOTH );
//        GL11.glEnable( GL11.GL_LIGHTING );
    }

    private double getGravity( Item item ) {
        return item instanceof ItemBow ? 0.05D : 0.03D;
    }

    private boolean isThrowable( Item item ) {
        return ( item instanceof ItemBow ) || ( item instanceof ItemSnowball)
                || ( item instanceof ItemEgg) || ( item instanceof ItemEnderPearl);
    }

    @Override
    public void onUpdate() {
        if ( this.isActive( ) ) {
            r = Minecraft.getMinecraft().thePlayer.getDistance( x, y, z ) / 100;
            g = 1.0;
            b = 0.0;
        }
    }

    @Override
    public void onRender() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if ( ( player.getCurrentEquippedItem( ) != null ) && isActive( ) ) {
            if ( this.isThrowable( Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem( ).getItem( ) ) ) {
                this.x = ( player.lastTickPosX
                        + ( ( player.posX - player.lastTickPosX )
                        * Minecraft.getMinecraft().timer.renderPartialTicks ) )
                        - ( MathHelper.cos( (float) Math.toRadians( player.rotationYaw ) ) * 0.16F );
                this.y = ( player.lastTickPosY
                        + ( ( player.posY - player.lastTickPosY ) * Minecraft.getMinecraft().timer.renderPartialTicks )
                        + player.getEyeHeight( ) ) - 0.100149011612D;
                this.z = ( player.lastTickPosZ
                        + ( ( player.posZ - player.lastTickPosZ )
                        * Minecraft.getMinecraft().timer.renderPartialTicks ) )
                        - ( MathHelper.sin( (float) Math.toRadians( player.rotationYaw ) ) * 0.16F );
                float con = 1.0F;
                if ( !( player.getCurrentEquippedItem( ).getItem( ) instanceof ItemBow ) ) {
                    con = 0.4F;
                }

                this.motionX = ( -MathHelper.sin( (float) Math.toRadians( player.rotationYaw ) )
                        * MathHelper.cos( (float) Math.toRadians( player.rotationPitch ) ) * con );
                this.motionZ = ( MathHelper.cos( (float) Math.toRadians( player.rotationYaw ) )
                        * MathHelper.cos( (float) Math.toRadians( player.rotationPitch ) ) * con );
                this.motionY = ( -MathHelper.sin( (float) Math.toRadians( player.rotationPitch ) ) * con );
                double ssum = Math.sqrt( ( this.motionX * this.motionX )
                        + ( this.motionY * this.motionY ) + ( this.motionZ
                        * this.motionZ ) );

                this.motionX /= ssum;
                this.motionY /= ssum;
                this.motionZ /= ssum;

                if ( player.getCurrentEquippedItem( ).getItem( ) instanceof ItemBow ) {
                    float pow = ( 72000 - player.getItemInUseCount( ) ) / 20.0F;
                    pow = ( ( pow * pow ) + ( pow * 2.0F ) ) / 3.0F;

                    if ( pow > 1.0F ) {
                        pow = 1.0F;
                    }

                    if ( pow <= 0.1F ) {
                        pow = 1.0F;
                    }

                    pow *= 2.0F;
                    pow *= 1.5F;
                    this.motionX *= pow;
                    this.motionY *= pow;
                    this.motionZ *= pow;
                } else {
                    this.motionX *= 1.5D;
                    this.motionY *= 1.5D;
                    this.motionZ *= 1.5D;
                }

                GL11.glPushMatrix( );
                enableDefaults( );
                GL11.glLineWidth( 1.8F );
                GL11.glColor3d( r, g, b );
                GL11.glBegin( GL11.GL_LINE_STRIP );
                boolean hasHitBlock = false;
                double gravity = this.getGravity( player.getCurrentEquippedItem( ).getItem( ) );

                //TODO Better infinite loop checking
                for (int i = 0; i < 100000 && !hasHitBlock; i++) {
                    double rx = ( this.x ) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                    double ry = ( this.y ) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                    double rz = ( this.z ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                    GL11.glVertex3d( rx, ry, rz );

                    this.x += this.motionX;
                    this.y += this.motionY;
                    this.z += this.motionZ;
                    this.motionX *= 0.99D;
                    this.motionY *= 0.99D;
                    this.motionZ *= 0.99D;
                    this.motionY -= gravity;

                    hasHitBlock = Minecraft.getMinecraft().theWorld.rayTraceBlocks( new Vec3( player.posX, player.posY + player.getEyeHeight( ), player.posZ ), new Vec3( this.x, this.y, this.z ) ) != null;
                }

                GL11.glEnd( );

                new AxisAlignedBB( x - 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosX,
                        y - 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosY,
                        z - 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosZ,
                        ( x - 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosX ) + 1,
                        ( y - 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosY ) + 1,
                        ( z - 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosZ ) + 1 );

                GL11.glTranslated( x - Minecraft.getMinecraft().getRenderManager().renderPosX,
                        y - Minecraft.getMinecraft().getRenderManager().renderPosY,
                        z - Minecraft.getMinecraft().getRenderManager().renderPosZ );
                GL11.glRotatef( Minecraft.getMinecraft().thePlayer.rotationYaw,
                        0.0F,
                        (float) ( y - Minecraft.getMinecraft().getRenderManager().renderPosY ), 0.0F );
                GL11.glTranslated( -( x - Minecraft.getMinecraft().getRenderManager().renderPosX ), -( y - Minecraft.getMinecraft().getRenderManager().renderPosY ), -( z - Minecraft.getMinecraft().getRenderManager().renderPosZ ) );
                GLHelper.drawESP( x - 0.35 - Minecraft.getMinecraft().getRenderManager().renderPosX, y - 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosY, z - 0.5 - Minecraft.getMinecraft().getRenderManager().renderPosZ, r, b, g );
                disableDefaults( );
                GL11.glPopMatrix( );
            }
        }
    }
}
