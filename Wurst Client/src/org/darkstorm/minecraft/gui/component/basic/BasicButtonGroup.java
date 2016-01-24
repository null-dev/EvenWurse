package org.darkstorm.minecraft.gui.component.basic;

import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.ButtonGroup;

import java.util.ArrayList;
import java.util.List;

public class BasicButtonGroup implements ButtonGroup {
    private final List<Button> buttons = new ArrayList<>();

    @Override
    public void addButton(Button button) {
        if (button == null) throw new NullPointerException();
        synchronized (buttons) {
            buttons.add(button);
        }
    }

    @Override
    public void removeButton(Button button) {
        if (button == null) throw new NullPointerException();
        synchronized (buttons) {
            buttons.remove(button);
        }
    }

    @Override
    public Button[] getButtons() {
        synchronized (buttons) {
            return buttons.toArray(new Button[buttons.size()]);
        }
    }

}
