package org.darkstorm.minecraft.gui.component;

import org.darkstorm.minecraft.gui.listener.SelectableComponentListener;

public interface SelectableComponent extends Component
{
	boolean isSelected();
	
	void setSelected(boolean selected);
	
	void addSelectableComponentListener(
			SelectableComponentListener listener);
	
	void removeSelectableComponentListener(
			SelectableComponentListener listener);
}
