package net.mcexpanded.essence;

import net.mcexpanded.essence.registry.EssenceDataMaps;
import net.mcexpanded.essence.registry.EnchantmentNodeGroup;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = Essence.MOD_ID)
public class EssenceEvents
{
    @SubscribeEvent
    public static void datapackRegistry(DataPackRegistryEvent.NewRegistry event)
    {
        event.dataPackRegistry(
                Essence.NODE_GROUP_REGISTRY_KEY,
                EnchantmentNodeGroup.CODEC,
                EnchantmentNodeGroup.CODEC
        );
    }

    @SubscribeEvent
    public static void registerAttributed(RegisterDataMapTypesEvent event)
    {
        event.register(EssenceDataMaps.ESSENCE_PROPERTIES);
        event.register(EssenceDataMaps.ENCHANT_PROPERTIES);
    }

}
