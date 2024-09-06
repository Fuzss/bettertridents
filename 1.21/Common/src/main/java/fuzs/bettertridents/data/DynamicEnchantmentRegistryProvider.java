package fuzs.bettertridents.data;

import fuzs.puzzleslib.api.data.v2.AbstractRegistriesDatapackGenerator;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.WeatherCheck;

public class DynamicEnchantmentRegistryProvider extends AbstractRegistriesDatapackGenerator.Enchantments {

    public DynamicEnchantmentRegistryProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    protected void addBootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderGetter<Fluid> fluids = context.lookup(Registries.FLUID);
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
        this.add(net.minecraft.world.item.enchantment.Enchantments.IMPALING, Enchantment.enchantment(
                        Enchantment.definition(items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE), 2, 5,
                                Enchantment.dynamicCost(1, 8), Enchantment.dynamicCost(21, 8), 4, EquipmentSlotGroup.MAINHAND
                        ))
                .exclusiveWith(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                .withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(LevelBasedValue.perLevel(2.5F)),
                        AnyOfCondition.anyOf(
                                LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                        EntityPredicate.Builder.entity()
                                                .entityType(
                                                        EntityTypePredicate.of(EntityTypeTags.SENSITIVE_TO_IMPALING))
                                                .build()
                                ), WeatherCheck.weather().setRaining(true),
                                LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                        EntityPredicate.Builder.entity()
                                                .located(LocationPredicate.Builder.location()
                                                        .setBlock(BlockPredicate.Builder.block()
                                                                .of(Blocks.BUBBLE_COLUMN)))
                                ), LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                        EntityPredicate.Builder.entity()
                                                .located(LocationPredicate.Builder.location()
                                                        .setFluid(FluidPredicate.Builder.fluid()
                                                                .of(fluids.getOrThrow(FluidTags.WATER))))
                                )
                        )
                ));
    }
}
