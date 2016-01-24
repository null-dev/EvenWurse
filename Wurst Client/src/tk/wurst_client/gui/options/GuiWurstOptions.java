/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui.options;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import tk.wurst_client.WurstClient;
import tk.wurst_client.gui.options.config.GuiConfigManager;
import tk.wurst_client.gui.options.keybinds.GuiKeybindManager;
import tk.wurst_client.gui.options.xray.GuiXRayBlocksManager;
import tk.wurst_client.gui.options.zoom.GuiZoomManager;
import tk.wurst_client.options.OptionsManager.GoogleAnalytics;
import tk.wurst_client.utils.F;
import tk.wurst_client.utils.MiscUtils;

import java.util.ArrayList;

public class GuiWurstOptions extends GuiScreen {
    private GuiScreen prevMenu;
    private String[] modListModes = {"Auto", "Count", "Hidden"};
    private String[] toolTips = {"", "Add/remove friends by clicking them with\n" + "the middle mouse button.",
            F.f("How the mod list under the Wurst logo\n" + "should be displayed.\n" + "�lModes:�r\n" +
                    "<UNDERLINE>Auto</UNDERLINE>: Renders the whole list if it fits\n" + "onto the screen.\n" +
                    "<UNDERLINE>Count</UNDERLINE>: Only renders the number of active\n" + "mods.\n" +
                    "<UNDERLINE>Hidden</UNDERLINE>: Renders nothing."),
            "Automatically maximizes the Minecraft window.\n" + "Windows & Linux only!",
            "Whether or not the Wurst News should be\n" + "shown in the main menu.",
            "Sends anonymous usage statistics that\n" + "help us improve the Wurst Client.",
            "Keybinds allow you to toggle any mod\n" + "or command by simply pressing a\n" + "button.",
            "Manager for the blocks that X-Ray will\n" + "show.",
            "The Zoom Manager allows you to\n" + "change the zoom key, how far it\n" + "will zoom in and more.",
            F.f("Modify the configurations of loaded<NEWLINE>" + "modules to customize your EvenWurse experience!"), "",
            "", "The official website of the Wurst\n" + "Client. Here you can find the\n" +
            "latest Wurst updates, news and the\n" + "Wurst wiki.",
            "The official YouTube channel of the\n" + "Wurst Client. Here we post Wurst\n" +
                    "update videos, Wurst tutorials and\n" + "more.",
            "Our Twitter account shows the latest\n" + "Wurst updates, news and sneak peeks in\n" +
                    "140 characters or less.",
            "The feedback network for the Wurst\n" + "Client, its website, etc. This is\n" +
                    "the place to go for suggesting\n" + "features and reporting bugs.", ""};
    private boolean autoMaximize;

