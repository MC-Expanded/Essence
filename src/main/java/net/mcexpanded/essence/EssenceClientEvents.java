package net.mcexpanded.essence;

import net.mcexpanded.essence.block.altar.AltarRenderer;
import net.mcexpanded.essence.registry.EssenceBlockEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Essence.MOD_ID, value = Dist.CLIENT)
public class EssenceClientEvents
{
    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(EssenceBlockEntities.ALTAR.get(), AltarRenderer::new);
    }
}
