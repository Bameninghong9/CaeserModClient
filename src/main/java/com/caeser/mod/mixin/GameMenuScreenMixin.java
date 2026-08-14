package com.caeser.mod.mixin;

import com.caeser.mod.gui.CaeserMainMenuScreen;
//? if <=1.21.11 {
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
//?} else {
/*import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
*///?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if <=1.21.11 {
@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    protected GameMenuScreenMixin(Text title) {
//?} else {
/*@Mixin(PauseScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    protected GameMenuScreenMixin(Component title) {
*///?}
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void addCaeserButton(CallbackInfo ci) {
        // Add button in the top right corner or a custom position without shifting vanilla buttons
        //? if <=1.21.11 {
        this.addDrawableChild(ButtonWidget.builder(Text.literal("C"), button -> {
            this.client.setScreen(new CaeserMainMenuScreen(this));
        }).dimensions(this.width - 24, 4, 20, 20).build());
        //?} else {
        /*this.addRenderableWidget(Button.builder(Component.literal("C"), button -> {
            this.minecraft.setScreen(new CaeserMainMenuScreen(this));
        }).bounds(this.width - 24, 4, 20, 20).build());
        *///?}
    }
}
