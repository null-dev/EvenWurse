package org.darkstorm.minecraft.gui.layout;

import java.awt.Dimension;
import java.awt.Rectangle;

public interface LayoutManager
{
	void reposition(Rectangle area, Rectangle[] componentAreas,
					Constraint[][] constraints);
	
	Dimension getOptimalPositionedSize(Rectangle[] componentAreas,
									   Constraint[][] constraints);
}
