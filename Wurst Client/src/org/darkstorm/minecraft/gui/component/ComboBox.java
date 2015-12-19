package org.darkstorm.minecraft.gui.component;

import org.darkstorm.minecraft.gui.listener.ComboBoxListener;

public interface ComboBox extends Component, SelectableComponent
{
	String[] getElements();
	
	void setElements(String... elements);
	
	int getSelectedIndex();
	
	void setSelectedIndex(int selectedIndex);
	
	String getSelectedElement();
	
	void addComboBoxListener(ComboBoxListener listener);
	
	void removeComboBoxListener(ComboBoxListener listener);
}
