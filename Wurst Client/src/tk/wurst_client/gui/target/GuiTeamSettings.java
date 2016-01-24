/*
 * Copyright � 2014 - 2015 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui.target;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import tk.wurst_client.WurstClient;
import tk.wurst_client.utils.F;

import java.io.IOException;
import java.util.ArrayList;

/*
TODO FEATURE: Automatic team setup by getting the color of the user's nametag
 */
public class GuiTeamSettings extends GuiScreen {
    private GuiScreen prevMenu;

    public GuiTeamSettings(GuiScreen prevMenu) {
        this.prevMenu = prevMenu;
        WurstClient.INSTANCE.analytics.trackPageView("/team-settings", "Team Settings");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        // color buttons
        String[] colors = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        for (int i = 0; i < 16; i++) {
            int offsetX = -22;
            switch (i % 4) {
                case 3:
                    offsetX = 26;
                    break;
                case 2:
                    offsetX = 2;
                    break;
                case 0:
                    offsetX = -46;
                    break;
            }
            int offsetY = 72;
            switch (i % 16 / 4) {
                case 2:
                    offsetY = 48;
                    break;
                case 1:
                    offsetY = 24;
                    break;
                case 0:
                    offsetY = 0;
                    break;
            }
            buttonList.add(new TeamColorButton(i, width / 2 + offsetX, height / 3 + offsetY,
                    F.SS + colors[i] + colors[i]));
        }
        boolean[] team_colors = WurstClient.INSTANCE.options.target.getTeamColorsSafely();
        for (int i = 0; i < 16; i++) {
            ((TeamColorButton) buttonList.get(i)).setFakeHover(team_colors[i]);
        }

        // other buttons
        buttonList.add(new GuiButton(16, width / 2 - 46, height / 3 + 96, 44, 20, "All On"));
        buttonList.add(new GuiButton(17, width / 2 + 2, height / 3 + 96, 44, 20, "All Off"));
        buttonList.add(new GuiButton(18, width / 2 - 100, height / 3 + 120, 200, 20, "Done"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (!button.enabled) return;
        if (button.id == 18) {
            Minecraft.getMinecraft().displayGuiScreen(prevMenu);
            WurstClient.INSTANCE.analytics.trackEvent("team settings", "done");
        } else {
            switch (button.id) {
                case 16:
                    for (int i = 0; i < 16; i++) {
                        WurstClient.INSTANCE.options.target.team_colors[i] = true;
                        ((TeamColorButton) buttonList.get(i)).setFakeHover(true);
                    }
                    WurstClient.INSTANCE.analytics.trackEvent("team settings", "all on");
                    break;
                case 17:
                    for (int i = 0; i < 16; i++) {
                        WurstClient.INSTANCE.options.target.team_colors[i] = false;
                        ((TeamColorButton) buttonList.get(i)).setFakeHover(false);
                    }
                    WurstClient.INSTANCE.analytics.trackEvent("team settings", "all off");
                    break;
                default:
                    boolean onOff = !WurstClient.INSTANCE.options.target.team_colors[button.id];
                    WurstClient.INSTANCE.options.target.team_colors[button.id] = onOff;
                    ((TeamColorButton) buttonList.get(button.id)).setFakeHover(onOff);
                    WurstClient.INSTANCE.analytics
                            .trackEvent("team settings", "toggle", onOff ? "on" : "off", button.id);
                    break;
            }
            WurstClient.INSTANCE.files.saveOptions();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "Team Settings", width / 2, 20, 16777215);
        drawCenteredString(fontRendererObj, "Target all entities with the following", width / 2, height / 3 - 30,
                10526880);
        drawCenteredString(fontRendererObj, "color(s) in their name:", width / 2, height / 3 - 20, 10526880);

        ArrayList<String> tooltip = new ArrayList<>();
        for (Object aButtonList : buttonList) {
            GuiButton button = ((GuiButton) aButtonList);
            button.drawButton(mc, mouseX, mouseY);

            if (!button.isMouseOver()) continue;
            switch (button.id) {
                case 0:
                    tooltip.add("Black");
                    break;
                case 1:
                    tooltip.add("Dark Blue");
                    break;
                case 2:
                    tooltip.add("Dark Green");
                    break;
                case 3:
                    tooltip.add("Dark Aqua");
                    break;
                case 4:
                    tooltip.add("Dark Red");
                    break;
                case 5:
                    tooltip.add("Dark Purple");
                    break;
                case 6:
                    tooltip.add("Gold");
                    break;
                case 7:
                    tooltip.add("Gray");
                    break;
                case 8:
                    tooltip.add("Dark Gray");
                    break;
                case 9:
                    tooltip.add("Blue");
                    break;
                case 10:
                    tooltip.add("Green");
                    break;
                case 11:
                    tooltip.add("Aqua");
                    break;
                case 12:
                    tooltip.add("Red");
                    break;
                case 13:
                    tooltip.add("Light Purple");
                    break;
                case 14:
                    tooltip.add("Yellow");
                    break;
                case 15:
                    tooltip.add("White");
                    break;
            }
        }
        drawHoveringText(tooltip, mouseX, mouseY);
    }

    public class TeamColorButton extends GuiButton {
        private boolean fakeHover;

        public TeamColorButton(int buttonId, int x, int y, String buttonText) {
            super(buttonId, x, y, 20, 20, buttonText);
        }

        public void setFakeHover(boolean fakeHover) {
            this.fakeHover = fakeHover;
        }

        @Override
        protected int getHoverState(boolean mouseOver) {
            return fakeHover ? super.getHoverState(mouseOver) : 0;
        }
    }
}
