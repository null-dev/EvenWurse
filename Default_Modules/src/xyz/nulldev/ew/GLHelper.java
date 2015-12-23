package xyz.nulldev.ew;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

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
