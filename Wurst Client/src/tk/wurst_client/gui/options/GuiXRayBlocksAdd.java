package tk.wurst_client.gui.options;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import tk.wurst_client.Client;
import tk.wurst_client.module.modules.XRay;

public class GuiXRayBlocksAdd extends GuiScreen
{
    private GuiScreen prevMenu;
    private GuiTextField nameBox;
    private static final String __OBFID = "CL_00000709";

    public GuiXRayBlocksAdd(GuiScreen par1GuiScreen)
    {
        this.prevMenu = par1GuiScreen;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.nameBox.updateCursorCounter();
        Block block = Block.getBlockFromName(this.nameBox.getText());
        ((GuiButton)this.buttonList.get(0)).enabled = this.nameBox.getText().trim().length() > 0 && block != null;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, "Add"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 144 + 12, "Cancel"));
        this.nameBox = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 80, 200, 20);
        this.nameBox.setFocused(true);
    }

    /**
     * "Called when the screen is unloaded. Used to disable keyboard repeat events."
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton clickedButton)
    {
        if (clickedButton.enabled)
        {
            if (clickedButton.id == 0)
            {//Add
                Block block = Block.getBlockFromName(this.nameBox.getText());
                XRay.xrayBlocks.add(block);
                GuiXRayBlocksManager.blockList.sortBlocks();
                Client.Wurst.fileManager.saveXRayBlocks();
                this.mc.displayGuiScreen(this.prevMenu);
            }else if (clickedButton.id == 1)
            {//Cancel
                this.mc.displayGuiScreen(this.prevMenu);
            }
        }
    }

	/**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        this.nameBox.textboxKeyTyped(par1, par2);

        if (par2 == 28 || par2 == 156)
        {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
    }

    /**
     * Called when the mouse is clicked.
     * @throws IOException 
     */
    protected void mouseClicked(int par1, int par2, int par3) throws IOException
    {
        super.mouseClicked(par1, par2, par3);
        this.nameBox.mouseClicked(par1, par2, par3);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.drawBackground(0);
        Block block = Block.getBlockFromName(this.nameBox.getText());
		int x = this.width / 2 - 9;
		int y = this.height / 2 - 32;
		ItemStack itemStack = new ItemStack(Item.getItemFromBlock(block));
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		if(itemStack.getItem() != null)
		{
			try
			{
				Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}else
			this.mc.fontRendererObj.drawString("?", x + 6, y + 5, 10526880);
		Minecraft.getMinecraft().getRenderItem().func_175030_a(Minecraft.getMinecraft().fontRendererObj, itemStack, x + 4, y + 4);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
		try
		{
			this.drawCenteredString(this.fontRendererObj, "Name: " + (itemStack.getItem() == null ? block.getLocalizedName() : itemStack.getDisplayName()), this.width / 2, y + 24, 10526880);
			this.drawCenteredString(this.fontRendererObj, "ID: " + Block.getIdFromBlock(block), this.width / 2, y + 36, 10526880);
			this.drawCenteredString(this.fontRendererObj, "Block exists: " + (block != null), this.width / 2, y + 48, 10526880);
		}catch(Exception e)
		{
			this.mc.fontRendererObj.drawString("?", x + 6, y + 5, 10526880);
			this.drawCenteredString(this.fontRendererObj, "Name: unknown", this.width / 2, y + 24, 10526880);
			this.drawCenteredString(this.fontRendererObj, "ID: unknown", this.width / 2, y + 36, 10526880);
			this.drawCenteredString(this.fontRendererObj, "Block exists: " + (block != null), this.width / 2, y + 48, 10526880);
		}
        this.drawCenteredString(this.fontRendererObj, "Add a Block", this.width / 2, 20, 16777215);
        this.drawString(this.fontRendererObj, "Name or ID", this.width / 2 - 100, 67, 10526880);
        this.nameBox.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }
}
