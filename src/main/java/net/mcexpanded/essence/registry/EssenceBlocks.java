package net.mcexpanded.essence.registry;

import net.mcexpanded.essence.Essence;
import net.mcexpanded.essence.block.altar.AltarBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public interface EssenceBlocks
{
    DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Essence.MOD_ID);

    DeferredBlock<Block> ALTAR = registerBlock("altar", AltarBlock::new);

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends T> block)
    {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, block);
        EssenceItems.ITEMS.registerItem(name, (p) -> new BlockItem(toReturn.get(), p
                .useBlockDescriptionPrefix()
                .setId(ResourceKey.create(Registries.ITEM, Essence.rl(name)))));
        return toReturn;
    }

    static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}
