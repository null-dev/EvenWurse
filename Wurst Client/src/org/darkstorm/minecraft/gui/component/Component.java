package org.darkstorm.minecraft.gui.component;

import org.darkstorm.minecraft.gui.theme.Theme;

import java.awt.*;

public interface Component {
    Theme getTheme();

    void setTheme(Theme theme);

    void render();

    void update();

    int getX();

    void setX(int x);

    int getY();

    void setY(int y);

    int getWidth();

    void setWidth(int width);

    int getHeight();

    void setHeight(int height);

    Point getLocation();

    Dimension getSize();

    Rectangle getArea();

    Container getParent();

    void setParent(Container parent);

    Color getBackgroundColor();

    void setBackgroundColor(Color color);

    Color getForegroundColor();

    void setForegroundColor(Color color);

    void onMousePress(int x, int y, int button);

    void onMouseRelease(int x, int y, int button);

    void resize();

    boolean isVisible();

    void setVisible(boolean visible);

    boolean isEnabled();

    void setEnabled(boolean enabled);
}
