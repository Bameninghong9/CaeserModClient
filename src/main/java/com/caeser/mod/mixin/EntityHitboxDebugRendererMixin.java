package com.caeser.mod.mixin;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.render.debug.EntityHitboxDebugRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityHitboxDebugRenderer.class)
public class EntityHitboxDebugRendererMixin {

    @Inject(method = "drawHitbox", at = @At("HEAD"), cancellable = true)
    private void onDrawHitbox(Entity entity, float tickDelta, boolean showLocalServer, CallbackInfo ci) {
        if (CaeserConfig.INSTANCE.hitboxes) {
            ci.cancel();

            // Determine if entity is a player
            boolean isPlayer = entity instanceof net.minecraft.entity.player.PlayerEntity;

            // Determine if entity is hovered
            boolean isHovered = false;
            net.minecraft.client.MinecraftClient client = net.minecraft.client.MinecraftClient.getInstance();
            if (client.crosshairTarget != null && client.crosshairTarget.getType() == net.minecraft.util.hit.HitResult.Type.ENTITY) {
                if (((net.minecraft.util.hit.EntityHitResult) client.crosshairTarget).getEntity() == entity) {
                    isHovered = true;
                }
            }

            int color;
            float thickness;
            if (isPlayer) {
                color = isHovered ? CaeserConfig.INSTANCE.playerHitboxHoverColor : CaeserConfig.INSTANCE.playerHitboxColor;
                thickness = CaeserConfig.INSTANCE.playerHitboxThickness;
            } else {
                color = isHovered ? CaeserConfig.INSTANCE.mobHitboxHoverColor : CaeserConfig.INSTANCE.mobHitboxColor;
                thickness = CaeserConfig.INSTANCE.mobHitboxThickness;
            }

            double d = entity.getX() - entity.lastRenderX;
            double e = entity.getY() - entity.lastRenderY;
            double f = entity.getZ() - entity.lastRenderZ;
            double x = entity.lastRenderX + d * (double)tickDelta;
            double y = entity.lastRenderY + e * (double)tickDelta;
            double z = entity.lastRenderZ + f * (double)tickDelta;

            Box box = entity.getBoundingBox().offset(-entity.getX() + x, -entity.getY() + y, -entity.getZ() + z);
            
            
            
            // Draw custom hitbox
            GizmoDrawing.box(box, DrawStyle.stroked(color, thickness));
        }
    }
}
