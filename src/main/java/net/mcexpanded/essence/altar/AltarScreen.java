package net.mcexpanded.essence.altar;

import net.mcexpanded.essence.Essence;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class AltarScreen extends AbstractContainerScreen<AltarMenu>
{
    private static final Identifier TEXTURE = Essence.rl("textures/gui/tackle_box/tackle_box.png");
    private static final Identifier ICONS = Essence.rl("textures/gui/tackle_box/tackle_box_icons.png");

    public AltarScreen(AltarMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, Component.empty());
        inventoryLabelY = 2314234;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a)
    {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        super.extractRenderState(graphics, mouseX, mouseY, a);
    }
}
