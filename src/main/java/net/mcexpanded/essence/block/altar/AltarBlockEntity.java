package net.mcexpanded.essence.block.altar;

import net.mcexpanded.essence.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Random;

public class AltarBlockEntity extends BlockEntity
{
    private ItemStack item = ItemStack.EMPTY;
    public long seed = 0;
    public int tickOffset = new Random().nextInt(100000);
    public float playerClose = 0;
    public float playerCloseOld = 0;

    public AltarBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(EssenceBlockEntities.ALTAR.get(), pos, blockState);
        if (level instanceof ServerLevel sl) seed = sl.getSeed();
        sync();
    }

    public ItemStack getItem()
    {
        return item;
    }

    public void sync()
    {
        setChanged();

        if (level instanceof ServerLevel serverLevel)
        {
            serverLevel.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        return saveWithFullMetadata(registries);
    }

    @Override
    public void handleUpdateTag(ValueInput input)
    {
        super.handleUpdateTag(input);
        this.item = input.read("item", SingleStackContainer.CODEC).orElse(SingleStackContainer.empty()).create();
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.item = input.read("item", SingleStackContainer.CODEC).orElse(SingleStackContainer.empty()).create();
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        if (!item.isEmpty())
        {
            output.store("item", SingleStackContainer.CODEC, SingleStackContainer.from(item));
        }
        output.putLong("seed", seed);
    }
}
