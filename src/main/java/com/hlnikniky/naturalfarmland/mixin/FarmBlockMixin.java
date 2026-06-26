package com.hlnikniky.naturalfarmland.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmBlock.class)
public class FarmBlockMixin {

    // Отменяем randomTick — своя логика влажности
    @Inject(
            method = "randomTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void onRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        int moisture = state.getValue(FarmBlock.MOISTURE);
        boolean isRaining = level.isRaining() && level.canSeeSky(pos.above());

        if (isRaining) {
            if (moisture < FarmBlock.MAX_MOISTURE) {
                level.setBlock(pos, state.setValue(FarmBlock.MOISTURE, moisture + 1), 2);
            }
        } else if (hasWaterNearby(level, pos)) {
            if (moisture > 0) {
                level.setBlock(pos, state.setValue(FarmBlock.MOISTURE, moisture - 1), 2);
            }
        }
        ci.cancel(); // всегда отменяем — никакого turnToDirt из randomTick
    }

    // Отменяем tick — он вызывается когда блок сверху стал непрозрачным
    @Inject(
            method = "tick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void onTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        ci.cancel(); // блокируем turnToDirt при постановке блока сверху
    }

    // Отменяем затаптывание
    @Inject(
            method = "fallOn(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;F)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void onFallOn(net.minecraft.world.level.Level level, BlockState state, BlockPos pos, net.minecraft.world.entity.Entity entity, float fallDistance, CallbackInfo ci) {
        ci.cancel();
    }

    private static boolean hasWaterNearby(ServerLevel level, BlockPos pos) {
        for (BlockPos nearby : BlockPos.betweenClosed(
                pos.offset(-4, 0, -4),
                pos.offset(4, 1, 4))) {
            if (level.getFluidState(nearby).is(net.minecraft.tags.FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }
}