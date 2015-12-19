package org.darkstorm.minecraft.gui.component;

public interface Label extends TextComponent
{
	enum TextAlignment
	{
		CENTER,
		LEFT,
		RIGHT,
		TOP,
		BOTTOM
	}
	
	TextAlignment getHorizontalAlignment();
	
	TextAlignment getVerticalAlignment();
	
	void setHorizontalAlignment(TextAlignment alignment);
	
	void setVerticalAlignment(TextAlignment alignment);
}
