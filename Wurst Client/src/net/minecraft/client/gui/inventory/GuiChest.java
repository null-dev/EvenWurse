package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import tk.wurst_client.Client;
import tk.wurst_client.module.modules.YesCheat;

public class GuiChest extends GuiContainer
{
	private static final ResourceLocation field_147017_u = new ResourceLocation("textures/gui/container/generic_54.png");
	private IInventory upperChestInventory;
	private IInventory lowerChestInventory;
	
	/**
	 * window height is calculated with these values; the more rows, the higher
	 */
	private int inventoryRows;
	
	public GuiChest(IInventory p_i46315_1_, IInventory p_i46315_2_)
	{
		super(new ContainerChest(p_i46315_1_, p_i46315_2_, Minecraft.getMinecraft().thePlayer));
		upperChestInventory = p_i46315_1_;
		lowerChestInventory = p_i46315_2_;
		allowUserInput = false;
		short var3 = 222;
		int var4 = var3 - 108;
		inventoryRows = p_i46315_2_.getSizeInventory() / 9;
		ySize = var4 + inventoryRows * 18;
	}
	
	/*
	 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
	 * 
	 * This Source Code Form is subject to the terms of the Mozilla Public
	 * License, v. 2.0. If a copy of the MPL was not distributed with this
	 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		buttonList.add(new GuiButton(0, guiLeft + xSize - 108, guiTop + 4, 50, 12, "Steal"));
		buttonList.add(new GuiButton(1, guiLeft + xSize - 56, guiTop + 4, 50, 12, "Store"));
	}
	
	/*
	 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
	 * 
	 * This Source Code Form is subject to the terms of the Mozilla Public
	 * License, v. 2.0. If a copy of the MPL was not distributed with this
	 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
	 */
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button.id == 0)// Steal
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					for(int i = 0; i < inventoryRows * 9; i++)
					{
						try
						{
							if(Client.wurst.moduleManager.getModuleFromClass(YesCheat.class).getToggled())
								Thread.sleep(250);
							Slot slot = (Slot)inventorySlots.inventorySlots.get(i);
							handleMouseClick(slot, slot.slotNumber, 0, 1);
							handleMouseClick(slot, slot.slotNumber, 0, 6);
						}catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}).start();
		else if(button.id == 1)// Store
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					for(Object slot : inventorySlots.inventorySlots)
					{
						try
						{
							if(Client.wurst.moduleManager.getModuleFromClass(YesCheat.class).getToggled())
								Thread.sleep(250);
							handleMouseClick((Slot)slot, ((Slot)slot).slotNumber, 0, 1);
						}catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}).start();
	}
	
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items). Args : mouseX, mouseY
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		fontRendererObj.drawString(lowerChestInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
		fontRendererObj.drawString(upperChestInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
	}
	
	/**
	 * Args : renderPartialTicks, mouseX, mouseY
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_147017_u);
		int var4 = (width - xSize) / 2;
		int var5 = (height - ySize) / 2;
		drawTexturedModalRect(var4, var5, 0, 0, xSize, inventoryRows * 18 + 17);
		drawTexturedModalRect(var4, var5 + inventoryRows * 18 + 17, 0, 126, xSize, 96);
	}
}
