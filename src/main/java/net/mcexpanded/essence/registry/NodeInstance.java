package net.mcexpanded.essence.registry;

import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

public record NodeInstance
        (
                float x,
                float z,
                Holder<Enchantment> enchantment,
                boolean available,
                int level
        )
{


}