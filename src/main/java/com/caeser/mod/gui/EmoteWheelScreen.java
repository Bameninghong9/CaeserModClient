package com.caeser.mod.gui;

import com.caeser.mod.emote.EmoteManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class EmoteWheelScreen extends Screen {
    public EmoteWheelScreen() {
        super(Text.literal("Emotes"));
    }

    @Override
    protected void init() {
        super.init();
        int width = 120;
        int height = 20;
        int x = this.width / 2 - width / 2;
        int y = this.height / 2 - 40;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("?? Wave"), button -> {
            EmoteManager.INSTANCE.playEmote("emotes/wave.json");
            this.client.setScreen(null);
        }).dimensions(x, y, width, height).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("?? Sit (NRC)"), button -> {
            EmoteManager.INSTANCE.playEmote("emotes/new_sit.json");
            this.client.setScreen(null);
        }).dimensions(x, y + 25, width, height).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Ballett Spin"), button -> {
            EmoteManager.INSTANCE.playEmote("emotes/ballettspin.json");
            this.client.setScreen(null);
        }).dimensions(x, y + 50, width, height).build());
        
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Storytime"), button -> {
            EmoteManager.INSTANCE.playEmote("emotes/storytime.json");
            this.client.setScreen(null);
        }).dimensions(x, y + 75, width, height).build());
        
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Pray"), button -> {
            EmoteManager.INSTANCE.playEmote("emotes/pray.json");
            this.client.setScreen(null);
        }).dimensions(x, y + 100, width, height).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0x80000000);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 70, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
