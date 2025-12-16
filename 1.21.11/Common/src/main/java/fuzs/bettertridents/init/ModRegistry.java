package fuzs.bettertridents.init;

import com.mojang.serialization.MapCodec;
import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.advancements.critereon.WetEntityPredicate;
import fuzs.bettertridents.world.entity.item.LoyalExperienceOrb;
import fuzs.bettertridents.world.entity.item.LoyalItemEntity;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentType;
import fuzs.puzzleslib.api.data.v2.AbstractDatapackRegistriesProvider;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.EntityTypePredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

public class ModRegistry {
    public static final RegistrySetBuilder REGISTRY_SET_BUILDER = new RegistrySetBuilder().add(Registries.ENCHANTMENT,
            ModRegistry::bootstrapEnchantments);
    static final RegistryManager REGISTRIES = RegistryManager.from(BetterTridents.MOD_ID);
    public static final Holder.Reference<Item> TRIDENT_FRAGMENT_ITEM = REGISTRIES.registerItem("trident_fragment");
    public static final Holder.Reference<EntityType<LoyalItemEntity>> LOYAL_ITEM_ENTITY_TYPE = REGISTRIES.registerEntityType(
            "loyal_item",
            () -> EntityType.Builder.<LoyalItemEntity>of(LoyalItemEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(6)
                    .updateInterval(20));
    public static final Holder.Reference<EntityType<LoyalExperienceOrb>> LOYAL_EXPERIENCE_ORB_ENTITY_TYPE = REGISTRIES.registerEntityType(
            "loyal_experience_orb",
            () -> EntityType.Builder.<LoyalExperienceOrb>of(LoyalExperienceOrb::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(6)
                    .updateInterval(20));
    public static final Holder.Reference<MapCodec<WetEntityPredicate>> IS_WET_ENTITY_SUB_PREDICATE_TYPE = REGISTRIES.register(
            Registries.ENTITY_SUB_PREDICATE_TYPE,
            "is_wet",
            () -> WetEntityPredicate.CODEC);

    public static final DataAttachmentType<Entity, Integer> TRIDENT_SLOT_ATTACHMENT_TYPE = DataAttachmentRegistry.<Integer>entityBuilder()
            .persistent(ExtraCodecs.NON_NEGATIVE_INT)
            .build(BetterTridents.id("trident_slot"));
    public static final DataAttachmentType<Entity, DamageSource> LAST_DAMAGE_SOURCE_ATTACHMENT_TYPE = DataAttachmentRegistry.<DamageSource>entityBuilder()
            .build(BetterTridents.id("last_damage_source"));

    public static void bootstrap() {
        ModLootTables.bootstrap();
    }

    public static void bootstrapEnchantments(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderGetter<EntityType<?>> entityTypes = context.lookup(Registries.ENTITY_TYPE);
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
        AbstractDatapackRegistriesProvider.registerEnchantment(context,
                net.minecraft.world.item.enchantment.Enchantments.IMPALING,
                Enchantment.enchantment(Enchantment.definition(items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                                2,
                                5,
                                Enchantment.dynamicCost(1, 8),
                                Enchantment.dynamicCost(21, 8),
                                4,
                                EquipmentSlotGroup.MAINHAND))
                        .exclusiveWith(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                        .withEffect(EnchantmentEffectComponents.DAMAGE,
                                new AddValue(LevelBasedValue.perLevel(2.5F)),
                                AnyOfCondition.anyOf(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                                EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypes,
                                                                EntityTypeTags.SENSITIVE_TO_IMPALING))
                                                        .build()),
                                        LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                                EntityPredicate.Builder.entity()
                                                        .subPredicate(WetEntityPredicate.INSTANCE)))));
    }
}
