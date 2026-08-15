package com.caeser.mod.emote;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.math.Vec3d;

public class GamingChairSpawner {
    private static ArmorStandEntity chairEntity = null;

    public static void spawn(MinecraftClient client) {
        if (client.world != null && client.player != null && chairEntity == null) {
            chairEntity = new ArmorStandEntity(client.world, client.player.getX(), client.player.getY() - 0.9, client.player.getZ());
            chairEntity.setId(Integer.MAX_VALUE - 1000);
            chairEntity.setInvisible(true);
            chairEntity.setNoGravity(true);
            chairEntity.setYaw(client.player.getYaw());
            chairEntity.equipStack(EquipmentSlot.HEAD, new ItemStack(com.caeser.mod.CaeserMod.GAMING_CHAIR_ITEM));
            client.world.addEntity(chairEntity);
        }
    }

    public static void tick(MinecraftClient client) {
        if (client.player != null && chairEntity != null) {
            chairEntity.setPosition(client.player.getX(), client.player.getY() - 0.9, client.player.getZ());
            chairEntity.setYaw(client.player.getBodyYaw());
            chairEntity.setBodyYaw(client.player.getBodyYaw());
            chairEntity.setHeadYaw(client.player.getBodyYaw());
        }
    }

    public static void remove(MinecraftClient client) {
        if (client.world != null && chairEntity != null) {
            client.world.removeEntity(chairEntity.getId(), net.minecraft.entity.Entity.RemovalReason.DISCARDED);
            chairEntity = null;
        }
    }
}

