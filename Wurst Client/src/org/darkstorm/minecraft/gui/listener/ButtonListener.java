package org.darkstorm.minecraft.gui.listener;

import org.darkstorm.minecraft.gui.component.Button;

public interface ButtonListener extends ComponentListener {
    void onButtonPress(Button button);
}
