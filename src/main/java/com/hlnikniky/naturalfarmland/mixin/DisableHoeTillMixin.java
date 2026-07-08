package com.hlnikniky.naturalfarmland.mixin;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.hlnikniky.naturalfarmland.Config.HOE_TILL_STATE;


@Mixin(value = HoeItem.class, remap = false)
public class DisableHoeTillMixin {


    @Inject(
            method = "useOn",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void disableTill(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (HOE_TILL_STATE.getAsBoolean()) {
            return;
        }
        if (context.getLevel()
                .getBlockState(context.getClickedPos())
                .is(net.minecraft.tags.BlockTags.DIRT)) {

            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}