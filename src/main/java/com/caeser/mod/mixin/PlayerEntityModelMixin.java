package com.caeser.mod.mixin;

import com.caeser.mod.emote.EmoteManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public abstract class PlayerEntityModelMixin extends BipedEntityModel<PlayerEntityRenderState> {

    public PlayerEntityModelMixin(net.minecraft.client.model.ModelPart root) {
        super(root);
    }

    @Inject(method = "setAngles(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;)V", at = @At("TAIL"))
    public void onSetAngles(PlayerEntityRenderState state, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player != null && state.id == MinecraftClient.getInstance().player.getId() && EmoteManager.INSTANCE.isPlaying()) {
            float tickDelta = MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(true);

            applyBone(this.head, "head", tickDelta);
            applyBone(this.body, "body", tickDelta);
            applyBone(this.rightArm, "right_arm", tickDelta);
            applyBone(this.leftArm, "left_arm", tickDelta);
            applyBone(this.rightLeg, "right_leg", tickDelta);
            applyBone(this.leftLeg, "left_leg", tickDelta);
        }
    }

    private void applyBone(net.minecraft.client.model.ModelPart part, String boneName, float partialTicks) {
        float[] rot = com.caeser.mod.emote.PreviewHelper.previewEmoteName != null 
            ? com.caeser.mod.emote.PreviewHelper.getPreviewRotation(boneName)
            : EmoteManager.INSTANCE.getInterpolatedRotation(boneName, partialTicks);
            
        if (rot != null) {
            part.pitch = (float) Math.toRadians(rot[0]);
            part.yaw = (float) Math.toRadians(rot[1]);
            part.roll = (float) Math.toRadians(rot[2]);
        }
        
        float[] pos = com.caeser.mod.emote.PreviewHelper.previewEmoteName != null 
            ? com.caeser.mod.emote.PreviewHelper.getPreviewPosition(boneName)
            : EmoteManager.INSTANCE.getInterpolatedPosition(boneName, partialTicks);
            
        if (pos != null) {
            part.originX += pos[0];
            part.originY -= pos[1];
            part.originZ += pos[2];
        }
    }
}
