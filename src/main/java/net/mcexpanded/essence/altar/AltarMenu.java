package net.mcexpanded.essence.altar;

import net.mcexpanded.essence.registry.EssenceMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AltarMenu extends AbstractContainerMenu
{
    private final Container container;
    public static final int CONTAINER_SIZE = 1;
    public final AltarBlockEntity be;

    public AltarMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData)
    {
        this(containerId, playerInventory, new SimpleContainer(CONTAINER_SIZE), playerInventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public AltarMenu(int containerId, Inventory playerInventory, Container container, BlockEntity blockEntity)
    {
        super(EssenceMenuTypes.ALTAR_MENU.get(), containerId);
        checkContainerSize(container, CONTAINER_SIZE);
        this.container = container;
        this.be = ((AltarBlockEntity) blockEntity);
        container.startOpen(playerInventory.player);


        //this.addSlot(new TackleBoxRodSlot(this, container, ROD_SLOT, 134, 37));

//        for (int i1 = 0; i1 < 3; ++i1)
//            for (int k1 = 0; k1 < 9; ++k1)
//                this.addSlot(new Slot(playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 84 + i1 * 18));
//
//        for (int j1 = 0; j1 < 9; ++j1)
//            this.addSlot(new Slot(playerInventory, j1, 8 + j1 * 18, 142));

    }

    public boolean stillValid(Player player)
    {
        return this.container.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem())
        {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.container.getContainerSize())
            {
                if (!this.moveItemStackTo(itemstack1, this.container.getContainerSize(), this.slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(itemstack1, 0, this.container.getContainerSize(), false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
                slot.setByPlayer(ItemStack.EMPTY);
            else
                slot.setChanged();
        }

        return itemstack;
    }

    public void removed(Player player)
    {
        super.removed(player);
        this.container.stopOpen(player);
    }
}
