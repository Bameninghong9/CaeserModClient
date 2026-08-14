package com.caeser.mod.mixin;

import com.caeser.mod.gui.hud.CpsModule;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void onMouseButtonHead(long window, net.minecraft.client.input.MouseInput input, int action, CallbackInfo ci) {
        if (input.button() == 0 && action == 1) { // Left click pressed
            CpsModule.addClick();
        }
    }
}
