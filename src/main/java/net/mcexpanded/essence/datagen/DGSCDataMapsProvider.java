package net.mcexpanded.essence.datagen;

import net.mcexpanded.essence.registry.Position;
import net.mcexpanded.essence.registry.EssenceDataMaps;
import net.mcexpanded.essence.registry.EssenceProperties;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DGSCDataMapsProvider extends DataMapProvider
{
    protected DGSCDataMapsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider)
    {
        var essence = this.builder(EssenceDataMaps.ESSENCE_PROPERTIES);

        essence.add(Items.LAPIS_LAZULI.builtInRegistryHolder(), new EssenceProperties(new Position(1f, 0f), List.of(), 2), false);
    }
}
