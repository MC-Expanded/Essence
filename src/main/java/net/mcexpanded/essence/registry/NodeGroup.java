package net.mcexpanded.essence.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record NodeGroup
        (
                Position position,
                boolean dynamic
        )
{
    public static final Codec<NodeGroup> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Position.CODEC.fieldOf("position").forGetter(NodeGroup::position),
                    Codec.BOOL.fieldOf("dynamic").forGetter(NodeGroup::dynamic)
            ).apply(instance, NodeGroup::new)
    );
}
