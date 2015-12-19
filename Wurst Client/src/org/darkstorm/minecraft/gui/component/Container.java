package org.darkstorm.minecraft.gui.component;

import org.darkstorm.minecraft.gui.layout.Constraint;
import org.darkstorm.minecraft.gui.layout.LayoutManager;

public interface Container extends Component
{
	LayoutManager getLayoutManager();
	
	void setLayoutManager(LayoutManager layoutManager);
	
	Component[] getChildren();
	
	void add(Component child, Constraint... constraints);
	
	Constraint[] getConstraints(Component child);
	
	Component getChildAt(int x, int y);
	
	boolean hasChild(Component component);
	
	boolean remove(Component child);
	
	void layoutChildren();
}
