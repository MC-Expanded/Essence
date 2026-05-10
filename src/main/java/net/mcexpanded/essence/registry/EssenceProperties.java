package net.mcexpanded.essence.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.List;

public record EssenceProperties
        (
                Position pushDirection,
                List<Identifier> behaviours,
                float cost,
                int color
        )
{

    public EssenceProperties(Position pushDirection, float cost, int color)
    {
        this(pushDirection, List.of(), cost, color);
    }

    public static final EssenceProperties EMPTY = new EssenceProperties(new Position(0, 0), List.of(), 0, 0);

    public static final Codec<EssenceProperties> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Position.CODEC.fieldOf("push_direction").forGetter(EssenceProperties::pushDirection),
                    Identifier.CODEC.listOf().fieldOf("behaviours").forGetter(EssenceProperties::behaviours),
                    Codec.FLOAT.fieldOf("exp_cost").forGetter(EssenceProperties::cost),
                    Codec.INT.fieldOf("color").forGetter(EssenceProperties::color)
            ).apply(instance, EssenceProperties::new)
    );

}
