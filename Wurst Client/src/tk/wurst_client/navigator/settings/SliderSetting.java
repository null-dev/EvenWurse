package tk.wurst_client.navigator.settings;

import com.google.gson.JsonObject;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import tk.wurst_client.navigator.gui.NavigatorFeatureScreen;

/**
 * Project: EvenWurse
 * Created: 10/01/16
 * Author: nulldev
 */
public class SliderSetting extends BasicSlider implements NavigatorSetting {
    public SliderSetting() {
        super();
    }

    public SliderSetting(String text, double value, double minimum, double maximum, double increment,
                         ValueDisplay display) {
        super(text, value, minimum, maximum, increment, display);
    }

    public SliderSetting(String text, double value, double minimum, double maximum, int increment) {
        super(text, value, minimum, maximum, increment);
    }

    public SliderSetting(String text, double value, double minimum, double maximum) {
        super(text, value, minimum, maximum);
    }

    public SliderSetting(String text, double value) {
        super(text, value);
    }

    public SliderSetting(String text) {
        super(text);
    }

    @Override
    public void addToFeatureScreen(NavigatorFeatureScreen featureScreen) {
        // text
        featureScreen.addText("\n" + getText() + ":\n");

        // slider
        featureScreen.addSlider(featureScreen.new SliderData(this, 60 + featureScreen.getTextHeight()));
    }

    @Override
    public double getValue() {
        return super.getValue();
    }

    @Override
    public void save(JsonObject json) {
        json.addProperty(getText(), getValue());
    }

    @Override
    public void load(JsonObject json) {
        setValue(json.get(getText()).getAsDouble());
    }
}
