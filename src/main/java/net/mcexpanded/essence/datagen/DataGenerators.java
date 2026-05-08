package net.mcexpanded.essence.datagen;

import net.mcexpanded.essence.Essence;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Essence.MOD_ID)
public class DataGenerators
{

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event)
    {
        DataGenerator gen = event.getGenerator();

        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        PackOutput output = gen.getPackOutput();


        event.createDatapackRegistryObjects(
                new RegistrySetBuilder()
                        .add(Essence.NODE_GROUP_REGISTRY_KEY, DGNodeGroup::bootstrap)
        );

        //data maps
        event.createProvider(DGSCDataMapsProvider::new);

        //item models
        //event.createProvider(DGSCModelProvider::new);

        //biome modifiers
        //event.createProvider(DGSCBiomeModifierProvider::new);

        //block tags
        //event.createProvider(DGSCBlocksTagsProvider::new);

        //item tags
        //event.createProvider(DGSCItemsTagsProvider::new);

        //fp tags
        //event.createProvider(DGSCFPTagsProvider::new);

        //biome tags
        //event.createProvider(DGSCBiomeTagsProvider::new);

        //advancements
        //gen.addProvider(event.includeServer(), new DGSCAdvancementProvider(output, lookupProvider, existingFileHelper));

        //loot modifiers
        //event.createProvider(DGSCLootModifiers::new);

        //loot table
        //gen.addProvider(true, new LootTableProvider(output, Collections.emptySet(),
        //        List.of(new LootTableProvider.SubProviderEntry(DGSCBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));

        //recipes
        //event.getGenerator().addProvider(true, new DGSCRecipeProvider.Runner(output, lookupProvider));
    }
}
