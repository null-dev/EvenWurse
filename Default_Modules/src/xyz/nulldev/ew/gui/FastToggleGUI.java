package xyz.nulldev.ew.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.darkstorm.minecraft.gui.util.RenderUtil;
import tk.wurst_client.WurstClient;
import tk.wurst_client.api.GUI;
import tk.wurst_client.api.KeyCode;
import tk.wurst_client.api.ModuleConfiguration;
import tk.wurst_client.mods.Mod;
import tk.wurst_client.utils.F;
import xyz.nulldev.ew.mods.FastToggleMod;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * Project: EvenWurse
 * Created: 01/01/16
 * Author: nulldev
 */

/**
 * Toggle mods at the speed of light! (Almost)
 */
public class FastToggleGUI extends GuiScreen {
    private GuiTextField searchBar;
    private int selectionIndex = -1;
    private ArrayList<Tuple<String, Mod>> filteredMods = null;
    private ArrayList<Tuple<Rectangle, Mod>> rendererdMods = new ArrayList<>();

    public FastToggleGUI() {
        searchBar = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, 0, 32, 200, 20);
        searchBar.setEnableBackgroundDrawing(true);
        searchBar.setFocused(true);
    }

    @Override
    public void initGui() {
        super.initGui();
        searchBar.xPosition = width / 2 - 100;
        searchBar.setSelectionPos(searchBar.getText().length());
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if(keyCode == KeyCode.RIGHT_ARROW || keyCode == KeyCode.LEFT_ARROW) {
            return;
        } else if(keyCode == KeyCode.UP_ARROW) {
            if(selectionIndex > 0) selectionIndex--;
            updateSelection();
            return;
        } else if(keyCode == KeyCode.DOWN_ARROW && filteredMods != null) {
            if(selectionIndex < filteredMods.size() - 1) selectionIndex++;
            updateSelection();
            return;
        } else if(keyCode == KeyCode.BACKSPACE) {
            deleteSelection();
        } else if(keyCode == KeyCode.ENTER && filteredMods != null && selectionIndex != -1) {
            //Toggle highlighted mod
            filteredMods.get(selectionIndex).getValue().toggle();
            GUI.displayGuiScreen(null);
            return;
        }
        searchBar.textboxKeyTyped(typedChar, keyCode);
        if(searchBar.getText().length() < 1) {
            filteredMods = null;
            selectionIndex = -1;
        } else {
            filteredMods = filterMods(searchBar.getText());
            if(filteredMods.size() < 1) {
                selectionIndex = -1;
            } else {
                selectionIndex = 0;
            }
        }
        updateSelection();
    }

    void updateSelection() {
        if(selectionIndex != -1 && filteredMods != null) {
            String name = filteredMods.get(selectionIndex).getKey();
            String[] split = name.split("</GREEN>");
            String value = "";
            if(split.length > 1) {
                value = split[1];
            }
            deleteSelection();
            int origPos = searchBar.getCursorPosition();
            searchBar.setText(searchBar.getText() + value);
            searchBar.setCursorPosition(origPos);
            searchBar.setSelectionPos(searchBar.getText().length());
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(mouseButton == 0) {
            for(Tuple<Rectangle, Mod> renderedMod : rendererdMods) {
                if(renderedMod.getKey().contains(mouseX, mouseY)) {
                    renderedMod.getValue().toggle();
                    break;
                }
            }
        }
    }

    void deleteSelection() {
        searchBar.setText(searchBar.getText().substring(0,
                searchBar.getText().length() - searchBar.getSelectedText().length()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawDefaultBackground();
        searchBar.drawTextBox();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_CULL_FACE);
        glDisable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        int x = width / 2/* - 50*/;
//        RenderUtil.scissorBox(0, 59, width, height - 42);
//        glEnable(GL_SCISSOR_TEST);
        rendererdMods.clear();
        if(filteredMods != null) {
            int maxMods = ModuleConfiguration.forModule(WurstClient.INSTANCE.mods
                    .getModByClass(FastToggleMod.class))
                    .getInt("Max Mods on Screen", 15);
            int i = 0;
            for (Tuple<String, Mod> mod : filteredMods) {
                if(i - 1 > maxMods) break;
                // y position
                int y = 60 + i * 20;
                if (y < 40)
                    continue;
                if (y > height - 40)
                    break;

                Rectangle area;
                if(i == selectionIndex) {
                    area = new Rectangle(x - 109, y - 3, 218, 18);
                } else {
                    area = new Rectangle(x - 100, y, 200, 12);
                }
                rendererdMods.add(new Tuple<>(area, mod.getValue()));

                // color
                boolean hovering =
                        area.contains(mouseX, mouseY) || i == selectionIndex;
                if (mod.getValue().isEnabled())
                    if (mod.getValue().isBlocked())
                        glColor4f(hovering ? 1F : 0.875F, 0F, 0F, 0.5F);
                    else
                        glColor4f(0F, hovering ? 1F : 0.875F, 0F, 0.5F);
                else if (hovering)
                    glColor4f(0.375F, 0.375F, 0.375F, 0.5F);
                else
                    glColor4f(0.25F, 0.25F, 0.25F, 0.5F);

                // box & shadow
                glBegin(GL_QUADS);
                {
                    glVertex2d(area.x, area.y);
                    glVertex2d(area.x + area.width, area.y);
                    glVertex2d(area.x + area.width, area.y + area.height);
                    glVertex2d(area.x, area.y + area.height);
                }
                glEnd();
                RenderUtil.boxShadow(area.x, area.y, area.x + area.width, area.y
                        + area.height);

                // text
                glEnable(GL_TEXTURE_2D);
                try {
                    String buttonText = F.f(mod.getKey());
                    GUI.getFontRenderer().drawString(
                            buttonText,
                            area.x
                                    + (area.width - GUI.getFontRenderer()
                                    .getStringWidth(buttonText)) / 2, area.y +((area.height/2) - 4),
                            0xffffff);
                } catch (Exception e) {
                    System.out.println("[EvenWurse] Exception filtering mods!");
                    e.printStackTrace();
                }
                glDisable(GL_TEXTURE_2D);
                i++;
            }
        }
        //RESET
//        glDisable(GL_SCISSOR_TEST);
        glEnable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    ArrayList<Tuple<String, Mod>> filterMods(String string) {
        ArrayList<Tuple<String, Mod>> out = new ArrayList<>();
        for(Mod mod : WurstClient.INSTANCE.mods.getAllMods()) {
            String name = mod.getName();
            if(name.toUpperCase().startsWith(string.toUpperCase())) {
                String begin = name.substring(0, string.length());
                String sub = name.substring(string.length());
                out.add(new Tuple<>("<GREEN>" + begin + "</GREEN>" + sub, mod));
            }
        }
        return out;
    }
}
class Tuple<Key, Value> {
    private Key key;
    private Value value;

    public Tuple(Key key, Value value)
    {
        this.key = key;
        this.value = value;
    }

    public Key getKey()
    {
        return this.key;
    }

    public Value getValue()
    {
        return this.value;
    }
}
