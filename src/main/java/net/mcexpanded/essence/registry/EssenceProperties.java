package net.mcexpanded.essence.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.List;

public record EssenceProperties
        (
                Position pushDirection,
                List<Identifier> behaviours,
                float cost
        )
{

    public static final Codec<EssenceProperties> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Position.CODEC.fieldOf("push_direction").forGetter(EssenceProperties::pushDirection),
                    Identifier.CODEC.listOf().fieldOf("behaviours").forGetter(EssenceProperties::behaviours),
                    Codec.FLOAT.fieldOf("exp_cost").forGetter(EssenceProperties::cost)
            ).apply(instance, EssenceProperties::new)
    );

}
