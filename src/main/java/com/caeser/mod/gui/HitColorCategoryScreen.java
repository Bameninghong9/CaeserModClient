package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.widget.ColorBoxWidget;
import com.caeser.mod.gui.widget.ColorPickerPopup;
import com.caeser.mod.gui.widget.CaeserButtonWidget;
import com.caeser.mod.util.ICustomOverlayTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class HitColorCategoryScreen extends Screen {
    private final Screen parent;
    private ColorBoxWidget allColorBox;
    private ColorPickerPopup activePopup;

    public HitColorCategoryScreen(Screen parent) {
        super(Text.literal("HitColor Categories"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 150;
        int y = 50;

        this.addDrawableChild(new CaeserButtonWidget(x + 8, y - 22, 40, 20, Text.literal("< Back"), () -> {
            this.client.setScreen(this.parent);
        }));

        allColorBox = new ColorBoxWidget(x + 10, y + 25, 20, CaeserConfig.INSTANCE.hitColor, Text.empty(), (box) -> {
            activePopup = new ColorPickerPopup(this.width, this.height, box.getColor(), color -> {
                box.setColor(color);
                CaeserConfig.INSTANCE.hitColor = color;
                updateTextures();
            });
        });
        
        this.addDrawableChild(allColorBox);
    }

    private void updateTextures() {
        if (MinecraftClient.getInstance().gameRenderer != null && MinecraftClient.getInstance().gameRenderer.getOverlayTexture() instanceof ICustomOverlayTexture) {
            ((ICustomOverlayTexture) MinecraftClient.getInstance().gameRenderer.getOverlayTexture()).updateCustomOverlay();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw Glass Background
        context.fill(0, 0, this.width, this.height, 0x80030712);
        
        int x = this.width / 2 - 150;
        int y = 50;
        int w = 300;
        int panelHeight = 60;
        
        // Draw Modal Background
        context.fill(x, y - 30, x + w, y + panelHeight, 0xB20A0F1D);
        
        // Draw Outline
        int outlineColor = 0xFF3B82F6; // Blue outline
        context.fill(x, y - 30, x + w, y - 29, outlineColor); // Top
        context.fill(x, y + panelHeight - 1, x + w, y + panelHeight, outlineColor); // Bottom
        context.fill(x, y - 30, x + 1, y + panelHeight, outlineColor); // Left
        context.fill(x + w - 1, y - 30, x + w, y + panelHeight, outlineColor); // Right
        
        // Draw Header background and line
        context.fill(x + 1, y - 29, x + w - 1, y - 1, 0xFF0F172A);
        context.fill(x, y - 1, x + w, y, 0xFF1E293B);
        
        super.render(context, mouseX, mouseY, delta);

        // Draw Title
        context.drawTextWithShadow(this.textRenderer, this.title, this.width / 2 - this.textRenderer.getWidth(this.title) / 2, y - 20, 0xFFFFFFFF);

        context.drawTextWithShadow(this.textRenderer, Text.literal("HitColor"), x + 40, y + 31, 0xFFFFFFFF);

        if (activePopup != null) {
            context.fill(0, 0, this.width, this.height, 0x66000000);
            activePopup.render(context, mouseX, mouseY, delta);
        }
    }

    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (activePopup != null) {
            if (activePopup.isMouseOver(mouseX, mouseY)) {
                return activePopup.mouseClicked(click, bl);
            } else {
                activePopup = null;
                return true;
            }
        }
        return super.mouseClicked(click, bl);
    }
    
    public boolean mouseDragged(net.minecraft.client.gui.Click click, double deltaX, double deltaY) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (activePopup != null) return activePopup.mouseDragged(click, deltaX, deltaY);
        return super.mouseDragged(click, deltaX, deltaY);
    }
    
    public boolean mouseReleased(net.minecraft.client.gui.Click click) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (activePopup != null) return activePopup.mouseReleased(click);
        return super.mouseReleased(click);
    }
    
    public boolean charTyped(net.minecraft.client.input.CharInput input) {
        char chr = (char) input.codepoint();
        int modifiers = input.modifiers();
        if (activePopup != null) return activePopup.charTyped(input);
        return super.charTyped(input);
    }
    
    public boolean keyPressed(net.minecraft.client.input.KeyInput input) {
        int keyCode = input.key();
        int scanCode = input.scancode();
        int modifiers = input.modifiers();
        if (activePopup != null) return activePopup.keyPressed(input);
        return super.keyPressed(input);
    }

    @Override
    public void close() {
        if (activePopup != null) {
            activePopup = null;
        } else {
            CaeserConfig.save();
            this.client.setScreen(parent);
        }
    }
}
