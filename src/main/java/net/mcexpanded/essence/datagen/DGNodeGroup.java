package net.mcexpanded.essence.datagen;

import net.mcexpanded.essence.Essence;
import net.mcexpanded.essence.registry.Position;
import net.mcexpanded.essence.registry.NodeGroup;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public class DGNodeGroup
{
    public static ResourceKey<NodeGroup> rk(String string)
    {
        return ResourceKey.create(Essence.NODE_GROUP_REGISTRY_KEY, Essence.rl(string));
    }

    public static void bootstrap(BootstrapContext<NodeGroup> context)
    {
        context.register(rk("fire"), new NodeGroup(new Position(10f, 4f), false));
    }
}
