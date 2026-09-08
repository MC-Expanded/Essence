package net.mcexpanded.essence.block.altar;

import net.mcexpanded.essence.registry.EssenceBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class AltarBlock extends Block implements EntityBlock
{
    public AltarBlock(BlockBehaviour.Properties properties)
    {
        super(properties
                .noOcclusion()
                .destroyTime(2)
                .lightLevel(o -> 15)
        );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return EssenceBlockEntities.ALTAR.get().create(blockPos, blockState);
    }
}
