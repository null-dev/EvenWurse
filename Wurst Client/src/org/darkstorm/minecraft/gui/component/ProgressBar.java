package org.darkstorm.minecraft.gui.component;

public interface ProgressBar extends Component, BoundedRangeComponent {
    boolean isIndeterminate();

    void setIndeterminate(boolean indeterminate);
}
