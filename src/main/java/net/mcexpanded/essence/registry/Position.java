package net.mcexpanded.essence.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Position(float x, float y)
{
    public static final Codec<Position> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("x").forGetter(Position::x),
                    Codec.FLOAT.fieldOf("z").forGetter(Position::y)
                    ).apply(instance, Position::new)
    );

    public Position offset(float x, float y)
    {
        return new Position(this.x + x, this.y + y);
    }

    public Position offset(Position offset)
    {
        return new Position(this.x + offset.x, this.y + offset.y);
    }

    public Position offset(Position offset, float scale)
    {
        return new Position(this.x + offset.x * scale, this.y + offset.y * scale);
    }
}