    public GuiWurstOptions(GuiScreen par1GuiScreen) {
        prevMenu = par1GuiScreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        autoMaximize = WurstClient.INSTANCE.files.loadAutoMaximize();
        buttonList.clear();
        buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 144 - 16, 200, 20, "Back"));
        buttonList.add(new GuiButton(1, width / 2 - 154, height / 4 + 24 - 16, 100, 20,
                "Click Friends: " + (WurstClient.INSTANCE.options.middleClickFriends ? "ON" : "OFF")));
        buttonList.add(new GuiButton(2, width / 2 - 154, height / 4 + 48 - 16, 100, 20,
                "Mod List: " + modListModes[WurstClient.INSTANCE.options.modListMode]));
        buttonList.add(new GuiButton(3, width / 2 - 154, height / 4 + 72 - 16, 100, 20,
                "AutoMaximize: " + (autoMaximize ? "ON" : "OFF")));
        buttonList.add(new GuiButton(4, width / 2 - 154, height / 4 + 96 - 16, 100, 20,
                "Wurst News: " + (WurstClient.INSTANCE.options.wurstNews ? "ON" : "OFF")));
        buttonList.add(new GuiButton(5, width / 2 - 154, height / 4 + 120 - 16, 100, 20,
                "Analytics: " + (WurstClient.INSTANCE.options.google_analytics.enabled ? "ON" : "OFF")));
        buttonList.add(new GuiButton(6, width / 2 - 50, height / 4 + 24 - 16, 100, 20, "Keybinds"));
        buttonList.add(new GuiButton(7, width / 2 - 50, height / 4 + 48 - 16, 100, 20, "X-Ray Blocks"));
        buttonList.add(new GuiButton(8, width / 2 - 50, height / 4 + 72 - 16, 100, 20, "Zoom"));
        buttonList.add(new GuiButton(9, width / 2 - 50, height / 4 + 96 - 16, 100, 20, "Module Config"));
        // this.buttonList.add(new GuiButton(9, this.width / 2 - 50, this.height
        // / 4 + 96 - 16, 100, 20, "???"));
        // this.buttonList.add(new GuiButton(10, this.width / 2 - 50,
        // this.height / 4 + 120 - 16, 100, 20, "???"));
        buttonList.add(new GuiButton(11, width / 2 + 54, height / 4 + 24 - 16, 100, 20, "Wurst Website"));
        buttonList.add(new GuiButton(12, width / 2 + 54, height / 4 + 48 - 16, 100, 20, "Wurst on YouTube"));
        buttonList.add(new GuiButton(13, width / 2 + 54, height / 4 + 72 - 16, 100, 20, "Wurst on Twitter"));
        buttonList.add(new GuiButton(14, width / 2 + 54, height / 4 + 96 - 16, 100, 20, "Wurst Feedback"));
        // buttonList.add(new GuiButton(15, width / 2 + 54, height / 4 + 120 -
        // 16, 100, 20, "???"));
        ((GuiButton) buttonList.get(3)).enabled = !Minecraft.isRunningOnMac;
    }

    @Override
    protected void actionPerformed(GuiButton clickedButton) {
        if (clickedButton.enabled) {
            switch (clickedButton.id) {
                case 0:
                    mc.displayGuiScreen(prevMenu);
                    break;
                case 1: // Click Friends
                    WurstClient.INSTANCE.options.middleClickFriends = !WurstClient.INSTANCE.options.middleClickFriends;
                    clickedButton.displayString =
                            "Click Friends: " + (WurstClient.INSTANCE.options.middleClickFriends ? "ON" : "OFF");
                    WurstClient.INSTANCE.files.saveOptions();
                    WurstClient.INSTANCE.analytics.trackEvent("options", "click friends",
                            WurstClient.INSTANCE.options.middleClickFriends ? "ON" : "OFF");
                    break;
                case 2: // Mod List
                    WurstClient.INSTANCE.options.modListMode++;
                    if (WurstClient.INSTANCE.options.modListMode > 2) WurstClient.INSTANCE.options.modListMode = 0;
                    clickedButton.displayString = "Mod List: " + modListModes[WurstClient.INSTANCE.options.modListMode];
                    WurstClient.INSTANCE.files.saveOptions();
                    WurstClient.INSTANCE.analytics
                            .trackEvent("options", "mod list", modListModes[WurstClient.INSTANCE.options.modListMode]);
                    break;
                case 3: // AutoMaximize
                    autoMaximize = !autoMaximize;
                    clickedButton.displayString = "AutoMaximize: " + (autoMaximize ? "ON" : "OFF");
                    WurstClient.INSTANCE.files.saveAutoMaximize(autoMaximize);
                    WurstClient.INSTANCE.analytics.trackEvent("options", "automaximize", autoMaximize ? "ON" : "OFF");
                    break;
                case 4: // Wurst News
                    WurstClient.INSTANCE.options.wurstNews = !WurstClient.INSTANCE.options.wurstNews;
                    clickedButton.displayString =
                            "Wurst News: " + (WurstClient.INSTANCE.options.wurstNews ? "ON" : "OFF");
                    WurstClient.INSTANCE.files.saveOptions();
                    WurstClient.INSTANCE.analytics
                            .trackEvent("options", "wurst news", WurstClient.INSTANCE.options.wurstNews ? "ON" : "OFF");
                    break;
                case 5: // Analytics
                    GoogleAnalytics analytics = WurstClient.INSTANCE.options.google_analytics;
                    if (analytics.enabled) WurstClient.INSTANCE.analytics.trackEvent("options", "analytics", "disable");
                    analytics.enabled = !analytics.enabled;
                    if (analytics.enabled) WurstClient.INSTANCE.analytics.trackEvent("options", "analytics", "enable");
                    clickedButton.displayString = "Analytics: " + (analytics.enabled ? "ON" : "OFF");
                    WurstClient.INSTANCE.files.saveOptions();
                    break;
                case 6:
                    // Keybind Manager
                    mc.displayGuiScreen(new GuiKeybindManager(this));
                    break;
                case 7:
                    // X-Ray Block Manager
                    mc.displayGuiScreen(new GuiXRayBlocksManager(this));
                    break;
                case 8:
                    // Zoom Manager
                    mc.displayGuiScreen(new GuiZoomManager(this));
                    break;
                case 9:
                    //Config Manager
                    mc.displayGuiScreen(new GuiConfigManager(this));
                    break;
                case 10:
                    break;
                case 11:
                    MiscUtils.openLink("https://www.wurst-client.tk/");
                    WurstClient.INSTANCE.analytics.trackEvent("options", "wurst website");
                    break;
                case 12:
                    MiscUtils.openLink("https://www.wurst-client.tk/youtube");
                    WurstClient.INSTANCE.analytics.trackEvent("options", "youtube channel");
                    break;
                case 13:
                    MiscUtils.openLink("https://www.wurst-client.tk/twitter");
                    WurstClient.INSTANCE.analytics.trackEvent("options", "twitter");
                    break;
                case 14:
                    MiscUtils.openLink("https://www.wurst-client.tk/feedback");
                    WurstClient.INSTANCE.analytics.trackEvent("options", "feedback");
                    break;
                case 15:
                    break;
            }
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "Wurst Options", width / 2, 40, 0xffffff);
        drawCenteredString(fontRendererObj, "Settings", width / 2 - 104, height / 4 + 24 - 28, 0xcccccc);
        drawCenteredString(fontRendererObj, "Managers", width / 2, height / 4 + 24 - 28, 0xcccccc);
        drawCenteredString(fontRendererObj, "Online", width / 2 + 104, height / 4 + 24 - 28, 0xcccccc);
        super.drawScreen(par1, par2, par3);
        for (Object aButtonList : buttonList) {
            GuiButton button = (GuiButton) aButtonList;
            if (button.isMouseOver() && !toolTips[button.id].isEmpty()) {
                ArrayList toolTip = Lists.newArrayList(toolTips[button.id].split("\n"));
                drawHoveringText(toolTip, par1, par2);
                break;
            }
        }
    }
}
