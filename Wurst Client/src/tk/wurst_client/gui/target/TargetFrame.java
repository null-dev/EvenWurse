/*
 * Copyright � 2014 - 2015 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui.target;

import net.minecraft.client.Minecraft;
import org.darkstorm.minecraft.gui.component.basic.BasicButton;
import org.darkstorm.minecraft.gui.component.basic.BasicCheckButton;
import org.darkstorm.minecraft.gui.component.basic.BasicFrame;
import org.darkstorm.minecraft.gui.layout.GridLayoutManager;
import org.darkstorm.minecraft.gui.layout.GridLayoutManager.HorizontalGridConstraint;
import tk.wurst_client.WurstClient;

import java.lang.reflect.Field;

public class TargetFrame extends BasicFrame
{
	public TargetFrame()
	{
		setTitle("Target");
		setTheme(WurstClient.INSTANCE.gui.getTheme());
		setLayoutManager(new GridLayoutManager(1, 0));
		setVisible(true);
		setClosable(false);
		setMinimized(true);
		setPinnable(true);
		
		for(Field option : WurstClient.INSTANCE.options.target.getClass()
			.getFields())
		{
			if(!option.getType().equals(boolean.class))
				continue;
			String title =
				option.getName().substring(0, 1).toUpperCase()
					+ option.getName().substring(1).replace("_", " ");
			BasicCheckButton checkbox = new BasicCheckButton(title);
			checkbox.addButtonListener(button -> {
                try
                {
                    option.setBoolean(WurstClient.INSTANCE.options.target,
                        ((BasicCheckButton)button).isSelected());
                    WurstClient.INSTANCE.files.saveOptions();
                }catch(Exception e)
                {
                    System.err
                        .println("[Wurst] Failed to save option \"target."
                            + option.getName() + "\"!");
                    e.printStackTrace();
                }
            });
			try
			{
				checkbox.setSelected(option
					.getBoolean(WurstClient.INSTANCE.options.target));
			}catch(Exception e)
			{
				System.err.println("[Wurst] Failed to load option \"target."
					+ option.getName() + "\"!");
				e.printStackTrace();
				checkbox.setSelected(false);
			}
			add(checkbox, HorizontalGridConstraint.FILL);
		}
		
		BasicButton advancedBtn = new BasicButton("Team Settings", null);
		advancedBtn.addButtonListener(button -> Minecraft.getMinecraft()
            .displayGuiScreen(
                new GuiTeamSettings(
                    Minecraft.getMinecraft().currentScreen)));
		add(advancedBtn);
	}
}
