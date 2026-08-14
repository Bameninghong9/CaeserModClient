package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.hud.HudBackgroundType;
import com.caeser.mod.gui.hud.IHudModule;
import com.caeser.mod.gui.widget.CaeserButtonWidget;
import com.caeser.mod.gui.widget.ColorPickerWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.function.Supplier;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;

public class HudBackgroundCategoryScreen extends CaeserModalScreen {
    protected final IHudModule module;
    
    private final Supplier<HudBackgroundType> bgTypeGetter;
    private final Consumer<HudBackgroundType> bgTypeSetter;
    
    private final Supplier<Integer> bgColorGetter;
    private final Consumer<Integer> bgColorSetter;
    
    private final Supplier<Integer> outlineColorGetter;
    private final Consumer<Integer> outlineColorSetter;

    private ColorPickerWidget bgColorPicker;
    private ColorPickerWidget outlineColorPicker;
    
    protected int currentY = 50;
    
    private boolean isDragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public HudBackgroundCategoryScreen(Screen parent, Text title, IHudModule module,
            Supplier<HudBackgroundType> bgTypeGetter, Consumer<HudBackgroundType> bgTypeSetter,
            Supplier<Integer> bgColorGetter, Consumer<Integer> bgColorSetter,
            Supplier<Integer> outlineColorGetter, Consumer<Integer> outlineColorSetter) {
        super(parent, title);
        this.module = module;
        this.bgTypeGetter = bgTypeGetter;
        this.bgTypeSetter = bgTypeSetter;
        this.bgColorGetter = bgColorGetter;
        this.bgColorSetter = bgColorSetter;
        this.outlineColorGetter = outlineColorGetter;
        this.outlineColorSetter = outlineColorSetter;
    }

    @Override
    protected void initModal() {
        this.currentY = this.startY + 40;
        int contentX = this.startX + 10;

        // Toggle Button (ON / OFF) at top right of the panel
        this.addDrawableChild(new CaeserButtonWidget(this.startX + this.panelWidth - 50, this.startY + 10, 40, 20, Text.literal(module.isEnabled() ? "ON" : "OFF"), () -> {
            module.setEnabled(!module.isEnabled());
            CaeserConfig.save();
            this.client.setScreen(new HudBackgroundCategoryScreen(parent, title, module, bgTypeGetter, bgTypeSetter, bgColorGetter, bgColorSetter, outlineColorGetter, outlineColorSetter));
        }));
        
        addCustomWidgets(contentX);
        
        // Background Type Button
        this.addDrawableChild(new CaeserButtonWidget(contentX, currentY, 300, 20, Text.literal("Background: " + bgTypeGetter.get().name()), () -> {
            HudBackgroundType next = HudBackgroundType.values()[(bgTypeGetter.get().ordinal() + 1) % HudBackgroundType.values().length];
            bgTypeSetter.accept(next);
            CaeserConfig.save();
            this.client.setScreen(new HudBackgroundCategoryScreen(parent, title, module, bgTypeGetter, bgTypeSetter, bgColorGetter, bgColorSetter, outlineColorGetter, outlineColorSetter));
        }));
        currentY += 34; // Added 10px spacing for the ColorPicker title!
        
        // Color Pickers
        int pickerX = this.startX + (this.panelWidth - 144) / 2; // Center horizontally (144 is the width of ColorPickerWidget)
        bgColorPicker = new ColorPickerWidget(pickerX, currentY, Text.literal("Background Color"), bgColorGetter.get(), color -> {
            bgColorSetter.accept(color);
        });
        
        outlineColorPicker = new ColorPickerWidget(pickerX, currentY, Text.literal("Outline Color"), outlineColorGetter.get(), color -> {
            outlineColorSetter.accept(color);
        });
        
        this.addDrawableChild(bgColorPicker);
        this.addDrawableChild(outlineColorPicker);
        
        updateVisibility();
    }
    
    protected void addCustomWidgets(int x) {
        // Subclasses can override this to add custom widgets before the background widgets
    }

    private void updateVisibility() {
        HudBackgroundType type = bgTypeGetter.get();
        bgColorPicker.visible = (type == HudBackgroundType.COLOR);
        bgColorPicker.active = (type == HudBackgroundType.COLOR);
        
        outlineColorPicker.visible = (type == HudBackgroundType.OUTLINE);
        outlineColorPicker.active = (type == HudBackgroundType.OUTLINE);
    }
    
    @Override
    protected void renderModalBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render the module preview at its actual position, but on top of the darkened background
        if (module != null && module.isEnabled()) {
            // Render it!
            module.render(context, delta);
            
            int mx = module.getX();
            int my = module.getY();
            int mw = (int)(module.getWidth() * module.getScale());
            int mh = (int)(module.getHeight() * module.getScale());
            
            // Draw a subtle border around the module to show it's selected/draggable (yellow highlight)
            context.fill(mx - 1, my - 1, mx + mw + 1, my, 0xFFFFFF00); // Top
            context.fill(mx - 1, my + mh, mx + mw + 1, my + mh + 1, 0xFFFFFF00); // Bottom
            context.fill(mx - 1, my, mx, my + mh, 0xFFFFFF00); // Left
            context.fill(mx + mw, my, mx + mw + 1, my + mh, 0xFFFFFF00); // Right
        }
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        if (click.button() == 0 && module != null && module.isEnabled()) {
            double mouseX = click.x();
            double mouseY = click.y();
            int mx = module.getX();
            int my = module.getY();
            int mw = (int)(module.getWidth() * module.getScale());
            int mh = (int)(module.getHeight() * module.getScale());

            if (mouseX >= mx && mouseX <= mx + mw && mouseY >= my && mouseY <= my + mh) {
                isDragging = true;
                dragOffsetX = (int) mouseX - mx;
                dragOffsetY = (int) mouseY - my;
                return true;
            }
        }
        return super.mouseClicked(click, bl);
    }

    @Override
    public boolean mouseReleased(net.minecraft.client.gui.Click click) {
        if (click.button() == 0 && isDragging) {
            isDragging = false;
            CaeserConfig.save();
            return true;
        }
        return super.mouseReleased(click);
    }
    
    @Override
    public boolean mouseDragged(net.minecraft.client.gui.Click click, double deltaX, double deltaY) {
        if (isDragging && module != null) {
            int newX = (int) click.x() - dragOffsetX;
            int newY = (int) click.y() - dragOffsetY;
            
            // Constrain to screen bounds to prevent dragging off-screen
            int mw = (int)(module.getWidth() * module.getScale());
            int mh = (int)(module.getHeight() * module.getScale());
            newX = Math.max(0, Math.min(newX, this.width - mw));
            newY = Math.max(0, Math.min(newY, this.height - mh));
            
            module.setX(newX);
            module.setY(newY);
            return true;
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }

    @Override
    public void close() {
        CaeserConfig.save();
        this.client.setScreen(parent);
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
}
