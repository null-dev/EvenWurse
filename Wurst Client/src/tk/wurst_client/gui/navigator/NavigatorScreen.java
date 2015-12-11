package tk.wurst_client.gui.navigator;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Rectangle;
import java.io.IOException;

import net.minecraft.client.gui.GuiScreen;

import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.input.Mouse;

import tk.wurst_client.WurstClient;
import tk.wurst_client.font.Fonts;
import tk.wurst_client.mods.Mod;
import tk.wurst_client.mods.ModManager;

public class NavigatorScreen extends GuiScreen
{
	private int scroll = 0;
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException
	{
		super.mouseClicked(x, y, button);
	}
	
	@Override
	public void mouseReleased(int x, int y, int button)
	{
		super.mouseReleased(x, y, button);
	}
	
	@Override
	public void updateScreen()
	{
		scroll += Mouse.getDWheel() / 10;
		if(scroll > 0)
			scroll = 0;
		else
		{
			int maxScroll =
				-WurstClient.INSTANCE.mods.getAllMods().size() / 3 * 20
					+ height - 120;
			if(scroll < maxScroll)
				scroll = maxScroll;
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_CULL_FACE);
		glDisable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);
		
		int x = width / 2 - 50;
		RenderUtil.scissorBox(0, 59, width, height - 42);
		glEnable(GL_SCISSOR_TEST);
		for(int i = 0; i < WurstClient.INSTANCE.mods.getAllMods().size(); i++)
		{
			int y = 60 + (i / 3) * 20 + scroll;
			if(y < 40)
				continue;
			if(y > height - 40)
				break;
			int xi;
			switch(i % 3)
			{
				case 1:
					xi = x - 104;
					break;
				case 2:
					xi = x + 104;
					break;
				default:
					xi = x;
					break;
			}
			Rectangle area = new Rectangle(xi, y, 100, 16);
			if(area.contains(mouseX, mouseY))
				glColor4f(0.375F, 0.375F, 0.375F, 0.5F);
			else
				glColor4f(0.25F, 0.25F, 0.25F, 0.5F);
			glBegin(GL_QUADS);
			{
				glVertex2d(area.x, area.y);
				glVertex2d(area.x + area.width, area.y);
				glVertex2d(area.x + area.width, area.y + area.height);
				glVertex2d(area.x, area.y + area.height);
			}
			glEnd();
			RenderUtil.boxShadow(area.x, area.y, area.x + area.width, area.y
				+ area.height);
			glEnable(GL_TEXTURE_2D);
			try
			{
				String mod =
					((Mod)ModManager.class.getFields()[i]
						.get(WurstClient.INSTANCE.mods)).getName();
				Fonts.segoe15.drawString(mod, area.x
					+ (area.width - Fonts.segoe15.getStringWidth(mod)) / 2,
					area.y + 2, 0xffffff);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			glDisable(GL_TEXTURE_2D);
		}
		glDisable(GL_SCISSOR_TEST);
		
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
	}
}
