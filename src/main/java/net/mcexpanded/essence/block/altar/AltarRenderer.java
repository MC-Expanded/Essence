package net.mcexpanded.essence.block.altar;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.mcexpanded.essence.Essence;
import net.mcexpanded.essence.compat.IrisCompat;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import org.jspecify.annotations.Nullable;

import java.awt.*;
import java.util.Random;

public class AltarRenderer implements BlockEntityRenderer<AltarBlockEntity, AltarRenderState>
{
    public AltarRenderer(BlockEntityRendererProvider.Context context)
    {
    }

    @Override
    public AltarRenderState createRenderState()
    {
        return new AltarRenderState();
    }

    @Override
    public void extractRenderState(AltarBlockEntity blockEntity, AltarRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(
                blockEntity,
                state,
                partialTicks,
                cameraPosition,
                breakProgress
        );
    }

    private static void submitFace(PoseStack ps, SubmitNodeCollector collector, Vec3 blockCenter, int light)
    {
        collector.submitCustomGeometry(ps, RenderTypes.entityCutout(Essence.rl("textures/pane_base.png")), (pose, buffer) ->
        {
            float subsections = 0.5f;
            for (float i = -5; i < 5; i += subsections)
            {
                for (float j = -5; j < 5; j += subsections)
                {
                    addVertex(pose, buffer, 0.0F + i, 0.0F + j, light);
                    addVertex(pose, buffer, subsections + i, 0.0F + j, light);
                    addVertex(pose, buffer, subsections + i, subsections + j, light);
                    addVertex(pose, buffer, 0.0F + i, subsections + j, light);
                }
            }
        });
    }

    private static void addVertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float z, int light)
    {
        buffer.addVertex(pose, x + 1, 0.003f, z)
                .setColor(getColor(x, z))
                .setUv(0, 0)
                .setUv1(0, 10)
                .setUv2(light, 0)
                .setNormal(pose, 0.0F, -1.0F, 0.0F);
    }

    @Override
    public boolean shouldRender(AltarBlockEntity blockEntity, Vec3 cameraPosition)
    {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(AltarBlockEntity blockEntity)
    {
        return new AABB(blockEntity.getBlockPos()).inflate(10);
    }

    @Override
    public void submit(AltarRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        poseStack.pushPose();

        //if iris, use lower light so it doesn't look as bright, otherwise use block light
        int light = ModList.get().isLoaded("iris") && IrisCompat.isShaderPackInUse() ? 0xF000D8 : state.lightCoords;

        submitFace(poseStack, submitNodeCollector, state.blockPos.getCenter(), light);

        poseStack.popPose();
    }

    private static final int COLOR_1 = 0x0a1144;
    private static final int COLOR_2 = 0x060028;

    // --- Tuning knobs ---
    // Smaller = larger, smoother blobs. Bigger = more, smaller blobs.
    private static final double NOISE_SCALE = 0.3;
    // Smaller = slower morphing animation.
    private static final double TIME_SCALE = 0.2;
    // >1 sharpens the transition between colors (more solid blobs, less muddy blend).
    private static final double CONTRAST = 1;

    public static int getColor(double x, double y)
    {
        double t = System.nanoTime() * 1e-9 * TIME_SCALE;

        double n = perlin3D(x * NOISE_SCALE, y * NOISE_SCALE, t);

        double blend = (n + 1.0) * 0.5;                 // roughly 0..1
        blend = (blend - 0.5) * CONTRAST + 0.5;          // push toward extremes
        blend = clamp01(blend);
        blend = blend * blend * (3 - 2 * blend);         // smoothstep, softens edges

        return lerpColor(COLOR_1, COLOR_2, blend);
    }

    private static double clamp01(double v)
    {
        return Math.max(0.0, Math.min(1.0, v));
    }

    private static int lerpColor(int c1, int c2, double t)
    {
        int r1 = (c1 >> 16) & 0xFF, g1 = (c1 >> 8) & 0xFF, b1 = c1 & 0xFF;
        int r2 = (c2 >> 16) & 0xFF, g2 = (c2 >> 8) & 0xFF, b2 = c2 & 0xFF;

        int r = (int) Math.round(r1 + (r2 - r1) * t);
        int g = (int) Math.round(g1 + (g2 - g1) * t);
        int b = (int) Math.round(b1 + (b2 - b1) * t);

        return (r << 16) | (g << 8) | b;
    }

    // ---- Perlin noise (Ken Perlin's "improved noise", 3D) ----
    private static final int[] PERM = new int[512];

    static
    {
        int[] p = new int[256];
        for (int i = 0; i < 256; i++) p[i] = i;

        Random rnd = new Random(42); // fixed seed -> same noise field every run
        for (int i = 255; i > 0; i--)
        {
            int j = rnd.nextInt(i + 1);
            int tmp = p[i];
            p[i] = p[j];
            p[j] = tmp;
        }
        for (int i = 0; i < 256; i++)
        {
            PERM[i] = p[i];
            PERM[256 + i] = p[i];
        }
    }

    private static double fade(double t)
    {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private static double lerp(double t, double a, double b)
    {
        return a + t * (b - a);
    }

    private static double grad(int hash, double x, double y, double z)
    {
        int h = hash & 15;
        double u = h < 8 ? x : y;
        double v = h < 4 ? y : (h == 12 || h == 14 ? x : z);
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    private static double perlin3D(double x, double y, double z)
    {
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;
        int Z = (int) Math.floor(z) & 255;

        x -= Math.floor(x);
        y -= Math.floor(y);
        z -= Math.floor(z);

        double u = fade(x), v = fade(y), w = fade(z);

        int A = PERM[X] + Y, AA = PERM[A] + Z, AB = PERM[A + 1] + Z;
        int B = PERM[X + 1] + Y, BA = PERM[B] + Z, BB = PERM[B + 1] + Z;

        return lerp(w,
                lerp(v,
                        lerp(u, grad(PERM[AA], x, y, z), grad(PERM[BA], x - 1, y, z)),
                        lerp(u, grad(PERM[AB], x, y - 1, z), grad(PERM[BB], x - 1, y - 1, z))),
                lerp(v,
                        lerp(u, grad(PERM[AA + 1], x, y, z - 1), grad(PERM[BA + 1], x - 1, y, z - 1)),
                        lerp(u, grad(PERM[AB + 1], x, y - 1, z - 1), grad(PERM[BB + 1], x - 1, y - 1, z - 1))));
    }
}