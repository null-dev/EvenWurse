package org.darkstorm.minecraft.gui.component;

import org.darkstorm.minecraft.gui.listener.SliderListener;

public interface Slider extends Component, TextComponent, BoundedRangeComponent
{
	String getContentSuffix();
	
	boolean isValueChanging();
	
	void setContentSuffix(String suffix);
	
	void setValueChanging(boolean changing);
	
	void addSliderListener(SliderListener listener);
	
	void removeSliderListener(SliderListener listener);
}
