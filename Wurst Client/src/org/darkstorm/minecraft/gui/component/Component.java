package org.darkstorm.minecraft.gui.component;

import org.darkstorm.minecraft.gui.theme.Theme;

import java.awt.*;

public interface Component
{
	Theme getTheme();
	
	void setTheme(Theme theme);
	
	void render();
	
	void update();
	
	int getX();
	
	int getY();
	
	int getWidth();
	
	int getHeight();
	
	void setX(int x);
	
	void setY(int y);
	
	void setWidth(int width);
	
	void setHeight(int height);
	
	Point getLocation();
	
	Dimension getSize();
	
	Rectangle getArea();
	
	Container getParent();
	
	Color getBackgroundColor();
	
	Color getForegroundColor();
	
	void setBackgroundColor(Color color);
	
	void setForegroundColor(Color color);
	
	void setParent(Container parent);
	
	void onMousePress(int x, int y, int button);
	
	void onMouseRelease(int x, int y, int button);
	
	void resize();
	
	boolean isVisible();
	
	void setVisible(boolean visible);
	
	boolean isEnabled();
	
	void setEnabled(boolean enabled);
}
