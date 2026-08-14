package com.caeser.mod.gui;

import com.caeser.mod.config.AutoTextEntry;
import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class AutoTextScreen extends Screen {
    private final Screen parent;
    private int expandedIndex = -1; // -1 for none, -2 for "NEW AUTO TEXT", 0+ for existing entries
    private boolean listeningForKey = false;
    private int listeningForIndex = -1; // Which entry are we listening for? -2 for new

    // Fields for NEW AUTO TEXT
    private TextFieldWidget newCommandField;
    private int newSelectedKey = GLFW.GLFW_KEY_UNKNOWN;

    // Fields for EXISTING AUTO TEXT (only one expanded at a time)
    private TextFieldWidget editCommandField;
    
    private final List<AccordionEntry> entries = new ArrayList<>();
    
    private static class AccordionEntry {
        int y;
        boolean isNew;
        int index;
    }

    public AutoTextScreen(Screen parent) {
        super(Text.literal("AutoText"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.clearChildren();
        super.init();

        int panelWidth = 340;
        int startX = (this.width - panelWidth) / 2;
        
        this.addDrawableChild(new com.caeser.mod.gui.widget.CaeserButtonWidget(startX, 15, 40, 20, Text.literal("< Back"), () -> {
            this.client.setScreen(this.parent);
        }));

        this.newCommandField = new TextFieldWidget(this.textRenderer, startX + 100, 0, panelWidth - 120, 16, Text.literal("Command"));
        this.newCommandField.setMaxLength(256);
        this.addDrawableChild(this.newCommandField);

        this.editCommandField = new TextFieldWidget(this.textRenderer, startX + 100, 0, panelWidth - 120, 16, Text.literal("Command"));
        this.editCommandField.setMaxLength(256);
        this.addDrawableChild(this.editCommandField);
        
        rebuildEntries();
    }
    
    private void rebuildEntries() {
        entries.clear();
        AccordionEntry newEntry = new AccordionEntry();
        newEntry.isNew = true;
        entries.add(newEntry);
        
        for (int i = 0; i < CaeserConfig.INSTANCE.autoTexts.size(); i++) {
            AccordionEntry entry = new AccordionEntry();
            entry.isNew = false;
            entry.index = i;
            entries.add(entry);
        }
    }

    private void drawBorder(DrawContext context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + 1, color); // top
        context.fill(x, y + height - 1, x + width, y + height, color); // bottom
        context.fill(x, y + 1, x + 1, y + height - 1, color); // left
        context.fill(x + width - 1, y + 1, x + width, y + height - 1, color); // right
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        
        this.newCommandField.setVisible(false);
        this.editCommandField.setVisible(false);
        
        int panelWidth = 340;
        int startX = (this.width - panelWidth) / 2;
        int startY = 40;
        
        int currentY = startY;
        
        for (AccordionEntry entry : entries) {
            entry.y = currentY;
            boolean expanded = (entry.isNew && expandedIndex == -2) || (!entry.isNew && expandedIndex == entry.index);
            
            // Draw header
            int headerColor = expanded ? 0xFF1E293B : 0xFF0F172A;
            if (mouseX >= startX && mouseX <= startX + panelWidth && mouseY >= currentY && mouseY <= currentY + 24) {
                headerColor = 0xFF3B82F6; // hover
            }
            context.fill(startX, currentY, startX + panelWidth, currentY + 24, headerColor);
            drawBorder(context, startX, currentY, panelWidth, 24, 0xFF3B82F6);
            
            String title = entry.isNew ? "NEW AUTO TEXT " + (expanded ? "V" : ">") : CaeserConfig.INSTANCE.autoTexts.get(entry.index).text + " " + (expanded ? "V" : ">");
            context.drawTextWithShadow(this.textRenderer, title, startX + 10, currentY + 8, 0xFFFFFFFF);
            
            if (!entry.isNew) {
                // Delete button in header
                int btnX = startX + panelWidth - 24;
                context.fill(btnX, currentY + 4, btnX + 20, currentY + 20, 0xFFEF4444);
                context.drawTextWithShadow(this.textRenderer, "X", btnX + 7, currentY + 10, 0xFFFFFFFF);
            }
            
            currentY += 24;
            
            if (expanded) {
                context.fill(startX, currentY, startX + panelWidth, currentY + 60, 0xFF111827);
                drawBorder(context, startX, currentY, panelWidth, 60, 0xFF3B82F6);
                
                context.drawTextWithShadow(this.textRenderer, "MESSAGE", startX + 20, currentY + 12, 0xFFAAAAAA);
                context.drawTextWithShadow(this.textRenderer, "KEYBIND", startX + 20, currentY + 36, 0xFFAAAAAA);
                
                if (entry.isNew) {
                    if (expanded) {
                        this.newCommandField.setY(currentY + 8);
                        this.newCommandField.setVisible(true);
                    }
                    
                    // Keybind button
                    String keyName = this.newSelectedKey == GLFW.GLFW_KEY_UNKNOWN ? "---" : "KEY";
                    // Just use GLFW.glfwGetKeyName directly to avoid KeyInput mismatch
                    if (this.newSelectedKey != GLFW.GLFW_KEY_UNKNOWN) {
                        String name = GLFW.glfwGetKeyName(this.newSelectedKey, -1);
                        if (name != null) keyName = name.toUpperCase();
                    }
                    if (listeningForKey && listeningForIndex == -2) keyName = "Press a key...";
                    
                    context.fill(startX + 100, currentY + 32, startX + panelWidth - 20, currentY + 48, 0xFF1E293B);
                    drawBorder(context, startX + 100, currentY + 32, panelWidth - 120, 16, 0xFF3B82F6);
                    context.drawTextWithShadow(this.textRenderer, keyName, startX + 110, currentY + 36, 0xFFFFFFFF);
                    
                    // Add Button
                    context.fill(startX + panelWidth - 60, currentY + 32, startX + panelWidth - 5, currentY + 48, 0xFF3B82F6);
                    context.drawTextWithShadow(this.textRenderer, "ADD", startX + panelWidth - 45, currentY + 36, 0xFFFFFFFF);
                    
                } else {
                    
                    if (expanded) {
                        this.editCommandField.setY(currentY + 8);
                        this.editCommandField.setVisible(true);
                    }
                    
                    AutoTextEntry existing = CaeserConfig.INSTANCE.autoTexts.get(entry.index);
                    String keyName = existing.keyCode == GLFW.GLFW_KEY_UNKNOWN ? "---" : "KEY";
                    if (existing.keyCode != GLFW.GLFW_KEY_UNKNOWN) {
                        String name = GLFW.glfwGetKeyName(existing.keyCode, -1);
                        if (name != null) {
                            keyName = name.toUpperCase();
                        } else {
                            keyName = "KEY " + existing.keyCode;
                        }
                    }
                    if (listeningForKey && listeningForIndex == entry.index) keyName = "Press a key...";
                    
                    context.fill(startX + 100, currentY + 32, startX + panelWidth - 20, currentY + 48, 0xFF1E293B);
                    drawBorder(context, startX + 100, currentY + 32, panelWidth - 120, 16, 0xFF3B82F6);
                    context.drawTextWithShadow(this.textRenderer, keyName, startX + 110, currentY + 36, 0xFFFFFFFF);
                    
                    // Save Button
                    context.fill(startX + panelWidth - 60, currentY + 32, startX + panelWidth - 5, currentY + 48, 0xFF3B82F6);
                    context.drawTextWithShadow(this.textRenderer, "SAVE", startX + panelWidth - 45, currentY + 36, 0xFFFFFFFF);
                }
                
                currentY += 65;
            } else {
                currentY += 5;
            }
        }
        
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        double mouseX = click.x();
        double mouseY = click.y();
        if (super.mouseClicked(click, bl)) return true;
        
        int panelWidth = 340;
        int startX = (this.width - panelWidth) / 2;
        
        for (AccordionEntry entry : entries) {
            boolean expanded = (entry.isNew && expandedIndex == -2) || (!entry.isNew && expandedIndex == entry.index);
            
            // Delete button
            if (!entry.isNew && mouseX >= startX + panelWidth - 24 && mouseX <= startX + panelWidth - 4 && mouseY >= entry.y + 4 && mouseY <= entry.y + 20) {
                CaeserConfig.INSTANCE.autoTexts.remove(entry.index);
                CaeserConfig.save();
                expandedIndex = -1;
                rebuildEntries();
                return true;
            }
            
            // Header toggle
            if (mouseX >= startX && mouseX <= startX + panelWidth && mouseY >= entry.y && mouseY <= entry.y + 24) {
                if (entry.isNew) {
                    expandedIndex = expandedIndex == -2 ? -1 : -2;
                } else {
                    if (expandedIndex != entry.index) {
                        expandedIndex = entry.index;
                        this.editCommandField.setText(CaeserConfig.INSTANCE.autoTexts.get(entry.index).text);
                    } else {
                        expandedIndex = -1;
                    }
                }
                return true;
            }
            
            if (expanded) {
                int contentY = entry.y + 24;
                
                // Keybind click
                if (mouseX >= startX + 100 && mouseX <= startX + panelWidth - 70 && mouseY >= contentY + 32 && mouseY <= contentY + 48) {
                    listeningForKey = true;
                    listeningForIndex = entry.isNew ? -2 : entry.index;
                    return true;
                }
                
                // Add/Save click
                if (mouseX >= startX + panelWidth - 60 && mouseX <= startX + panelWidth - 5 && mouseY >= contentY + 32 && mouseY <= contentY + 48) {
                    if (entry.isNew) {
                        if (!newCommandField.getText().isEmpty() && newSelectedKey != GLFW.GLFW_KEY_UNKNOWN) {
                            CaeserConfig.INSTANCE.autoTexts.add(new AutoTextEntry(newSelectedKey, newCommandField.getText()));
                            CaeserConfig.save();
                            newCommandField.setText("");
                            newSelectedKey = GLFW.GLFW_KEY_UNKNOWN;
                            expandedIndex = -1;
                            rebuildEntries();
                        }
                    } else {
                        AutoTextEntry existing = CaeserConfig.INSTANCE.autoTexts.get(entry.index);
                        existing.text = editCommandField.getText();
                        CaeserConfig.save();
                        expandedIndex = -1;
                    }
                    return true;
                }
            }
        }
        
        return false;
    }

    private boolean consumeNextChar = false;

    @Override
    public boolean keyPressed(net.minecraft.client.input.KeyInput input) {
        int keyCode = input.key();
        if (this.listeningForKey) {
            if (keyCode != GLFW.GLFW_KEY_ESCAPE) {
                if (listeningForIndex == -2) {
                    this.newSelectedKey = keyCode;
                } else {
                    CaeserConfig.INSTANCE.autoTexts.get(listeningForIndex).keyCode = keyCode;
                    CaeserConfig.save();
                }
            }
            this.listeningForKey = false;
            this.consumeNextChar = true;
            return true;
        }

        if (super.keyPressed(input)) {
            return true;
        }

        return false;
    }
    
    @Override
    public boolean charTyped(net.minecraft.client.input.CharInput input) {
        if (this.listeningForKey) return true;
        if (this.consumeNextChar) {
            this.consumeNextChar = false;
            return true;
        }
        return super.charTyped(input);
    }
    
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0x88000000);
    }
}
