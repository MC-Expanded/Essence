package net.mcexpanded.essence;

import net.mcexpanded.essence.registry.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(Essence.MOD_ID)
public class Essence
{
    public static final String MOD_ID = "essence";

    public static final ResourceKey<Registry<NodeGroup>> NODE_GROUP_REGISTRY_KEY =
            ResourceKey.createRegistryKey(rl("node_group"));

    public static Identifier rl(String ns, String path)
    {
        return Identifier.fromNamespaceAndPath(ns, path);
    }

    public static Identifier rl(String path)
    {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public Essence(IEventBus modEventBus, ModContainer modContainer)
    {
        EssenceItems.register(modEventBus);
        EssenceBlocks.register(modEventBus);
        EssenceBlockEntities.register(modEventBus);
        EssenceMenuTypes.register(modEventBus);
    }
}
