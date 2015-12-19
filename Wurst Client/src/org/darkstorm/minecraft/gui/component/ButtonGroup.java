package org.darkstorm.minecraft.gui.component;

public interface ButtonGroup
{
	void addButton(Button button);
	
	void removeButton(Button button);
	
	Button[] getButtons();
}
