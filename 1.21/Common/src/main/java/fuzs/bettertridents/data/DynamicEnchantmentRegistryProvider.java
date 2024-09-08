package fuzs.bettertridents.data;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.advancements.critereon.WetEntityPredicate;
import fuzs.bettertridents.config.CommonConfig;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.data.v2.AbstractRegistriesDatapackGenerator;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

public class DynamicEnchantmentRegistryProvider extends AbstractRegistriesDatapackGenerator.Enchantments {

    public DynamicEnchantmentRegistryProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    protected void addBootstrap(BootstrapContext<Enchantment> context) {
        // need this here to work across world restarts on Fabric
        if (ModLoaderEnvironment.INSTANCE.getModLoader().isFabricLike() && !BetterTridents.CONFIG.get(
                CommonConfig.class).boostImpaling) {
            return;
        }
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
                                ), LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                        EntityPredicate.Builder.entity().subPredicate(WetEntityPredicate.INSTANCE)
                                ))
                ));
    }
}
