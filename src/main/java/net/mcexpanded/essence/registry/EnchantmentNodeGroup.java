package net.mcexpanded.essence.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mcexpanded.essence.Essence;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public record EnchantmentNodeGroup
        (
                Position position,
                boolean dynamic
        )
{

    public static final ResourceKey<EnchantmentNodeGroup> FIRE = key("fire");
    public static final ResourceKey<EnchantmentNodeGroup> BONUS_DROPS = key("bonus_drops");
    public static final ResourceKey<EnchantmentNodeGroup> DEFENSE = key("defense");
    public static final ResourceKey<EnchantmentNodeGroup> ATTACK = key("attack");
    public static final ResourceKey<EnchantmentNodeGroup> SPEED = key("speed");
    public static final ResourceKey<EnchantmentNodeGroup> SPECIAL = key("special");
    public static final ResourceKey<EnchantmentNodeGroup> MENDING = key("mending");

    private static ResourceKey<EnchantmentNodeGroup> key(String id)
    {
        return ResourceKey.create(Essence.NODE_GROUP_REGISTRY_KEY, Essence.rl(id));
    }

    public static final Codec<EnchantmentNodeGroup> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Position.CODEC.fieldOf("position").forGetter(EnchantmentNodeGroup::position),
                    Codec.BOOL.fieldOf("dynamic").forGetter(EnchantmentNodeGroup::dynamic)
            ).apply(instance, EnchantmentNodeGroup::new)
    );
}
