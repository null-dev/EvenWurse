package org.darkstorm.minecraft.gui.theme;

import org.darkstorm.minecraft.gui.component.Component;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTheme implements Theme {
    protected final Map<Class<? extends Component>, ComponentUI> uis;

    public AbstractTheme() {
        uis = new HashMap<>();
    }

    protected void installUI(AbstractComponentUI<?> ui) {
        uis.put(ui.handledComponentClass, ui);
    }

    @Override
    public ComponentUI getUIForComponent(Component component) {
        if (component == null) throw new IllegalArgumentException();
        return getComponentUIForClass(component.getClass());
    }

    @SuppressWarnings("unchecked")
    public ComponentUI getComponentUIForClass(Class<? extends Component> componentClass) {
        for (Class<?> componentInterface : componentClass.getInterfaces()) {
            ComponentUI ui = uis.get(componentInterface);
            if (ui != null) return ui;
        }
        if (componentClass.getSuperclass().equals(Component.class)) {
            return uis.get(componentClass);
        } else if (!Component.class.isAssignableFrom(componentClass.getSuperclass())) return null; // WTF?
        return getComponentUIForClass((Class<? extends Component>) componentClass.getSuperclass());
    }

}
