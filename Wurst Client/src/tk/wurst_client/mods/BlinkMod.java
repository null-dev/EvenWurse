/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

import java.util.ArrayList;

@Info(category = Category.MOVEMENT,
        description = "Suspends all motion updates while enabled.\n" +
                "Can be used for teleportation, instant picking up of items and more.",
        name = "Blink")
public class BlinkMod extends Mod {
    private static ArrayList<Packet> packets = new ArrayList<>();
    private static long blinkTime;
    private static long lastTime;
    private EntityOtherPlayerMP fakePlayer = null;
    private double oldX;
    private double oldY;
    private double oldZ;

    public static void addToBlinkQueue(Packet packet) {
        if (Minecraft.getMinecraft().thePlayer.posX != Minecraft.getMinecraft().thePlayer.prevPosX ||
                Minecraft.getMinecraft().thePlayer.posZ != Minecraft.getMinecraft().thePlayer.prevPosZ ||
                Minecraft.getMinecraft().thePlayer.posY != Minecraft.getMinecraft().thePlayer.prevPosY) {
            blinkTime += System.currentTimeMillis() - lastTime;
            packets.add(packet);
        }
        lastTime = System.currentTimeMillis();
    }

    @Override
    public String getRenderName() {
        return "Blink [" + blinkTime + "ms]";
    }

    @Override
    public void onEnable() {
        lastTime = System.currentTimeMillis();

        oldX = Minecraft.getMinecraft().thePlayer.posX;
        oldY = Minecraft.getMinecraft().thePlayer.posY;
        oldZ = Minecraft.getMinecraft().thePlayer.posZ;
        fakePlayer = new EntityOtherPlayerMP(Minecraft.getMinecraft().theWorld,
                Minecraft.getMinecraft().thePlayer.getGameProfile());
        fakePlayer.clonePlayer(Minecraft.getMinecraft().thePlayer, true);
        fakePlayer.copyLocationAndAnglesFrom(Minecraft.getMinecraft().thePlayer);
        fakePlayer.rotationYawHead = Minecraft.getMinecraft().thePlayer.rotationYawHead;
        Minecraft.getMinecraft().theWorld.addEntityToWorld(-69, fakePlayer);
    }

    @Override
    public void onDisable() {
        for (Packet packet : packets) {
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
        }
        packets.clear();
        Minecraft.getMinecraft().theWorld.removeEntityFromWorld(-69);
        fakePlayer = null;
        blinkTime = 0;
    }

    public void cancel() {
        packets.clear();
        Minecraft.getMinecraft().thePlayer
                .setPositionAndRotation(oldX, oldY, oldZ, Minecraft.getMinecraft().thePlayer.rotationYaw,
                        Minecraft.getMinecraft().thePlayer.rotationPitch);
        setEnabled(false);
    }
}
