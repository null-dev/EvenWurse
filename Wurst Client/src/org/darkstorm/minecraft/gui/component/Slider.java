package org.darkstorm.minecraft.gui.component;

import org.darkstorm.minecraft.gui.listener.SliderListener;

public interface Slider extends Component, TextComponent, BoundedRangeComponent {
    String getContentSuffix();

    void setContentSuffix(String suffix);

    boolean isValueChanging();

    void setValueChanging(boolean changing);

    void addSliderListener(SliderListener listener);

    void removeSliderListener(SliderListener listener);

    String getTextWithModPrefix();
}
