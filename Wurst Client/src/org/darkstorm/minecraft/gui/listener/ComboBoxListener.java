package org.darkstorm.minecraft.gui.listener;

import org.darkstorm.minecraft.gui.component.ComboBox;

public interface ComboBoxListener extends ComponentListener {
    void onComboBoxSelectionChanged(ComboBox comboBox);
}
