package tk.wurst_client.module.modules;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import tk.wurst_client.Client;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class TacoCMD extends Module
{
	public TacoCMD()
	{
		super
		(
			"Taco",
			"",
			0,
			Category.HIDDEN
		);
	}
	
    private static final ResourceLocation tacoTexture1 = new ResourceLocation("textures/gui/wurst/dancingtaco1.png");
    private static final ResourceLocation tacoTexture2 = new ResourceLocation("textures/gui/wurst/dancingtaco2.png");
    private static final ResourceLocation tacoTexture3 = new ResourceLocation("textures/gui/wurst/dancingtaco3.png");
    private static final ResourceLocation tacoTexture4 = new ResourceLocation("textures/gui/wurst/dancingtaco4.png");
    private static final ResourceLocation[] tacoTextures = {tacoTexture1, tacoTexture2, tacoTexture3, tacoTexture4};
	private int i = 0;
	
	public void onRenderGUI()
	{
		if(!this.getToggled())
			return;
		glEnable(GL_BLEND);
		glDisable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		Tessellator var3 = Tessellator.getInstance();
		WorldRenderer var4 = var3.getWorldRenderer();
        ScaledResolution screenRes = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Client.Wurst.moduleManager.getModuleFromClass(TacoCMD.class).updateMS();
        if(Client.Wurst.moduleManager.getModuleFromClass(TacoCMD.class).hasTimePassedM(400))
        {
        	i++;
        	Client.Wurst.moduleManager.getModuleFromClass(TacoCMD.class).updateLastMS();
        	if(i == 4)
        		i = 0;
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(tacoTextures[i]);
        double x = screenRes.getScaledWidth() / 2 - 32 + 76;
        double y = screenRes.getScaledHeight() - 32 - 19;
        double h = 32;
        double w = 64;
        double fw = 256;
        double fh = 256;
        double u = 0;
        double v = 0;
        var4.startDrawingQuads();
        var4.addVertexWithUV((double)x + 0, (double)y + h, (double)0, (double)((float)(u + 0) * 0.00390625F), (double)((float)(v + fh) * 0.00390625F));
        var4.addVertexWithUV((double)x + w, (double)y + h, (double)0, (double)((float)(u + fw) * 0.00390625F), (double)((float)(v + fh) * 0.00390625F));
        var4.addVertexWithUV((double)x + w, (double)y + 0, (double)0, (double)((float)(u + fw) * 0.00390625F), (double)((float)(v + 0) * 0.00390625F));
        var4.addVertexWithUV((double)x + 0, (double)y + 0, (double)0, (double)((float)(u + 0) * 0.00390625F), (double)((float)(v + 0) * 0.00390625F));
        var3.draw();
		glEnable(GL_CULL_FACE);
		glDisable(GL_BLEND);
	}
}
