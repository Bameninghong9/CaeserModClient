package com.caeser.mod.gui.hud;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class KeyLayoutEditorScreen extends Screen {

    private final Screen parent;
    private final IHudModule module;
    private final List<KeyElement> keys;
    
    private KeyElement draggingKey = null;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public KeyLayoutEditorScreen(Screen parent, IHudModule module) {
        super(Text.literal("Key Layout Editor"));
        this.parent = parent;
        this.module = module;
        
        if (module instanceof KeystrokesModule) {
            this.keys = ((KeystrokesModule) module).getKeys();
        } else if (module instanceof KeyboardModule) {
            this.keys = ((KeyboardModule) module).getKeys();
        } else {
            this.keys = null;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw dark background
        this.renderBackground(context, mouseX, mouseY, delta);
        
        context.drawTextWithShadow(this.textRenderer, Text.literal("Left Click to Drag. Right Click to Toggle Visibility. ESC to Save."), 10, 10, 0xFFFFFF);

        if (keys == null) return;

        context.getMatrices().pushMatrix();
        // Translate to the module's origin so offsets align visually
        int mx = module.getX();
        int my = module.getY();
        context.getMatrices().translate((float)mx, (float)my);

        for (KeyElement key : keys) {
            int x = key.offsetX;
            int y = key.offsetY;
            int color = key.visible ? 0x80000000 : 0x80FF0000; // Red if hidden
            
            com.caeser.mod.util.RenderUtils.drawRoundedRect(context, x, y, key.width, key.height, 0, color);
            
            int textWidth = this.textRenderer.getWidth(key.name);
            int textX = x + (key.width - textWidth) / 2;
            int textY = y + (key.height - this.textRenderer.fontHeight) / 2;
            context.drawTextWithShadow(this.textRenderer, key.name, textX, textY, 0xFFFFFFFF);
        }

        context.getMatrices().popMatrix();
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (keys == null) return super.mouseClicked(click, bl);
        
        int mx = module.getX();
        int my = module.getY();
        
        for (int i = keys.size() - 1; i >= 0; i--) {
            KeyElement key = keys.get(i);
            int kx = mx + key.offsetX;
            int ky = my + key.offsetY;
            
            if (mouseX >= kx && mouseX <= kx + key.width && mouseY >= ky && mouseY <= ky + key.height) {
                if (button == 0) { // Left click = drag
                    draggingKey = key;
                    dragOffsetX = (int)mouseX - kx;
                    dragOffsetY = (int)mouseY - ky;
                } else if (button == 1) { // Right click = toggle
                    key.visible = !key.visible;
                    saveLayout();
                }
                return true;
            }
        }
        
        return super.mouseClicked(click, bl);
    }

    @Override
    public boolean mouseReleased(net.minecraft.client.gui.Click click) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (button == 0 && draggingKey != null) {
            draggingKey = null;
            saveLayout();
            return true;
        }
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseDragged(net.minecraft.client.gui.Click click, double deltaX, double deltaY) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (draggingKey != null) {
            int mx = module.getX();
            int my = module.getY();
            
            draggingKey.offsetX = (int)mouseX - dragOffsetX - mx;
            draggingKey.offsetY = (int)mouseY - dragOffsetY - my;
            return true;
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }
    
    private void saveLayout() {
        if (keys == null) return;
        
        for (KeyElement key : keys) {
            int[] layoutData = new int[] { key.offsetX, key.offsetY, key.visible ? 1 : 0 };
            if (module instanceof KeystrokesModule) {
                CaeserConfig.INSTANCE.keystrokesLayout.put(key.name, layoutData);
            } else if (module instanceof KeyboardModule) {
                CaeserConfig.INSTANCE.fullKeyboardLayout.put(key.name, layoutData);
            }
        }
        CaeserConfig.save();
    }

    @Override
    public void close() {
        saveLayout();
        this.client.setScreen(parent);
    }
}


