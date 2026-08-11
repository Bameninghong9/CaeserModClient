package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.widget.CaeserToggleWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class CaeserSettingsScreen extends Screen {
    private final Screen parent;
    private int currentCategory = 0; // 0 = All, 1 = Combat, 2 = Chat
    private final String[] categories = {"All", "Combat", "Chat"};
    
    private TextFieldWidget searchField;
    private String searchQuery = "";

    public CaeserSettingsScreen(Screen parent) {
        super(Text.literal("Caeser Client Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        
        int panelWidth = 450;
        int panelHeight = 300;
        int startX = (this.width - panelWidth) / 2;
        int startY = (this.height - panelHeight) / 2;
        
        this.clearChildren();

        this.searchField = new TextFieldWidget(this.textRenderer, startX + panelWidth - 160, startY + 2, 150, 20, Text.literal("Search"));
        this.searchField.setText(this.searchQuery);
        this.searchField.setChangedListener(text -> {
            this.searchQuery = text.toLowerCase();
            this.rebuildWidgets();
        });
        this.addDrawableChild(this.searchField);

        this.rebuildWidgets();
    }

    private void rebuildWidgets() {
        // Clear all except searchField
        this.clearChildren();
        this.addDrawableChild(this.searchField);

        int panelWidth = 450;
        int panelHeight = 300;
        int startX = (this.width - panelWidth) / 2;
        int startY = (this.height - panelHeight) / 2;
        
        int contentY = startY + 24;
        int contentX = startX + 10;
        int widgetY = contentY + 10;

        boolean showAll = currentCategory == 0;
        boolean showCombat = currentCategory == 1;
        boolean showChat = currentCategory == 2;

        // Combat modules
        if (showAll || showCombat) {
            if ("hitboxes".contains(searchQuery)) {
                this.addDrawableChild(new CaeserToggleWidget(
                    contentX, widgetY, panelWidth - 20, 20, 
                    Text.literal("Hitboxes"),
                    () -> CaeserConfig.INSTANCE.hitboxes,
                    val -> { CaeserConfig.INSTANCE.hitboxes = val; CaeserConfig.save(); },
                    () -> MinecraftClient.getInstance().setScreen(new HitboxCategoryScreen(this))
                ));
                widgetY += 24;
            }

            if ("hitcolor".contains(searchQuery)) {
                this.addDrawableChild(new CaeserToggleWidget(
                    contentX, widgetY, panelWidth - 20, 20, 
                    Text.literal("HitColor"),
                    () -> CaeserConfig.INSTANCE.hitColors,
                    val -> { CaeserConfig.INSTANCE.hitColors = val; CaeserConfig.save(); },
                    () -> MinecraftClient.getInstance().setScreen(new HitColorCategoryScreen(this))
                ));
                widgetY += 24;
            }
        }

        // Chat modules
        if (showAll || showChat) {
            // Chat settings (empty for now)
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0x80030712);

        int panelWidth = 450;
        int panelHeight = 300;
        int startX = (this.width - panelWidth) / 2;
        int startY = (this.height - panelHeight) / 2;

        context.fill(startX, startY + 24, startX + panelWidth, startY + panelHeight, 0xB20A0F1D);
        context.fill(startX, startY, startX + panelWidth, startY + 24, 0xFF0F172A);

        int tabWidth = 80;
        for (int i = 0; i < categories.length; i++) {
            int tabX = startX + (i * tabWidth);
            int textColor = 0xFFAAAAAA;
            
            if (mouseX >= tabX && mouseX <= tabX + tabWidth && mouseY >= startY && mouseY <= startY + 24) {
                context.fill(tabX, startY, tabX + tabWidth, startY + 24, 0xFF1E293B);
                textColor = 0xFFFFFFFF;
            }
            
            if (currentCategory == i) {
                context.fill(tabX, startY, tabX + tabWidth, startY + 24, 0xFF3B82F6);
                textColor = 0xFFFFFFFF;
            }

            int textWidth = this.textRenderer.getWidth(categories[i]);
            context.drawTextWithShadow(this.textRenderer, Text.literal(categories[i]), tabX + (tabWidth - textWidth) / 2, startY + 8, textColor);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        double mouseX = click.x();
        double mouseY = click.y();
        int panelWidth = 450;
        int panelHeight = 300;
        int startX = (this.width - panelWidth) / 2;
        int startY = (this.height - panelHeight) / 2;

        int tabWidth = 80;

        if (mouseY >= startY && mouseY <= startY + 24) {
            for (int i = 0; i < categories.length; i++) {
                int tabX = startX + (i * tabWidth);
                if (mouseX >= tabX && mouseX <= tabX + tabWidth) {
                    this.currentCategory = i;
                    this.init();
                    return true;
                }
            }
        }
        
        return super.mouseClicked(click, bl);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}
