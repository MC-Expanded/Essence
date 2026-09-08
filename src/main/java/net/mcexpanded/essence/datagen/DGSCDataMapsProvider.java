package net.mcexpanded.essence.datagen;

import net.mcexpanded.essence.registry.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
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
    }
}
