/*
 * Copyright © 2014 - 2015 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui;

import org.darkstorm.minecraft.gui.component.ComboBox;
import org.darkstorm.minecraft.gui.component.basic.BasicComboBox;
import org.darkstorm.minecraft.gui.component.basic.BasicFrame;
import org.darkstorm.minecraft.gui.layout.GridLayoutManager;
import org.darkstorm.minecraft.gui.layout.GridLayoutManager.HorizontalGridConstraint;
import org.darkstorm.minecraft.gui.listener.ComboBoxListener;

import tk.wurst_client.WurstClient;

public class TargetFrame extends BasicFrame
{
	public TargetFrame()
	{
		setTitle("Target");
		setTheme(WurstClient.INSTANCE.guiManager.getTheme());
		setLayoutManager(new GridLayoutManager(1, 0));
		setVisible(true);
		setClosable(false);
		setMinimized(true);
		setPinnable(true);
		ComboBox targetBox =
			new BasicComboBox("All", "Players", "Mobs", "Animals", "Monsters");
		targetBox.addComboBoxListener(new ComboBoxListener()
		{
			@Override
			public void onComboBoxSelectionChanged(ComboBox comboBox)
			{
				WurstClient.INSTANCE.options.targetMode =
					comboBox.getSelectedIndex();
				WurstClient.INSTANCE.fileManager.saveOptions();
			}
		});
		targetBox.setSelectedIndex(WurstClient.INSTANCE.options.targetMode);
		add(targetBox, HorizontalGridConstraint.CENTER);
	}
}
