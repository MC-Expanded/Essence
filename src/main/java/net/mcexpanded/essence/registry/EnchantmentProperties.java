package net.mcexpanded.essence.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

import java.util.List;

public record EnchantmentProperties
        (
                int color,
                List<Item> sacrifices,
                Identifier nodeGroup,
                String nameOverride
        )
{

    public EnchantmentProperties(int color, List<Item> sacrifices, Identifier nodeGroup)
    {
        this(color, sacrifices, nodeGroup, "");
    }

    public EnchantmentProperties(int color, Identifier nodeGroup)
    {
        this(color, List.of(), nodeGroup, "");
    }

    public static final EnchantmentProperties EMPTY = new EnchantmentProperties(-1, List.of(), null, "");

    public static final Codec<EnchantmentProperties> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("color").forGetter(EnchantmentProperties::color),
                    BuiltInRegistries.ITEM.byNameCodec().listOf().fieldOf("sacrifices").forGetter(EnchantmentProperties::sacrifices),
                    Identifier.CODEC.fieldOf("node_group").forGetter(EnchantmentProperties::nodeGroup),
                    Codec.STRING.optionalFieldOf("name_override", "").forGetter(EnchantmentProperties::nameOverride)
            ).apply(instance, EnchantmentProperties::new)
    );

    public EnchantmentProperties withName(@Nullable String name)
    {
        return new EnchantmentProperties(color, sacrifices, nodeGroup, name);
    }

    public EnchantmentProperties withColor(int color)
    {
        return new EnchantmentProperties(color, sacrifices, nodeGroup, nameOverride);
    }
}
