package net.mcexpanded.essence.registry;

import net.mcexpanded.essence.Essence;
import net.mcexpanded.essence.altar.AltarBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface EssenceBlockEntities
{
     DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Essence.MOD_ID);

     Supplier<BlockEntityType<AltarBlockEntity>> STAND = BLOCK_ENTITIES.register("stand",
            () -> new BlockEntityType<>(AltarBlockEntity::new, EssenceBlocks.ALTAR.get()));

    static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
