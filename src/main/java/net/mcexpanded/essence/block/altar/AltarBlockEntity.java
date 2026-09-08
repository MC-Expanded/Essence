package net.mcexpanded.essence.block.altar;

import com.mojang.datafixers.util.Pair;
import net.mcexpanded.essence.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.nikdo53.tinymultiblocklib.block.IMultiBlock;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AltarBlockEntity extends AbstractMultiBlockEntity implements MenuProvider
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

    public void sync()
    {
        setChanged();

        if (level instanceof ServerLevel serverLevel)
        {
            serverLevel.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public List<Pair<AltarBlock.AltarPart, EssenceProperties>> getAllEssences()
    {
        List<Pair<AltarBlock.AltarPart, EssenceProperties>> list = new ArrayList<>();
        if (level == null) return List.of();
        List<BlockPos> fullBlockShapeCache = IMultiBlock.getFullShape(level, getBlockPos());
        fullBlockShapeCache.forEach(bp ->
        {
            if (bp.equals(getBlockPos())) return;
            if (level.getBlockEntity(bp) instanceof AltarBlockEntity abe)
            {
                ItemStack itemInAltar = abe.getItem();
                EssenceProperties essence = EssenceDataMaps.getOrDefault(itemInAltar, EssenceDataMaps.ESSENCE_PROPERTIES, EssenceProperties.EMPTY);
                if (!essence.equals(EssenceProperties.EMPTY))
                {
                    list.add(Pair.of(level.getBlockState(bp).getValue(AltarBlock.PART), essence));
                }
            }
        });
        return list;
    }

    public List<Pair<AltarBlock.AltarPart, ItemStack>> getAllEssencesItems()
    {
        List<Pair<AltarBlock.AltarPart, ItemStack>> list = new ArrayList<>();
        if (level == null) return List.of();
        List<BlockPos> fullBlockShapeCache = IMultiBlock.getFullShape(level, getBlockPos());
        fullBlockShapeCache.forEach(bp ->
        {
            if (bp.equals(getBlockPos())) return;
            if (level.getBlockEntity(bp) instanceof AltarBlockEntity abe)
            {
                if (!abe.getItem().isEmpty())
                    list.add(Pair.of(level.getBlockState(bp).getValue(AltarBlock.PART), abe.getItem()));
            }
        });
        return list;
    }


    public static Position getPositionWithOffset(Pair<AltarBlock.AltarPart, EssenceProperties> pair)
    {
        Position p = pair.getSecond().pushDirection();
        Position push = new Position(p.x(), -p.y());
        return switch (pair.getFirst())
        {
            case PEDESTAL_N -> rotateByDegrees(push, 0);
            case PEDESTAL_NE -> rotateByDegrees(push, 45);
            case PEDESTAL_E -> rotateByDegrees(push, 90);
            case PEDESTAL_SE -> rotateByDegrees(push, 135);
            case PEDESTAL_S -> rotateByDegrees(push, 180);
            case PEDESTAL_SW -> rotateByDegrees(push, 225);
            case PEDESTAL_W -> rotateByDegrees(push, 270);
            case PEDESTAL_NW -> rotateByDegrees(push, 315);

            default -> push;
        };
    }

    private static Position rotateByDegrees(Position pos, int degrees)
    {
        double angle = Math.toRadians(degrees);
        double rotatedX = pos.x() * Math.cos(angle) - pos.y() * Math.sin(angle);
        double rotatedY = pos.x() * Math.sin(angle) + pos.y() * Math.cos(angle);
        return new Position((float) rotatedX, (float) rotatedY);
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
        this.seed = input.getLongOr("seed", 0);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.item = input.read("item", SingleStackContainer.CODEC).orElse(SingleStackContainer.empty()).create();
        this.seed = input.getLongOr("seed", 0);
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

    public static void tick(Level level, BlockPos worldPosition, BlockState blockState, AltarBlockEntity entity)
    {
        entity.playerCloseOld = entity.playerClose;
        Player player = level.getNearestPlayer(
                worldPosition.getX() + 0.5,
                worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5,
                2.5f, false);
        if (player != null)
            entity.playerClose += 0.1F;
        else
            entity.playerClose -= 0.1F;

        entity.playerClose = Mth.clamp(entity.playerClose, 0.0F, 1.0F);

    }
}
