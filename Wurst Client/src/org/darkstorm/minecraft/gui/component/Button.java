package org.darkstorm.minecraft.gui.component;

import org.darkstorm.minecraft.gui.listener.ButtonListener;

import tk.wurst_client.mods.Mod;

public interface Button extends Component, TextComponent
{
	void press();
	
	void addButtonListener(ButtonListener listener);
	
	void removeButtonListener(ButtonListener listener);
	
	ButtonGroup getGroup();
	
	void setGroup(ButtonGroup group);
	
	String getDescription();
	
	Mod getMod();
}
