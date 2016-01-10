package tk.wurst_client.navigator.settings;

import org.darkstorm.minecraft.gui.component.basic.BasicSlider;

/**
 * Project: EvenWurse
 * Created: 10/01/16
 * Author: nulldev
 */
public class SliderSetting extends BasicSlider implements NavigatorSetting
{
    public SliderSetting() {
        super();
    }

    public SliderSetting(String text, double value, double minimum,
                         double maximum, double increment, ValueDisplay display) {
        super(text, value, minimum, maximum, increment, display);
    }

    public SliderSetting(String text, double value, double minimum,
                         double maximum, int increment) {
        super(text, value, minimum, maximum, increment);
    }

    public SliderSetting(String text, double value, double minimum,
                         double maximum) {
        super(text, value, minimum, maximum);
    }

    public SliderSetting(String text, double value) {
        super(text, value);
    }

    public SliderSetting(String text) {
        super(text);
    }
}
