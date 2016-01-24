/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui.multiplayer;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.ServerData;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import tk.wurst_client.WurstClient;
import tk.wurst_client.servers.ServerPinger;
import tk.wurst_client.utils.F;
import tk.wurst_client.utils.Formatting;
import tk.wurst_client.utils.MiscUtils;

import java.io.IOException;
import java.util.ArrayList;

public class GuiServerFinder extends GuiScreen {
    private GuiMultiplayer prevMenu;
    private GuiTextField ipBox;
    private GuiTextField maxThreadsBox;
    private boolean running;
    private int checked;
    private int working;
    private boolean terminated;

    public GuiServerFinder(GuiMultiplayer prevMultiplayerMenu) {
        prevMenu = prevMultiplayerMenu;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        ipBox.updateCursorCounter();
        ((GuiButton) buttonList.get(0)).enabled =
                // 1.1.1.1 has a length of 7
                ipBox.getText().trim().length() >= 7
                        // Must have dots
                        && ipBox.getText().contains(".")
                        // Must not have a port
                        && !ipBox.getText().contains(":")
                        // Must have three dots
                        && StringUtils.countMatches(ipBox.getText(), ".") == 3
                        // The first part must be an integer
                        && MiscUtils.isInteger(ipBox.getText().split("\\.", -1)[0])
                        // And the second
                        && MiscUtils.isInteger(ipBox.getText().split("\\.", -1)[1])
                        // And the third
                        && MiscUtils.isInteger(ipBox.getText().split("\\.", -1)[2])
                        // And so on
                        && MiscUtils.isInteger(ipBox.getText().split("\\.", -1)[3]) && !running &&
                        MiscUtils.isInteger(maxThreadsBox.getText());
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, "Search"));
        buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, "Back"));
        ipBox = new GuiTextField(0, fontRendererObj, width / 2 - 100, height / 4 + 34, 200, 20);
        ipBox.setMaxStringLength(15);
        ipBox.setFocused(true);
        maxThreadsBox = new GuiTextField(1, fontRendererObj, width / 2 - 32, height / 4 + 58, 26, 12);
        maxThreadsBox.setMaxStringLength(3);
        maxThreadsBox.setFocused(false);
        maxThreadsBox.setText(Integer.toString(WurstClient.INSTANCE.options.serverFinderThreads));
        running = false;
        terminated = false;
        WurstClient.INSTANCE.analytics.trackPageView("/multiplayer/server-finder", "Server Finder");
    }

    /**
     * "Called when the screen is unloaded. Used to disable keyboard repeat events."
     */
    @Override
    public void onGuiClosed() {
        terminated = true;
        if (MiscUtils.isInteger(maxThreadsBox.getText())) {
            WurstClient.INSTANCE.options.serverFinderThreads = Integer.valueOf(maxThreadsBox.getText());
            WurstClient.INSTANCE.files.saveOptions();
        }
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton clickedButton) {
        if (clickedButton.enabled) {
            if (clickedButton.id == 0) {// Search
                if (MiscUtils.isInteger(maxThreadsBox.getText())) {
                    WurstClient.INSTANCE.options.serverFinderThreads = Integer.valueOf(maxThreadsBox.getText());
                    WurstClient.INSTANCE.files.saveOptions();
                }
                running = true;
                new Thread("Server Finder") {
                    @Override
                    public void run() {
                        int[] ipParts = new int[4];
                        for (int i = 0; i < ipBox.getText().split("\\.").length; i++) {
                            ipParts[i] = Integer.valueOf(ipBox.getText().split("\\.")[i]);
                        }
                        ArrayList<ServerPinger> pingers = new ArrayList<>();
                        serverFinder:
                        for (int i = 3; i >= 0; i--) {
                            for (int i2 = 0; i2 <= 255; i2++) {
                                if (terminated) break serverFinder;
                                int[] ipParts2 = ipParts.clone();
                                ipParts2[i] = i2;
                                String ip = ipParts2[0] + "." + ipParts2[1] + "." + ipParts2[2] + "." + ipParts2[3];

                                ServerPinger pinger = new ServerPinger();
                                pinger.ping(ip);
                                pingers.add(pinger);
                                while (pingers.size() >= WurstClient.INSTANCE.options.serverFinderThreads) {
                                    pingers = updatePingers(pingers);
                                }
                            }
                        }
                        while (pingers.size() > 0) pingers = updatePingers(pingers);
                        WurstClient.INSTANCE.analytics.trackEvent("server finder", "complete", "complete", working);
                    }
                }.start();
                WurstClient.INSTANCE.analytics.trackEvent("server finder", "start");
            } else if (clickedButton.id == 1) mc.displayGuiScreen(prevMenu);
        }
    }

    private ArrayList<ServerPinger> updatePingers(ArrayList<ServerPinger> pingers) {
        for (int i = 0; i < pingers.size(); i++) {
            if (!pingers.get(i).isStillPinging()) {
                GuiServerFinder.this.checked++;
                if (pingers.get(i).isWorking()) {
                    GuiServerFinder.this.working++;
                    GuiServerFinder.this.prevMenu.savedServerList
                            .addServerData(new ServerData("Grief me #" + working, pingers.get(i).server.serverIP));
                    GuiServerFinder.this.prevMenu.savedServerList.saveServerList();
                    GuiServerFinder.this.prevMenu.serverListSelector.setSelectedServer(-1);
                    GuiServerFinder.this.prevMenu.serverListSelector
                            .func_148195_a(GuiServerFinder.this.prevMenu.savedServerList);
                }
                pingers.remove(i);
            }
        }
        return pingers;
    }

    /**
     * Fired when a key is typed. This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char par1, int par2) {
        ipBox.textboxKeyTyped(par1, par2);
        maxThreadsBox.textboxKeyTyped(par1, par2);

        if (par2 == 28 || par2 == 156) actionPerformed((GuiButton) buttonList.get(0));
    }

    /**
     * Called when the mouse is clicked.
     *
     * @throws IOException
     */
    @Override
    protected void mouseClicked(int par1, int par2, int par3) throws IOException {
        super.mouseClicked(par1, par2, par3);
        ipBox.mouseClicked(par1, par2, par3);
        maxThreadsBox.mouseClicked(par1, par2, par3);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "Server Finder", width / 2, 20, 16777215);
        drawCenteredString(fontRendererObj, "This will search for servers with similar IPs", width / 2, 40, 10526880);
        drawCenteredString(fontRendererObj, "to the IP you type into the field below.", width / 2, 50, 10526880);
        drawCenteredString(fontRendererObj, "The servers it finds will be added to your server list.", width / 2, 60,
                10526880);
        drawString(fontRendererObj, "Numeric IP without port", width / 2 - 100, height / 4 + 24, 10526880);
        ipBox.drawTextBox();
        drawString(fontRendererObj, "Max. threads:", width / 2 - 100, height / 4 + 60, 10526880);
        maxThreadsBox.drawTextBox();
        if (!((GuiButton) buttonList.get(0)).enabled) {
            if (ipBox.getText().length() == 0) {
                drawCenteredString(fontRendererObj, Formatting.SS + "4IP field is empty!", width / 2, height / 4 + 73,
                        10526880);
            } else if (ipBox.getText().contains(":")) {
                drawCenteredString(fontRendererObj, F.SS + "4Ports are not supported!", width / 2, height / 4 + 73,
                        10526880);
            } else if (!MiscUtils.isInteger(ipBox.getText().split("\\.", -1)[0])) {
                drawCenteredString(fontRendererObj, F.SS + "4Hostnames are not supported!", width / 2, height / 4 + 73,
                        10526880);
            } else if (StringUtils.countMatches(ipBox.getText(), ".") >= 1 &&
                    !MiscUtils.isInteger(ipBox.getText().split("\\.", -1)[1])) {
                drawCenteredString(fontRendererObj, F.SS + "4Hostnames are not supported!", width / 2, height / 4 + 73,
                        10526880);
            } else if (StringUtils.countMatches(ipBox.getText(), ".") >= 2 &&
                    !MiscUtils.isInteger(ipBox.getText().split("\\.", -1)[2])) {
                drawCenteredString(fontRendererObj, F.SS + "4Hostnames are not supported!", width / 2, height / 4 + 73,
                        10526880);
            } else if (StringUtils.countMatches(ipBox.getText(), ".") >= 3 &&
                    !MiscUtils.isInteger(ipBox.getText().split("\\.", -1)[3])) {
                drawCenteredString(fontRendererObj, F.SS + "4Hostnames are not supported!", width / 2, height / 4 + 73,
                        10526880);
            } else if (StringUtils.countMatches(ipBox.getText(), ".") < 3) {
                drawCenteredString(fontRendererObj, "�4IP is too short!", width / 2, height / 4 + 73, 10526880);
            } else if (StringUtils.countMatches(ipBox.getText(), ".") > 3) {
                drawCenteredString(fontRendererObj, "�4IP is too long!", width / 2, height / 4 + 73, 10526880);
            } else if (!MiscUtils.isInteger(maxThreadsBox.getText())) {
                drawCenteredString(fontRendererObj, F.SS + "4Max. threads must be a number!", width / 2,
                        height / 4 + 73, 10526880);
            } else if (running) {
                if (checked == 1024) {
                    drawCenteredString(fontRendererObj, F.SS + "2Done!", width / 2, height / 4 + 73, 10526880);
                } else {
                    drawCenteredString(fontRendererObj, F.SS + "2Searching...", width / 2, height / 4 + 73, 10526880);
                }
            } else {
                drawCenteredString(fontRendererObj, F.SS + "4Unknown error! Bug?", width / 2, height / 4 + 73,
                        10526880);
            }
        }
        drawString(fontRendererObj, "Checked: " + checked + " / 1024", width / 2 - 100, height / 4 + 84, 10526880);
        drawString(fontRendererObj, "Working: " + working, width / 2 - 100, height / 4 + 94, 10526880);
        super.drawScreen(par1, par2, par3);
    }
}
