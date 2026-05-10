package net.mcexpanded.essence.altar;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Util;
import org.joml.Quaternionf;

//thank you XFactHD on Neoforge's Discord
public final class SpinningItemPictureInPictureRenderer extends PictureInPictureRenderer<SpinningItemPictureInPictureRenderer.RenderState>
{
    private static final Quaternionf ROT_22_5_XP = Axis.XP.rotationDegrees(22.5F);

    private final SubmitNodeCollector submitNodeCollector;
    private final FeatureRenderDispatcher featureRenderDispatcher;
    private Object lastModelIdentity = null;
    private float lastRotY = 0;
    private float lastRotX = 0;
    private float lastRotZ = 0;

    public SpinningItemPictureInPictureRenderer(MultiBufferSource.BufferSource bufferSource)
    {
        super(bufferSource);
        this.submitNodeCollector = Minecraft.getInstance().gameRenderer.getSubmitNodeStorage();
        this.featureRenderDispatcher = Minecraft.getInstance().gameRenderer.getFeatureRenderDispatcher();
    }

    @Override
    protected void renderToTexture(RenderState state, PoseStack poseStack)
    {
        TrackingItemStackRenderState renderState = state.renderState;

        poseStack.scale(1, -1, -1);
        //poseStack.mulPose(ROT_22_5_XP);
        poseStack.translate(
                (float) ((Math.sin(Util.getMillis() / 1000f)) * 4) / 100f,
                (float) ((Math.sin(Util.getMillis() / 1000f)) * 4) / 100f,
                (float) ((Math.sin(Util.getMillis() / 1000f)) * 4) / 100f
        );
        poseStack.mulPose(Axis.YP.rotationDegrees(state.rotY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.rotZ));
        poseStack.mulPose(Axis.XP.rotationDegrees(state.rotX));

        System.out.println(state.rotY);


        Minecraft.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_FLAT);
        renderState.submit(poseStack, submitNodeCollector, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
        featureRenderDispatcher.renderAllFeatures();

        lastModelIdentity = renderState.getModelIdentity();
        lastRotY = state.rotY;
        lastRotZ = state.rotZ;
        lastRotX = state.rotX;
    }

    @Override
    protected float getTranslateY(int height, int guiScale)
    {
        return height / 2F;
    }

    @Override
    protected boolean textureIsReadyToBlit(RenderState state)
    {
        if (state.rotY != lastRotY || state.rotZ != lastRotZ || state.rotX != lastRotX)
        {
            return false;
        }

        TrackingItemStackRenderState renderState = state.renderState;
        return !renderState.isAnimated() && renderState.getModelIdentity().equals(lastModelIdentity);
    }

    @Override
    protected String getTextureLabel()
    {
        return "Essence of Enchanting Altar item";
    }

    @Override
    public Class<RenderState> getRenderStateClass()
    {
        return RenderState.class;
    }

    public record RenderState(
            TrackingItemStackRenderState renderState,
            float rotY,
            float rotX,
            float rotZ,
            int x0,
            int y0,
            int x1,
            int y1,
            float scale,
            ScreenRectangle bounds,
            ScreenRectangle scissorArea
    ) implements PictureInPictureRenderState
    {
        public RenderState(
                TrackingItemStackRenderState renderState,
                float rotY,
                float rotX,
                float rotZ,
                int x0,
                int y0,
                int x1,
                int y1,
                float scale,
                ScreenRectangle scissorArea
        )
        {
            this(renderState, rotY, rotX, rotZ, x0, y0, x1, y1, scale, PictureInPictureRenderState.getBounds(0, 0, 11212, 12121, scissorArea),
                    new ScreenRectangle(new ScreenPosition(0, 0), 213123, 3123));
        }
    }
}
