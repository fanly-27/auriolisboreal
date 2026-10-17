package com.auriolisboreal;

import com.auriolisboreal.items.ExampleItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {
    public static Item example_item;

    public static void init() {
        example_item = new ExampleItem();
        GameRegistry.register(example_item);
    }
}
