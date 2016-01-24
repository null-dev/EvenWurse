package org.darkstorm.minecraft.gui.component;

public interface BoundedRangeComponent extends Component {
    double getValue();

    void setValue(double value);

    double getMinimumValue();

    void setMinimumValue(double minimumValue);

    double getMaximumValue();

    void setMaximumValue(double maximumValue);

    double getIncrement();

    void setIncrement(double increment);

    ValueDisplay getValueDisplay();

    void setValueDisplay(ValueDisplay display);

    enum ValueDisplay {
        DECIMAL,
        INTEGER,
        PERCENTAGE,
        DEGREES,
        NONE
    }
}
