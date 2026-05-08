package net.mcexpanded.essence.registry;

import net.mcexpanded.essence.Essence;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface EssenceItems
{
    DeferredRegister.Items ITEMS = DeferredRegister.createItems(Essence.MOD_ID);

    static void register(IEventBus modEventBus)
    {
        ITEMS.register(modEventBus);
    }
}
