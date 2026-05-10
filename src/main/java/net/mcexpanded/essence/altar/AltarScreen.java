package net.mcexpanded.essence.altar;

import com.mojang.datafixers.util.Pair;
import com.sun.jna.platform.win32.WinBase;
import net.mcexpanded.essence.Essence;
import net.mcexpanded.essence.registry.EnchantmentNode;
import net.mcexpanded.essence.registry.EssenceProperties;
import net.mcexpanded.essence.registry.Position;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix3x2fStack;
import org.joml.Vector2f;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class AltarScreen extends AbstractContainerScreen<AltarMenu>
{
    public static final Identifier ARROW = Essence.rl("textures/gui/altar/arrow.png");
    public static final Identifier SHADOW = Essence.rl("textures/gui/altar/shadow.png");
    public static final Identifier NODE = Essence.rl("textures/gui/altar/node.png");
    public static final Identifier HEX = Essence.rl("textures/gui/altar/hex.png");

    private final List<Pair<AltarBlock.AltarPart, EssenceProperties>> essences;
    private final List<Pair<AltarBlock.AltarPart, ItemStack>> essencesItems;
    private final List<EnchantmentNode> nodes;

    private boolean mouseDragging = false;
    private float xDragOffset = 0;
    private float yDragOffset = 0;
    private float scrollScale = 1;

    public AltarScreen(AltarMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, Component.empty());
        inventoryLabelY = 2314234;

        essences = menu.be.getAllEssences();
        essencesItems = menu.be.getAllEssencesItems();
        EnchantmentNode.cache = null;
        nodes = EnchantmentNode.getAllNodes(Minecraft.getInstance().level, menu.be.getItem(), menu.be.seed);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scrollX, double scrollY)
    {
        scrollScale += (float) (scrollY / 10);
        if (scrollScale < 0.5f)
            scrollScale = 0.5f;
        else
        {
            //todo adjust zooming so it zooms in and out of the center of the screen instead of the center of the map
            //xDragOffset += xDragOffset / scrollScale;
            //yDragOffset += yDragOffset / scrollScale;
        }

        return super.mouseScrolled(x, y, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick)
    {
        if (event.button() == 0)
        {
            mouseDragging = true;
        }
        return super.mouseClicked(event, doubleClick);

    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event)
    {
        if (event.button() == 0)
        {
            mouseDragging = false;
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy)
    {
        if (mouseDragging)
        {
            xDragOffset += dx;
            yDragOffset += dy;
        }
        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int rawMouseX, int rawMouseY, float a)
    {
        int x = this.width / 2;
        int y = this.height / 2;

        double xMousePos = (rawMouseX - xDragOffset - width / 2) / scrollScale;
        double yMousePos = (rawMouseY - yDragOffset - height / 2) / scrollScale;

        EnchantmentNode hoveredNode = null;

        //scissor to limit rendering to where there's background
        guiGraphics.enableScissor(x - 100, y - 100, x + 100, y + 100);
        //black background
        guiGraphics.fill(x - 1000, y - 1000, x + 1000, y + 1000, 0xff29033e);

        //translates everything based on the mouse dragging offset
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y);
        guiGraphics.pose().scale(scrollScale);
        guiGraphics.pose().translate(xDragOffset / scrollScale, yDragOffset / scrollScale);

        //render nodes
        for (EnchantmentNode node : nodes)
        {
            if (Math.abs(xMousePos - node.x()) <= 4 && Math.abs(yMousePos - node.y()) <= 4)
            {
                hoveredNode = node;
                //background shadow
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SHADOW, (int) (hoveredNode.x() - 8), (int) (hoveredNode.y() - 8),
                        0, 0, 16, 16, 16, 16, 16, 16);
            }

            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, NODE, (int) (node.x() - 3), (int) (node.y() - 3),
                    0, 0, 6, 6, 6, 6, 6, 6,
                    hoveredNode == node ? 0xffffffff : node.available() ? node.enchantmentProperties().color() : 0xff333333);
        }

        float xCurrentPosition = 0;
        float yCurrentPosition = 0;
        //render lines from essences
        for (Pair<AltarBlock.AltarPart, EssenceProperties> essence : essences)
        {
            Position pushWithOffset = AltarBlockEntity.getPositionWithOffset(essence);

            float xO = xCurrentPosition;
            float yO = yCurrentPosition;

            xCurrentPosition += pushWithOffset.x();
            yCurrentPosition += pushWithOffset.y();

            drawLine(guiGraphics, xO, yO, xCurrentPosition, yCurrentPosition, essence.getSecond().color());

            guiGraphics.fill(
                    (int) (xCurrentPosition - 1),
                    (int) (yCurrentPosition - 1),
                    (int) (xCurrentPosition + 1),
                    (int) (yCurrentPosition + 1),
                    essence.getSecond().color());
        }

        //green dot at 0,0
        guiGraphics.fill(-1, -1, 1, 1, 0xff00ff00);

        //render main item offset by total lines distance
        {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(xCurrentPosition, yCurrentPosition);

            //render main item
            ItemStack item = menu.be.getItem();
            if (!item.isEmpty())
            {
                TrackingItemStackRenderState renderState = new TrackingItemStackRenderState();
                Minecraft.getInstance().getItemModelResolver().updateForTopItem(renderState, item, ItemDisplayContext.FIXED, null, null, 0);

                guiGraphics.enableScissor(-123123, 0, 121212, 122121);


                guiGraphics.submitPictureInPictureRenderState(new SpinningItemPictureInPictureRenderer.RenderState(
                        renderState,
                        (float) ((Math.sin(Util.getMillis() / 1000f + 235)) * 25),
                        (float) ((Math.sin(Util.getMillis() / 1000f + 235632)) * 25),
                        (float) ((Math.sin(Util.getMillis() / 1000f + 123657)) * 5),
                        (int) (0 + xDragOffset), (int) (0 + yDragOffset), (int) (width + xDragOffset), (int) (height + yDragOffset),
                        16 * scrollScale, guiGraphics.peekScissorStack()
                ));

                guiGraphics.disableScissor();
            }

            guiGraphics.pose().popMatrix();
            guiGraphics.pose().popMatrix();
        }

        //render pedestal items
        {
            //background shadow on every spot
            if (false)
                Arrays.stream(AltarBlock.AltarPart.values()).forEach(o ->
                        {
                            if (o.equals(AltarBlock.AltarPart.ALTAR)) return;
                            Position pos = getScreenPosOffsetForAltarPart(o);
                            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SHADOW, (int) (pos.x() + x - 8), (int) (pos.y() + y - 8),
                                    0, 0, 16, 16, 16, 16, 16, 16);
                        }
                );

            if (true)
                //render hex
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, HEX, x - 100, y - 100,
                        0, 0, 200, 200, 200, 200, 200, 200);

            //item + foreground shadow on pedestals with items
            for (Pair<AltarBlock.AltarPart, ItemStack> pair : essencesItems)
            {
                Position pos = getScreenPosOffsetForAltarPart(pair.getFirst());

                //item
                {
                    guiGraphics.pose().pushMatrix();
                    //wavy effect
                    guiGraphics.pose().translate(new Vector2f(
                                    (float) Math.sin((Util.getMillis() + pos.x() + pos.y() * 200) / 800d),
                                    (float) Math.sin((Util.getMillis() + pos.x() + pos.y() * 200) / 1000d)
                            )
                    );
                    //render item
                    guiGraphics.item(pair.getSecond(), (int) (pos.x() + x - 8), (int) (pos.y() + y - 8), 0);
                }

                //foreground shadow
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SHADOW, (int) (pos.x() + x - 8), (int) (pos.y() + y - 8),
                        0, 0, 16, 16, 16, 16, 16, 16);
                guiGraphics.pose().popMatrix();
            }
        }

        //render arrow under essence item
        for (Pair<AltarBlock.AltarPart, EssenceProperties> essence : essences)
        {
            Position offset = getScreenPosOffsetForAltarPart(essence.getFirst());
            //Position pos = offset.offset(offset, -0.13f);

            Matrix3x2fStack pose = guiGraphics.pose();
            pose.pushMatrix();

            Position pushWithOffset = AltarBlockEntity.getPositionWithOffset(essence);

            pose.translate((int) offset.x() + x, (int) offset.y() + y);
            pose.rotate((float) Math.atan2(pushWithOffset.x(), -pushWithOffset.y()));

            //render arrow with relative rotation
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, ARROW, -3, -3,
                    0, 0, 7, 7, 7, 7, 7, 7, essence.getSecond().color());

            pose.popMatrix();

        }

        guiGraphics.disableScissor();
        //outline
        guiGraphics.outline(x - 101, y - 101, 202, 202, 0xff14021f);
        guiGraphics.outline(x - 102, y - 102, 204, 204, 0xff14021f);

        //render hovered node name
        if (hoveredNode != null)
        {
            Identifier identifier = ItemStack.EMPTY.get(DataComponents.TOOLTIP_STYLE);
            //enchantment.minecraft.sharpness
            if (hoveredNode.enchantment() == null || !hoveredNode.enchantmentProperties().nameOverride().isEmpty())
            {
                List<Component> components = List.of(Component.translatable(hoveredNode.enchantmentProperties().nameOverride()));
                List<ClientTooltipComponent> list = components.stream().map(Component::getVisualOrderText).map(ClientTooltipComponent::create).toList();
                //guiGraphics.tooltip(this.font, list, x - 105, y + 95, DefaultTooltipPositioner.INSTANCE, identifier);
                guiGraphics.tooltip(this.font, list, rawMouseX, rawMouseY, DefaultTooltipPositioner.INSTANCE, identifier);
            }
            else
            {
                List<Component> components = List.of(Component.translatable("enchantment." + hoveredNode.enchantment().getRegisteredName().replace(":", ".")));
                List<ClientTooltipComponent> list = components.stream().map(Component::getVisualOrderText).map(ClientTooltipComponent::create).toList();
                //guiGraphics.tooltip(this.font, list, x - 105, y + 95, DefaultTooltipPositioner.INSTANCE, identifier);
                guiGraphics.tooltip(this.font, list, rawMouseX, rawMouseY, DefaultTooltipPositioner.INSTANCE, identifier);
            }
        }

        super.extractRenderState(guiGraphics, rawMouseX, rawMouseY, a);
    }

    private static @NonNull Position getScreenPosOffsetForAltarPart(AltarBlock.AltarPart part)
    {
        return switch (part)
        {
            case PEDESTAL_N -> new Position(0, -90);
            case PEDESTAL_NE -> new Position(75, -75);
            case PEDESTAL_E -> new Position(-90, 0);
            case PEDESTAL_SE -> new Position(75, 75);
            case PEDESTAL_S -> new Position(0, 90);
            case PEDESTAL_SW -> new Position(-75, 75);
            case PEDESTAL_W -> new Position(90, 0);
            case PEDESTAL_NW -> new Position(-75, -75);
            default -> new Position(0, 0);
        };
    }

    public static void drawLine(GuiGraphicsExtractor guiGraphics, float startX, float startY, float endX, float endY, int color)
    {
        Matrix3x2fStack stack = guiGraphics.pose();
        stack.pushMatrix();
        stack.translate(startX, startY);
        Vec2 vec = new Vec2(endX - startX, endY - startY);
        float angle = (float) Math.acos(new Vec2(0, 1).dot(vec.normalized()));
        stack.rotate(vec.x > 0 ? -angle : angle);
        stack.translate(-0.5f, 0);
        guiGraphics.fill(0, 3, 1, (int) vec.length() - 4, color);
        stack.popMatrix();
    }
}
