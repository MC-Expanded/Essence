package net.mcexpanded.essence.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Position(float x, float y)
{
    public static final Codec<Position> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("x").forGetter(Position::x),
                    Codec.FLOAT.fieldOf("y").forGetter(Position::y)
                    ).apply(instance, Position::new)
    );
}
