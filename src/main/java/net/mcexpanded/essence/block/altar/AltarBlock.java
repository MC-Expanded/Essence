package net.mcexpanded.essence.block.altar;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class AltarBlock extends Block
{
    public AltarBlock(BlockBehaviour.Properties properties)
    {
        super(properties
                .noOcclusion()
                .destroyTime(2)
        );
    }

}
