package org.darkstorm.minecraft.gui.theme;

import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Container;

import java.awt.*;

public interface ComponentUI {
    void render(Component component);

    Rectangle getChildRenderArea(Container container);

    Dimension getDefaultSize(Component component);

    Color getDefaultBackgroundColor(Component component);

    Color getDefaultForegroundColor(Component component);

    Rectangle[] getInteractableRegions(Component component);

    void handleInteraction(Component component, Point location, int button);

    void handleUpdate(Component component);
}
