package net.mcexpanded.essence.datagen;

import net.mcexpanded.essence.registry.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DGSCDataMapsProvider extends DataMapProvider
{
    protected DGSCDataMapsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider)
    {
        var essence = this.builder(EssenceDataMaps.ESSENCE_PROPERTIES);
        var group = this.builder(EssenceDataMaps.ENCHANT_PROPERTIES);

        essence.add(Items.LAPIS_LAZULI.builtInRegistryHolder(), new EssenceProperties(new Position(0f, 20f), 2, 0xff4d58d6), false);
        essence.add(Items.LAPIS_BLOCK.builtInRegistryHolder(), new EssenceProperties(new Position(0f, 60f), 2, 0xff4d58d6), false);
        essence.add(Items.AMETHYST_SHARD.builtInRegistryHolder(), new EssenceProperties(new Position(0, -20f), 2, 0xffc060ce), false);
        essence.add(Items.QUARTZ.builtInRegistryHolder(), new EssenceProperties(new Position(10f, 10f), 2, 0xffe6e6e6), false);
        essence.add(Items.PRISMARINE_CRYSTALS.builtInRegistryHolder(), new EssenceProperties(new Position(-10f, 10f), 2, 0xff91e2df), false);

        //enchantment groups/properties
        group.add(Enchantments.FIRE_ASPECT, new EnchantmentProperties(0xffef8c35, EnchantmentNodeGroup.FIRE.identifier()), false);
        group.add(Enchantments.FIRE_PROTECTION, new EnchantmentProperties(0xffef6035, EnchantmentNodeGroup.FIRE.identifier()), false);
        group.add(Enchantments.FLAME,new EnchantmentProperties(0xffd67d13,  EnchantmentNodeGroup.FIRE.identifier()), false);

        group.add(Enchantments.LUCK_OF_THE_SEA, new EnchantmentProperties(0xff3d94d6, EnchantmentNodeGroup.BONUS_DROPS.identifier()), false);
        group.add(Enchantments.FORTUNE, new EnchantmentProperties(0xfff3d316, EnchantmentNodeGroup.BONUS_DROPS.identifier()), false);
        group.add(Enchantments.LOOTING, new EnchantmentProperties(0xff33da6b, EnchantmentNodeGroup.BONUS_DROPS.identifier()), false);
        group.add(Enchantments.INFINITY, new EnchantmentProperties(0xffa86ad2, EnchantmentNodeGroup.BONUS_DROPS.identifier()), false);

        group.add(Enchantments.PROJECTILE_PROTECTION, new EnchantmentProperties(0xff6a8dd2, EnchantmentNodeGroup.DEFENSE.identifier()), false);
        group.add(Enchantments.PROTECTION, new EnchantmentProperties(0xff6ab6d2, EnchantmentNodeGroup.DEFENSE.identifier()), false);
        group.add(Enchantments.BLAST_PROTECTION,new EnchantmentProperties(0xff706ad2,  EnchantmentNodeGroup.DEFENSE.identifier()), false);
        group.add(Enchantments.FEATHER_FALLING, new EnchantmentProperties(0xff958aa9, EnchantmentNodeGroup.DEFENSE.identifier()), false);
        group.add(Enchantments.THORNS, new EnchantmentProperties(-1, EnchantmentNodeGroup.DEFENSE.identifier()), false);
        group.add(Enchantments.BINDING_CURSE,new EnchantmentProperties(-1,  EnchantmentNodeGroup.DEFENSE.identifier()), false);

        group.add(Enchantments.SMITE,new EnchantmentProperties(-1,  EnchantmentNodeGroup.ATTACK.identifier()), false);
        group.add(Enchantments.BANE_OF_ARTHROPODS,new EnchantmentProperties(-1,  EnchantmentNodeGroup.ATTACK.identifier()), false);
        group.add(Enchantments.PUNCH, new EnchantmentProperties(-1, EnchantmentNodeGroup.ATTACK.identifier()), false);
        group.add(Enchantments.KNOCKBACK,new EnchantmentProperties(-1,  EnchantmentNodeGroup.ATTACK.identifier()), false);
        group.add(Enchantments.IMPALING,new EnchantmentProperties(-1,  EnchantmentNodeGroup.ATTACK.identifier()), false);
        group.add(Enchantments.PIERCING, new EnchantmentProperties(-1, EnchantmentNodeGroup.ATTACK.identifier()), false);
        group.add(Enchantments.SHARPNESS,new EnchantmentProperties(-1,  EnchantmentNodeGroup.ATTACK.identifier()), false);
        group.add(Enchantments.DENSITY, new EnchantmentProperties(-1, EnchantmentNodeGroup.ATTACK.identifier()), false);
        group.add(Enchantments.POWER, new EnchantmentProperties(-1, EnchantmentNodeGroup.ATTACK.identifier()), false);
        group.add(Enchantments.VANISHING_CURSE,new EnchantmentProperties(-1,  EnchantmentNodeGroup.DEFENSE.identifier()), false);
        group.add(Enchantments.BREACH,new EnchantmentProperties(-1,  EnchantmentNodeGroup.DEFENSE.identifier()), false);

        group.add(Enchantments.EFFICIENCY, new EnchantmentProperties(-1, EnchantmentNodeGroup.SPEED.identifier()), false);
        group.add(Enchantments.LURE, new EnchantmentProperties(-1, EnchantmentNodeGroup.SPEED.identifier()), false);
        group.add(Enchantments.RIPTIDE,new EnchantmentProperties(-1,  EnchantmentNodeGroup.SPEED.identifier()), false);
        group.add(Enchantments.WIND_BURST,new EnchantmentProperties(-1,  EnchantmentNodeGroup.SPEED.identifier()), false);
        group.add(Enchantments.DEPTH_STRIDER, new EnchantmentProperties(-1, EnchantmentNodeGroup.SPEED.identifier()), false);
        group.add(Enchantments.SOUL_SPEED,new EnchantmentProperties(-1,  EnchantmentNodeGroup.SPEED.identifier()), false);
        group.add(Enchantments.LUNGE, new EnchantmentProperties(-1, EnchantmentNodeGroup.SPEED.identifier()), false);
        group.add(Enchantments.QUICK_CHARGE,new EnchantmentProperties(-1,  EnchantmentNodeGroup.SPEED.identifier()), false);

        group.add(Enchantments.FROST_WALKER, new EnchantmentProperties(-1, EnchantmentNodeGroup.SPECIAL.identifier()), false);
        group.add(Enchantments.SILK_TOUCH, new EnchantmentProperties(-1, EnchantmentNodeGroup.SPECIAL.identifier()), false);
        group.add(Enchantments.SWEEPING_EDGE, new EnchantmentProperties(-1, EnchantmentNodeGroup.SPECIAL.identifier()), false);
        group.add(Enchantments.UNBREAKING, new EnchantmentProperties(-1, EnchantmentNodeGroup.SPECIAL.identifier()), false);
        group.add(Enchantments.AQUA_AFFINITY,new EnchantmentProperties(-1,  EnchantmentNodeGroup.SPECIAL.identifier()), false);
        group.add(Enchantments.RESPIRATION,new EnchantmentProperties(-1,  EnchantmentNodeGroup.SPECIAL.identifier()), false);
        group.add(Enchantments.CHANNELING,new EnchantmentProperties(-1,  EnchantmentNodeGroup.SPECIAL.identifier()), false);
        group.add(Enchantments.LOYALTY,new EnchantmentProperties(-1,  EnchantmentNodeGroup.SPECIAL.identifier()), false);

        group.add(Enchantments.MENDING, new EnchantmentProperties(-1, EnchantmentNodeGroup.MENDING.identifier()), false);
    }
}
