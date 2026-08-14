package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.hud.HudManager;
import com.caeser.mod.gui.hud.IHudModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CaeserMainMenuScreen extends Screen {
    private final Screen parent;
    private IHudModule draggingModule = null;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;
    
    private IHudModule scalingModule = null;
    private int scaleStartX = 0;
    private float initialScale = 1.0f;

    public CaeserMainMenuScreen(Screen parent) {
        super(Text.literal("Caeser Main Menu"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        
        // Centered "Mod Menu" button
        int buttonWidth = 200;
        int buttonHeight = 20;
        int x = this.width / 2 - buttonWidth / 2;
        int y = this.height / 2 - buttonHeight / 2;
        
        this.addDrawableChild(new net.minecraft.client.gui.widget.ClickableWidget(x, y, buttonWidth, buttonHeight, Text.literal("Caeser Mod Menu")) {
            @Override
            public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
                boolean hovered = this.isHovered();
                int bgColor = hovered ? 0xFF2A2E3D : 0xFF1E212A;
                
                context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, bgColor);
                
                // Border
                context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + 1, 0xFF363B4F); // Top
                context.fill(this.getX(), this.getY() + this.height - 1, this.getX() + this.width, this.getY() + this.height, 0xFF363B4F); // Bottom
                context.fill(this.getX(), this.getY(), this.getX() + 1, this.getY() + this.height, 0xFF363B4F); // Left
                context.fill(this.getX() + this.width - 1, this.getY(), this.getX() + this.width, this.getY() + this.height, 0xFF363B4F); // Right
                
                // Text
                int textWidth = client.textRenderer.getWidth(this.getMessage());
                context.drawTextWithShadow(client.textRenderer, this.getMessage(), this.getX() + (this.width - textWidth) / 2, this.getY() + (this.height - 8) / 2, 0xFFFFFFFF);
            }
            
            @Override
            protected void appendClickableNarrations(net.minecraft.client.gui.screen.narration.NarrationMessageBuilder builder) {}
            
            public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (this.isHovered()) {
                    client.setScreen(new CaeserSettingsScreen(CaeserMainMenuScreen.this));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
        for (IHudModule module : HudManager.INSTANCE.getModules()) {
            if (module.isEnabled()) {
                module.render(context, delta);
                
                int mx = module.getX();
                int my = module.getY();
                int mw = (int)(module.getWidth() * module.getScale());
                int mh = (int)(module.getHeight() * module.getScale());
                
                boolean hovered = mouseX >= mx && mouseX <= mx + mw && mouseY >= my && mouseY <= my + mh;
                boolean isDragging = draggingModule == module;
                
                if (hovered || isDragging) {
                    int color = 0x803B82F6;
                    context.fill(mx, my, mx + mw, my + mh, 0x40000000);
                    context.fill(mx, my, mx + mw, my + 1, color);
                    context.fill(mx, my + mh - 1, mx + mw, my + mh, color);
                    context.fill(mx, my, mx + 1, my + mh, color);
                    context.fill(mx + mw - 1, my, mx + mw, my + mh, color);
                }
                
                if (hovered) {
                    int closeX = mx + mw - 8;
                    int closeY = my - 2;
                    boolean closeHovered = mouseX >= closeX && mouseX <= closeX + 10 && mouseY >= closeY && mouseY <= closeY + 10;
                    context.drawTextWithShadow(this.textRenderer, "x", closeX + 2, closeY + 1, closeHovered ? 0xFFFF0000 : 0x80FF0000);
                    
                    int resizeX = mx + mw - 6;
                    int resizeY = my + mh - 6;
                    boolean resizeHovered = mouseX >= resizeX && mouseX <= mx + mw && mouseY >= resizeY && mouseY <= my + mh;
                    context.fill(resizeX, resizeY, mx + mw, my + mh, resizeHovered ? 0xFF3B82F6 : 0x803B82F6);
                }

                if (isDragging) {
                    int centerX = mx + mw / 2;
                    int centerY = my + mh / 2;
                    if (Math.abs(centerX - this.width / 2) <= 1) {
                        context.fill(this.width / 2, 0, this.width / 2 + 1, this.height, 0xFFFFFFFF);
                    }
                    if (Math.abs(centerY - this.height / 2) <= 1) {
                        context.fill(0, this.height / 2, this.width, this.height / 2 + 1, 0xFFFFFFFF);
                    }
                    
                    for (IHudModule other : HudManager.INSTANCE.getModules()) {
                        if (other.isEnabled() && other != module) {
                            int ox = other.getX();
                            int oy = other.getY();
                            int ow = (int)(other.getWidth() * other.getScale());
                            int oh = (int)(other.getHeight() * other.getScale());
                            
                            if (Math.abs(mx - ox) <= 1) context.fill(mx, 0, mx + 1, this.height, 0x80FFFFFF);
                            if (Math.abs((mx + mw) - (ox + ow)) <= 1) context.fill(mx + mw, 0, mx + mw + 1, this.height, 0x80FFFFFF);
                            if (Math.abs(my - oy) <= 1) context.fill(0, my, this.width, my + 1, 0x80FFFFFF);
                            if (Math.abs((my + mh) - (oy + oh)) <= 1) context.fill(0, my + mh, this.width, my + mh + 1, 0x80FFFFFF);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (button == 0) {
            for (IHudModule module : HudManager.INSTANCE.getModules()) {
                if (module.isEnabled()) {
                    int mx = module.getX();
                    int my = module.getY();
                    int mw = (int)(module.getWidth() * module.getScale());
                    int mh = (int)(module.getHeight() * module.getScale());

                    if (mouseX >= mx && mouseX <= mx + mw && mouseY >= my && mouseY <= my + mh) {
                        
                        if (mouseX >= mx + mw - 10 && mouseX <= mx + mw && mouseY >= my - 2 && mouseY <= my + 8) {
                            module.setEnabled(false);
                            CaeserConfig.save();
                            return true;
                        }
                        
                        if (mouseX >= mx + mw - 6 && mouseY >= my + mh - 6) {
                            scalingModule = module;
                            scaleStartX = (int)mouseX;
                            initialScale = module.getScale();
                            return true;
                        }
                        
                        draggingModule = module;
                        dragOffsetX = (int)mouseX - mx;
                        dragOffsetY = (int)mouseY - my;
                        return true;
                    }
                }
            }
        }
        return super.mouseClicked(click, bl);
    }

    public boolean mouseReleased(net.minecraft.client.gui.Click click) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (button == 0) {
            draggingModule = null;
            scalingModule = null;
        }
        return super.mouseReleased(click);
    }

    public boolean mouseDragged(net.minecraft.client.gui.Click click, double deltaX, double deltaY) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (scalingModule != null) {
            float deltaScale = (float)(mouseX - scaleStartX) / 100.0f;
            float newScale = Math.max(0.5f, Math.min(3.0f, initialScale + deltaScale));
            scalingModule.setScale(newScale);
            return true;
        }
        if (draggingModule != null) {
            int newX = (int)mouseX - dragOffsetX;
            int newY = (int)mouseY - dragOffsetY;
            int mw = (int)(draggingModule.getWidth() * draggingModule.getScale());
            int mh = (int)(draggingModule.getHeight() * draggingModule.getScale());
            
            int snapDistance = 4;
            
            if (Math.abs((newX + mw / 2) - this.width / 2) <= snapDistance) newX = this.width / 2 - mw / 2;
            if (Math.abs((newY + mh / 2) - this.height / 2) <= snapDistance) newY = this.height / 2 - mh / 2;
            
            if (Math.abs(newX) <= snapDistance) newX = 0;
            if (Math.abs(newX + mw - this.width) <= snapDistance) newX = this.width - mw;
            if (Math.abs(newY) <= snapDistance) newY = 0;
            if (Math.abs(newY + mh - this.height) <= snapDistance) newY = this.height - mh;

            for (IHudModule other : HudManager.INSTANCE.getModules()) {
                if (other.isEnabled() && other != draggingModule) {
                    int ox = other.getX();
                    int oy = other.getY();
                    int ow = (int)(other.getWidth() * other.getScale());
                    int oh = (int)(other.getHeight() * other.getScale());
                    
                    if (Math.abs(newX - ox) <= snapDistance) newX = ox;
                    if (Math.abs((newX + mw) - (ox + ow)) <= snapDistance) newX = ox + ow - mw;
                    if (Math.abs(newY - oy) <= snapDistance) newY = oy;
                    if (Math.abs((newY + mh) - (oy + oh)) <= snapDistance) newY = oy + oh - mh;
                }
            }
            
            draggingModule.setX(newX);
            draggingModule.setY(newY);
            return true;
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }

    @Override
    public void close() {
        CaeserConfig.save();
        this.client.setScreen(parent);
    }
}
