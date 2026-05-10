package net.mcexpanded.essence;

import net.mcexpanded.essence.altar.AltarRenderer;
import net.mcexpanded.essence.altar.AltarScreen;
import net.mcexpanded.essence.altar.SpinningItemPictureInPictureRenderer;
import net.mcexpanded.essence.registry.EssenceBlockEntities;
import net.mcexpanded.essence.registry.EssenceMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterPictureInPictureRenderersEvent;

@EventBusSubscriber(modid = Essence.MOD_ID, value = Dist.CLIENT)
public class EssenceClientEvents
{
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event)
    {
        event.register(EssenceMenuTypes.ALTAR_MENU.get(), AltarScreen::new);
    }

    @SubscribeEvent
    private static void onRegisterPictureInPictureRenderers(RegisterPictureInPictureRenderersEvent event)
    {
        event.register(SpinningItemPictureInPictureRenderer.RenderState.class, SpinningItemPictureInPictureRenderer::new);
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(EssenceBlockEntities.ALTAR.get(), AltarRenderer::new);
    }

}
