package net.mcexpanded.essence.block.altar;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.mcexpanded.essence.registry.EssenceBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class AltarRenderer implements BlockEntityRenderer<AltarBlockEntity, AltarRenderState>
{

    private final BlockModelResolver blockModelResolver;
    private final ItemModelResolver itemModelResolver;

    public AltarRenderer(BlockEntityRendererProvider.Context context)
    {
        this.blockModelResolver = context.blockModelResolver();
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public AltarRenderState createRenderState()
    {
        return new AltarRenderState();
    }

    @Override
    public void submit(AltarRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        if (!state.part.equals(AltarBlock.AltarPart.ALTAR)) return;
        poseStack.pushPose();

        poseStack.scale(0.7f, 0.7f, 0.7f);
        poseStack.translate(0.7f, 1.4f + (Math.sin(Util.getMillis() / 555f) / 60), 0.7f);
        poseStack.translate(0f, 0.4f * state.playerClose, 0f);

        float x = (float) (Math.sin(Util.getMillis() / 2000f + 323) * 20f);
        float y = (float) (Math.sin(Util.getMillis() / 2000f) * 20f);

        //System.out.println(state.playerClose);

        poseStack.mulPose(Axis.XP.rotationDegrees(x + (1f - state.playerClose) * 90f));
        poseStack.mulPose(Axis.YP.rotationDegrees(y));
        poseStack.mulPose(Axis.ZP.rotationDegrees((float) (Math.toRadians(Util.getMillis() % 360f) / 600f)));


        if (!state.item.isEmpty())
        {
            int lightVal = state.lightCoords;
            state.item.submit(poseStack, submitNodeCollector, lightVal, OverlayTexture.NO_OVERLAY, 0);
        }
        poseStack.popPose();
    }

    @Override
    public void extractRenderState(AltarBlockEntity be, AltarRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(be, state, partialTicks, cameraPosition, breakProgress);
        ItemStack itemStack = be.getItem();
        this.itemModelResolver.updateForNonLiving(state.item, itemStack, ItemDisplayContext.FIXED, Minecraft.getInstance().player);
        this.blockModelResolver.updateForItemFrame(state.frameModel, false, false);
        state.offset = be.tickOffset;
        state.playerClose = Mth.lerp(partialTicks, be.playerCloseOld, be.playerClose);
        if (be.getLevel().getBlockState(be.getBlockPos()).is(EssenceBlocks.ALTAR))
            state.part = be.getLevel().getBlockState(be.getBlockPos()).getValue(AltarBlock.PART);
        else
            state.part = AltarBlock.AltarPart.ALTAR;
    }
}
