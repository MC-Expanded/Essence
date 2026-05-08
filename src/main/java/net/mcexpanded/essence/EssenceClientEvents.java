package net.mcexpanded.essence;

import net.mcexpanded.essence.altar.AltarScreen;
import net.mcexpanded.essence.registry.EssenceMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Essence.MOD_ID, value = Dist.CLIENT)
public class EssenceClientEvents
{
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event)
    {
        event.register(EssenceMenuTypes.ALTAR_MENU.get(), AltarScreen::new);
    }
}
