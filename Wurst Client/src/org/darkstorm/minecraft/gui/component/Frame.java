package org.darkstorm.minecraft.gui.component;

public interface Frame extends Container, DraggableComponent {
    String getTitle();

    void setTitle(String title);

    boolean isPinned();

    void setPinned(boolean pinned);

    boolean isPinnable();

    void setPinnable(boolean pinnable);

    boolean isMinimized();

    void setMinimized(boolean minimized);

    boolean isMinimizable();

    void setMinimizable(boolean minimizable);

    void close();

    boolean isClosable();

    void setClosable(boolean closable);
}
