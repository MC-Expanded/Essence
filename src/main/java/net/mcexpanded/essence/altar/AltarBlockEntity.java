package net.mcexpanded.essence.altar;

import net.mcexpanded.essence.registry.EssenceBlockEntities;
import net.mcexpanded.essence.registry.SingleStackContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class AltarBlockEntity extends AbstractMultiBlockEntity implements MenuProvider
{
    private ItemStack item = ItemStack.EMPTY;
    public int tickOffset = new Random().nextInt(100000);

    public AltarBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(EssenceBlockEntities.ALTAR.get(), pos, blockState);
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
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player)
    {
        return new AltarMenu(i, inventory, new SimpleContainer(AltarMenu.CONTAINER_SIZE), this);
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
        if (!item.isEmpty()) {
            output.store("item", SingleStackContainer.CODEC, SingleStackContainer.from(item));
        }
    }

    @Override
    public Component getDisplayName()
    {
        return Component.empty();
    }

    public ItemStack getItem()
    {
        return item;
    }

    public void setItem(ItemStack item)
    {
        this.item = item;
        sync();
    }
}
