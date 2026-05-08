package net.mcexpanded.essence.altar;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
        poseStack.pushPose();

        float xRot = 0;
        float yRot = (float) (Util.getMillis() + state.offset) / 100;

        poseStack.scale(0.7f, 0.7f, 0.7f);
        poseStack.translate(0.7f, 2.4f + (Math.sin(yRot / 10) / 6), 0.7f);

        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));


        if (!state.item.isEmpty())
        {
            int lightVal = state.lightCoords;
            state.item.submit(poseStack, submitNodeCollector, lightVal, OverlayTexture.NO_OVERLAY, 0);
        }
        poseStack.popPose();
    }

    @Override
    public void extractRenderState(AltarBlockEntity blockEntity, AltarRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        ItemStack itemStack = blockEntity.getItem();
        this.itemModelResolver.updateForNonLiving(state.item, itemStack, ItemDisplayContext.FIXED, Minecraft.getInstance().player);
        this.blockModelResolver.updateForItemFrame(state.frameModel, false, false);
        state.offset = blockEntity.tickOffset;
    }
}
