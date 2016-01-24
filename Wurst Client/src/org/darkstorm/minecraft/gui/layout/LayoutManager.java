package org.darkstorm.minecraft.gui.layout;

import java.awt.*;

public interface LayoutManager {
    void reposition(Rectangle area, Rectangle[] componentAreas, Constraint[][] constraints);

    Dimension getOptimalPositionedSize(Rectangle[] componentAreas, Constraint[][] constraints);
}
