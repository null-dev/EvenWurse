package xyz.nulldev.ew;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import xyz.nulldev.ew.waypoints.Waypoint;

import java.awt.*;

/**
 * Project: EvenWurse
 * Created: 23/12/15
 * Author: nulldev
 */
public class GLHelper {

    public static void drawBoundingBox( AxisAlignedBB axisalignedbb ) {
        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getWorldRenderer().startDrawingQuads( ); // starts x
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ );
        tessellator.draw( );
        tessellator.getWorldRenderer().startDrawingQuads( );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ );
        tessellator.draw( ); // ends x
        tessellator.getWorldRenderer().startDrawingQuads( ); // starts y
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ );
        tessellator.draw( );
        tessellator.getWorldRenderer().startDrawingQuads( );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ );
        tessellator.draw( ); // ends y
        tessellator.getWorldRenderer().startDrawingQuads( ); // starts z
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ );
        tessellator.draw( );
        tessellator.getWorldRenderer().startDrawingQuads( );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ );
        tessellator.getWorldRenderer().addVertex( axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ );
        tessellator.draw( ); // ends z
    }



    /**
     * Draw a lightning bolt at a pair of specified coordinates and color
     * @param x The x coordinate
     * @param z The z coordinate
     */
    public static void drawLightning(double x, double z, float r, float g, float b) {
        Tessellator var10 = Tessellator.getInstance();
        WorldRenderer var11 = var10.getWorldRenderer();
        GlStateManager.func_179090_x();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 1);
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
        double[] var12 = new double[8];
        double[] var13 = new double[8];
        double var14 = 0.0D;
        double var16 = 0.0D;

        for (int var19 = 7; var19 >= 0; --var19)
        {
            var12[var19] = var14;
            var13[var19] = var16;
            var14 += (double)(10 - 5);
            var16 += (double)(10 - 5);
        }

        for (int var46 = 0; var46 < 4; ++var46)
        {
            int var21 = 7;
            int var22 = 0;

            double var23 = var12[var21];
            double var25 = var13[var21];

            int var27 = var22;
            var11.startDrawing(5);
//                float var32 = 0.5F;
//                var11.setColorRGBA_F(0.9F * var32, 0.9F * var32, 1.0F * var32, 0.3F);
            var11.setColorRGBA_F(r, g, b, 0.3F);
            double var35 = 0.1D + (double)var46 * 0.2D;

            var35 *= (double)(var27 - 1) * 0.1D + 1.0D;

            for (int var37 = 0; var37 < 5; ++var37)
            {

                double var42 = x + 0.5D - var35;
                double var44 = z + 0.5D - var35;

                if (var37 == 1 || var37 == 2)
                {
                    var42 += var35 * 2.0D;
                }

                if (var37 == 2 || var37 == 3)
                {
                    var44 += var35 * 2.0D;
                }

                var11.addVertex(var42 + var23, -1000 + (double)(var27 * 10000), var44 + var25);
                var11.addVertex(var42 + var23, -1000 + (double)((var27 + 1) * 10000), var44 + var25);
            }

            var10.draw();
        }

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.func_179098_w();
    }

    public static void drawTag( String s, double d, double d1, double d2 , float scale) {
//        float f = 5;

//        Minecraft.getMinecraft().entityRenderer.disableLightmap( 0D );
        d += 0.5D;
        d2 += 0.5D;
        FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
        int color = 0xFFFFFFFF;

//        float scale = f / sc;
        RenderManager renderManager1 = Minecraft.getMinecraft().getRenderManager();
        GL11.glPushMatrix( );
        GL11.glTranslatef( (float) d, (float) d1 + 1.5F, (float) d2 - 0.5F );
        GL11.glNormal3f( 0.0F, 1.0F, 0.0F );
        GL11.glRotatef( -renderManager1.playerViewY, 0.0F, 1.0F, 0.0F );
        GL11.glRotatef( renderManager1.playerViewX, 1.0F, 0.0F, 0.0F );

        GL11.glScalef( -scale, -scale, scale );

        GL11.glDisable( GL11.GL_LIGHTING );
        GL11.glDisable( GL11.GL_DEPTH_TEST );
        GL11.glEnable( GL11.GL_BLEND );
        GL11.glBlendFunc( 770, 771 );
        byte byte0 = 0;
        fontrenderer.drawStringWithShadow( s, -fontrenderer.getStringWidth( s ) / 2, byte0, color );
        fontrenderer.drawStringWithShadow( s, -fontrenderer.getStringWidth( s ) / 2, byte0, color );
        GL11.glDisable( GL11.GL_BLEND );
        GL11.glEnable( GL11.GL_DEPTH_TEST );
        GL11.glEnable( GL11.GL_LIGHTING );
        GlStateManager.color(1, 1, 1, 1);
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
        GL11.glPopMatrix( );
//        Minecraft.getMinecraft().entityRenderer.enableLightmap( 0D );
    }

    public static void drawWayPointTracer( Waypoint w )
    {
        try
        {
            GL11.glPushMatrix( );
            GL11.glEnable( GL11.GL_LINE_SMOOTH );
            GL11.glDisable( GL11.GL_LIGHTING );
            GL11.glDisable( GL11.GL_DEPTH_TEST );
            GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
            GL11.glDisable( GL11.GL_TEXTURE_2D );
            GL11.glDepthMask( false );
            GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
            GL11.glEnable( GL11.GL_BLEND );
            GL11.glLineWidth( 1.5F );

            GL11.glColor3d( w.red, w.green, w.blue );
            GL11.glBegin( GL11.GL_LINE_LOOP );
            GL11.glVertex3d( 0, 0, 0 );
            GL11.glVertex3d( w.dX + 0.5, w.dY + 0.5, w.dZ + 0.5 );
            GL11.glEnd( );

            GL11.glDisable( GL11.GL_BLEND );
            GL11.glDepthMask( true );
            GL11.glEnable( GL11.GL_TEXTURE_2D );
            GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
            GL11.glEnable( GL11.GL_DEPTH_TEST );
            GL11.glEnable( GL11.GL_LIGHTING );
            GL11.glDisable( GL11.GL_LINE_SMOOTH );
            GL11.glPopMatrix( );
        } catch ( Exception ignored) {

        }
    }

    public static void drawESP( double d, double d1, double d2, double r, double b, double g )
    {
        GL11.glPushMatrix( );
        GL11.glEnable( 3042 );
        GL11.glBlendFunc( 770, 771 );
        GL11.glLineWidth( 1.5F );
        GL11.glDisable( GL11.GL_LIGHTING );
        GL11.glDisable( GL11.GL_TEXTURE_2D );
        GL11.glEnable( GL11.GL_LINE_SMOOTH );
        GL11.glDisable( 2929 );
        GL11.glDepthMask( false );
        GL11.glColor4d( r, g, b, 0.1825F );
        drawBoundingBox( new AxisAlignedBB( d, d1, d2, d + 1.0, d1 + 1.0, d2 + 1.0 ) );
        GL11.glColor4d( r, g, b, 1.0F );
        RenderGlobal.drawOutlinedBoundingBox( new AxisAlignedBB( d, d1, d2, d + 1.0, d1 + 1.0, d2 + 1.0 ), new Color(1f,0.25f,0.25f).getRGB());
        GL11.glLineWidth( 2.0F );
        GL11.glDisable( GL11.GL_LINE_SMOOTH );
        GL11.glEnable( GL11.GL_TEXTURE_2D );
        GL11.glEnable( GL11.GL_LIGHTING );
        GL11.glEnable( 2929 );
        GL11.glDepthMask( true );
        GL11.glDisable( 3042 );
        GL11.glPopMatrix( );
    }
}
