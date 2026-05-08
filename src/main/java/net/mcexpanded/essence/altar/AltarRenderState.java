package net.mcexpanded.essence.altar;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class AltarRenderState extends BlockEntityRenderState
{
    public final BlockModelRenderState frameModel = new BlockModelRenderState();
    public final ItemStackRenderState item = new ItemStackRenderState();
    public int offset;
}
