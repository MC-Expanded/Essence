package net.mcexpanded.essence.altar;

import net.mcexpanded.essence.registry.EssenceBlockEntities;
import net.mcexpanded.essence.registry.EssenceDataMaps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.nikdo53.tinymultiblocklib.block.AbstractMultiBlock;
import net.nikdo53.tinymultiblocklib.block.IMultiBlock;
import net.nikdo53.tinymultiblocklib.block.IPreviewableMultiblock;
import net.nikdo53.tinymultiblocklib.components.IBlockPosOffsetEnum;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class AltarBlock extends AbstractMultiBlock implements IPreviewableMultiblock
{
    public static final EnumProperty<AltarPart> PART = EnumProperty.create("part", AltarPart.class);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    public AltarBlock(BlockBehaviour.Properties properties)
    {
        super(properties
                .noOcclusion()
                .destroyTime(2)
        );
        registerDefaultState(defaultBlockState().setValue(PART, AltarPart.ALTAR));
    }

    @Override
    public List<BlockPos> makeFullBlockShape(Level level, BlockPos center, BlockState blockState, @org.jetbrains.annotations.Nullable BlockEntity blockEntity, @org.jetbrains.annotations.Nullable Direction direction)
    {
        assert direction != null;
        return List.of(
                center,
                center.north().north().north().north(),
                center.north().north().north().east().east().east(),
                center.east().east().east().east(),
                center.south().south().south().east().east().east(),
                center.south().south().south().south(),
                center.south().south().south().west().west().west(),
                center.west().west().west().west(),
                center.north().north().north().west().west().west()
        );
    }

    @Override
    public RenderShape getMultiblockRenderShape(BlockState state, boolean isCenter)
    {
        return RenderShape.MODEL;
    }

    @Override
    public @org.jetbrains.annotations.Nullable EnumProperty<Direction> getDirectionProperty()
    {
        return FACING;
    }

    @Override
    public BlockState getStateForEachBlock(BlockState state, BlockPos pos, BlockPos centerOffset, Level level, @org.jetbrains.annotations.Nullable Direction direction)
    {
        state = state.setValue(PART, IBlockPosOffsetEnum.fromOffset(AltarPart.class, centerOffset, Direction.NORTH, AltarPart.ALTAR));

        return state;
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return getStateForPlacementHelper(context, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState getDefaultStateForPreviews(Direction direction)
    {
        return IPreviewableMultiblock.super.getDefaultStateForPreviews(direction.getOpposite());
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        BlockPos center = IMultiBlock.getCenter(level, pos);
        if (level.getBlockEntity(pos) instanceof AltarBlockEntity abe)
        {
            //altar
            if (center.equals(pos))
            {
                //remove item
                if (!abe.getItem().isEmpty() && player.isCrouching())
                {
                    player.addItem(abe.getItem());
                    abe.setItem(ItemStack.EMPTY);
                    return InteractionResult.CONSUME;
                }

                //place item
                if (abe.getItem().isEmpty() && !player.isCrouching())
                {
                    abe.setItem(itemStack);
                    player.setItemInHand(hand, ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }

                //open screen
                if (!abe.getItem().isEmpty() && !player.isCrouching())
                {
                    player.openMenu(new SimpleMenuProvider(abe, Component.empty()), center);
                    return InteractionResult.SUCCESS;
                }

            }
            //pedestals
            else
            {
                //pick up item from pedestal
                if (itemStack.isEmpty() && !abe.getItem().isEmpty())
                {
                    player.setItemInHand(hand, abe.getItem());
                    abe.setItem(ItemStack.EMPTY);
                    return InteractionResult.CONSUME;
                }

                //place item on pedestal
                if (!itemStack.isEmpty() && abe.getItem().isEmpty())
                {
                    if(EssenceDataMaps.getOrDefault(itemStack, EssenceDataMaps.ESSENCE_PROPERTIES, null) != null)
                    {
                        abe.setItem(itemStack);
                        player.setItemInHand(hand, ItemStack.EMPTY);
                        return InteractionResult.CONSUME;
                    }
                    else
                    {
                        player.sendOverlayMessage(Component.literal("The pedestal rejects this item"));
                    }
                }
            }
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        //the facing gets added automatically by the lib
        builder.add(PART);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return EssenceBlockEntities.ALTAR.get().create(blockPos, blockState);
    }

    @Override
    public boolean hasCustomBE()
    {
        return true;
    }

    @Override
    public @org.jspecify.annotations.Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type)
    {
        return super.getTicker(level, blockState, type);
    }

    public enum AltarPart implements StringRepresentable, IBlockPosOffsetEnum
    {
        ALTAR("altar", pos -> pos),
        PEDESTAL_N("north", pos -> pos.north().north().north().north()),
        PEDESTAL_NE("north_east", pos -> pos.north().north().north().east().east().east()),
        PEDESTAL_E("east", pos -> pos.east().east().east().east()),
        PEDESTAL_SE("south_east", pos -> pos.south().south().south().east().east().east()),
        PEDESTAL_S("south", pos -> pos.south().south().south().south()),
        PEDESTAL_SW("south_west", pos -> pos.south().south().south().west().west().west()),
        PEDESTAL_W("west", pos -> pos.west().west().west().west()),
        PEDESTAL_NW("north_west", pos -> pos.north().north().north().west().west().west()),
        ;

        private final String name;
        public final Function<BlockPos, BlockPos> offset;

        AltarPart(String name, Function<BlockPos, BlockPos> offset)
        {
            this.name = name;
            this.offset = offset;
        }

        public String toString()
        {
            return this.name;
        }

        @Override
        public String getSerializedName()
        {
            return this.name;
        }

        @Override
        public Function<BlockPos, BlockPos> getOffsetFunction()
        {
            return offset;
        }
    }
}
