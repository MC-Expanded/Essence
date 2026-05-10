package net.mcexpanded.essence.registry;

import net.mcexpanded.essence.Essence;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public interface EssenceDataMaps
{
    DataMapType<Item, EssenceProperties> ESSENCE_PROPERTIES = DataMapType.builder(
            Essence.rl("essence_properties"), Registries.ITEM, EssenceProperties.CODEC)
            .synced(EssenceProperties.CODEC, true).build();

    DataMapType<Enchantment, EnchantmentProperties> ENCHANT_PROPERTIES = DataMapType.builder(
                    Essence.rl("enchantment_properties"), Registries.ENCHANTMENT, EnchantmentProperties.CODEC)
            .synced(EnchantmentProperties.CODEC, true).build();


    static <T> T getOrDefault(ItemStack stack, DataMapType<Item, T> dataMap, T d)
    {
        T data = stack.typeHolder().getData(dataMap);
        if (data == null) return d;
        return data;
    }

}
