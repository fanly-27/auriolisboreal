package com.auriolisboreal.items;

import net.minecraft.item.Item;
import com.auriolisboreal.AuriolisBorealMod;

public class ExampleItem extends Item {
    public ExampleItem() {
        setUnlocalizedName("example_item");
        setRegistryName(AuriolisBorealMod.MODID, "example_item");
        setMaxStackSize(64);
    }
}
