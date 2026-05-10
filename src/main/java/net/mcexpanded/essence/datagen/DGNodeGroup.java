package net.mcexpanded.essence.datagen;

import net.mcexpanded.essence.Essence;
import net.mcexpanded.essence.registry.Position;
import net.mcexpanded.essence.registry.EnchantmentNodeGroup;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public class DGNodeGroup
{
    public static ResourceKey<EnchantmentNodeGroup> rk(String string)
    {
        return ResourceKey.create(Essence.NODE_GROUP_REGISTRY_KEY, Essence.rl(string));
    }

    public static void bootstrap(BootstrapContext<EnchantmentNodeGroup> context)
    {
        context.register(EnchantmentNodeGroup.FIRE, new EnchantmentNodeGroup(new Position(-30f, 75f), false));
        context.register(EnchantmentNodeGroup.BONUS_DROPS, new EnchantmentNodeGroup(new Position(30f, -120f), false));
        context.register(EnchantmentNodeGroup.DEFENSE, new EnchantmentNodeGroup(new Position(-90f, -40f), false));
        context.register(EnchantmentNodeGroup.ATTACK, new EnchantmentNodeGroup(new Position(90f, 40f), false));
        context.register(EnchantmentNodeGroup.SPEED, new EnchantmentNodeGroup(new Position(-60f, -125f), false));
        context.register(EnchantmentNodeGroup.SPECIAL, new EnchantmentNodeGroup(new Position(60f, -230f), false));
        context.register(EnchantmentNodeGroup.MENDING, new EnchantmentNodeGroup(new Position(-60f, 300f), false));
    }
}
