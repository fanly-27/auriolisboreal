package com.auriolisboreal;

import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = AuriolisBorealMod.MODID, value = Side.CLIENT)
public class ClientColors {
    private static final int PINK = 0xFF8CC8;

    @SubscribeEvent
    public static void registerBlockColors(ColorHandlerEvent.Block event) {
        event.getBlockColors().registerBlockColorHandler(
                (state, world, pos, tintIndex) -> PINK,
                ModBlocks.arboris_aura_leaves, ModBlocks.arboris_aura_grass, ModBlocks.arboris_aura_tallgrass);
    }

    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item event) {
        event.getItemColors().registerItemColorHandler(
                (stack, tintIndex) -> PINK,
                ModBlocks.arboris_aura_leaves, ModBlocks.arboris_aura_grass, ModBlocks.arboris_aura_tallgrass);
    }
}
