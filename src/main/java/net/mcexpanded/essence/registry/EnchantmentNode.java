package net.mcexpanded.essence.registry;

import net.mcexpanded.essence.Essence;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public record EnchantmentNode
        (
                float x,
                float y,
                Holder<Enchantment> enchantment,
                boolean available,
                EnchantmentProperties enchantmentProperties
        )
{

    public static List<EnchantmentNode> cache = null;

    public static List<EnchantmentNode> getAllNodes(Level level, ItemStack itemStack, long seed)
    {
        if (cache != null) return cache;
        Registry<Enchantment> registry = level.registryAccess().lookup(Registries.ENCHANTMENT).get();
        List<Holder<Enchantment>> list = registry.stream().map(registry::wrapAsHolder).toList();
        List<EnchantmentNode> nodes = new ArrayList<>();
        Random r = new Random(seed);

        level.registryAccess().lookupOrThrow(Essence.NODE_GROUP_REGISTRY_KEY)
                .stream().forEach(o ->
                        nodes.add(new EnchantmentNode(
                                o.position().x(),
                                -o.position().y(),
                                null,
                                true,
                                EnchantmentProperties.EMPTY.withName(
                                        level.registryAccess().lookupOrThrow(Essence.NODE_GROUP_REGISTRY_KEY).getKeyOrNull(o).toString()
                                ).withColor(0x66ffff00)
                        ))
                );

        for (Holder<Enchantment> enchantmentHolder : list)
        {
            EnchantmentProperties ep = enchantmentHolder.getData(EssenceDataMaps.ENCHANT_PROPERTIES);

            if (ep != null)
            {
                EnchantmentNodeGroup nodeGroup = level.registryAccess().lookupOrThrow(Essence.NODE_GROUP_REGISTRY_KEY).getValue(ep.nodeGroup());
                if (nodeGroup == null) throw new NullPointerException();

                Position position = new Position(nodeGroup.position().x(), -nodeGroup.position().y());
                Position freeSpot = getFreeSpotAroundPosition(nodes, position, r);
                nodes.add(new EnchantmentNode(
                                freeSpot.x(),
                                freeSpot.y(),
                                enchantmentHolder,
                                itemStack.supportsEnchantment(enchantmentHolder),
                                ep
                        )
                );
            }
            else
            {
                Position freeSpot = getFreeSpotAroundPosition(nodes, new Position(r.nextInt(20, 100), r.nextInt(20, 100)), r);
                nodes.add(new EnchantmentNode(
                        freeSpot.x(),
                        freeSpot.y(),
                        enchantmentHolder,
                        itemStack.supportsEnchantment(enchantmentHolder),
                        EnchantmentProperties.EMPTY
                ));
            }

        }

        cache = nodes;
        return cache;
    }

    private static Position getFreeSpotAroundPosition(List<EnchantmentNode> nodes, Position startPos, Random random)
    {
        for (int i = 1; i < 1000; i++)
        {
            int x = (int) (startPos.x() + random.nextInt(i) - i / 2);
            int y = (int) (startPos.y() + random.nextInt(i) - i / 2);

            if (nodes.stream().noneMatch(o -> Math.abs(o.x - x) < 25 && Math.abs(o.y - y) < 25))
                return new Position(x, y);

        }
        return new Position(20, 20);
    }

    private static Position getFreeSpot(List<EnchantmentNode> nodes, Random random)
    {
        for (int i = 1; i < 100; i++)
        {
            int x = random.nextInt(300 + i);
            int y = random.nextInt(300 + i);

            if (nodes.stream().noneMatch(o -> Math.abs(o.x - x) < 25 && Math.abs(o.y - y) < 25))
                return new Position(x, y);

        }
        return new Position(20, 20);
    }

}
