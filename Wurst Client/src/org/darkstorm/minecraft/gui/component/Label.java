package org.darkstorm.minecraft.gui.component;

public interface Label extends TextComponent {
    TextAlignment getHorizontalAlignment();

    void setHorizontalAlignment(TextAlignment alignment);

    TextAlignment getVerticalAlignment();

    void setVerticalAlignment(TextAlignment alignment);

    enum TextAlignment {
        CENTER,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }
}
