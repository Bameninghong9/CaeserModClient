package com.caeser.mod.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.world.BlockView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.caeser.mod.emote.EmoteManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @org.spongepowered.asm.mixin.Shadow protected abstract void setPos(double x, double y, double z);
    @org.spongepowered.asm.mixin.Shadow protected abstract void setRotation(float yaw, float pitch);
    
    @org.spongepowered.asm.mixin.Shadow private float yaw;
    @org.spongepowered.asm.mixin.Shadow private float pitch;
    @org.spongepowered.asm.mixin.Shadow private Vec3d pos;
    
    @Inject(method = "update", at = @At("TAIL"))
    public void onUpdate(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (focusedEntity == MinecraftClient.getInstance().player && EmoteManager.INSTANCE.isPlaying() && focusedEntity instanceof LivingEntity) {
            float[] posOffset = EmoteManager.INSTANCE.getInterpolatedPosition("head", tickDelta);
            float[] rotOffset = EmoteManager.INSTANCE.getInterpolatedRotation("head", tickDelta);
            
            if (posOffset != null && rotOffset != null) {
                LivingEntity living = (LivingEntity) focusedEntity;
                float bodyYaw = living.getBodyYaw(); // use method if field missing
                
                float xOffset = posOffset[0] / 16.0f;
                float yOffset = posOffset[1] / 16.0f; 
                float zOffset = posOffset[2] / 16.0f;
                
                float f = -bodyYaw * 0.017453292F;
                float cos = MathHelper.cos(f);
                float sin = MathHelper.sin(f);
                
                double worldX = (xOffset * cos - zOffset * sin);
                double worldZ = (xOffset * sin + zOffset * cos);
                double worldY = yOffset;
                
                this.setPos(this.pos.x - worldX, this.pos.y + worldY, this.pos.z - worldZ);
                
                if (!thirdPerson) {
                    float headPitch = rotOffset[0];
                    float headYaw = rotOffset[1];
                    this.setRotation(bodyYaw + headYaw, headPitch);
                }
            }
        }
    }
}
