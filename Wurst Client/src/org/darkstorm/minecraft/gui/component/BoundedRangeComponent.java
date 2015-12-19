package org.darkstorm.minecraft.gui.component;

public interface BoundedRangeComponent extends Component
{
	enum ValueDisplay
	{
		DECIMAL,
		INTEGER,
		PERCENTAGE,
		DEGREES,
		NONE
	}
	
	double getValue();
	
	double getMinimumValue();
	
	double getMaximumValue();
	
	double getIncrement();
	
	ValueDisplay getValueDisplay();
	
	void setValue(double value);
	
	void setMinimumValue(double minimumValue);
	
	void setMaximumValue(double maximumValue);
	
	void setIncrement(double increment);
	
	void setValueDisplay(ValueDisplay display);
}
