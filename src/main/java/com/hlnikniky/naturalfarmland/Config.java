package com.hlnikniky.naturalfarmland;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.BooleanValue HYDRATE_FARMLAND_WATER_BLOCK_STATE = BUILDER
            .comment("Hydrate farmland with water block")
            .define("hydrate_farmland_water_block", false);
    public static final ModConfigSpec.BooleanValue HOE_TILL_STATE = BUILDER
            .comment("Hoe can till dirt blocks")
            .define("hoe_till_enable", false);
    public static final ModConfigSpec.BooleanValue FARMLAND_CAN_SURVIVE_WITHOUT_WATER_STATE = BUILDER
            .comment("Farmland can survive without water")
            .define("farmland_survive_without water", true);
    public static final ModConfigSpec.BooleanValue FARMLAND_CAN_SURVIVE_FROM_PLACING_BLOCK_STATE = BUILDER
            .comment("Farmland can survive from placing block on it")
            .define("farmland_survive_from_placing_block", true);
    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}
