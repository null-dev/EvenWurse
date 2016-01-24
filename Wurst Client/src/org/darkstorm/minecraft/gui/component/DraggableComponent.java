package org.darkstorm.minecraft.gui.component;

public interface DraggableComponent extends Component {
    boolean isDragging();

    void setDragging(boolean dragging);
}
