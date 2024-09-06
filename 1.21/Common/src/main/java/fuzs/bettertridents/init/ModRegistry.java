package fuzs.bettertridents.init;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.capability.LastDamageSourceCapability;
import fuzs.bettertridents.capability.TridentSlotCapability;
import fuzs.bettertridents.world.entity.item.LoyalExperienceOrb;
import fuzs.bettertridents.world.entity.item.LoyalItemEntity;
import fuzs.puzzleslib.api.capability.v3.CapabilityController;
import fuzs.puzzleslib.api.capability.v3.data.EntityCapabilityKey;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Item;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(BetterTridents.MOD_ID);
    public static final Holder.Reference<Item> TRIDENT_FRAGMENT_ITEM = REGISTRIES.registerItem("trident_fragment",
            () -> new Item((new Item.Properties()))
    );
    public static final Holder.Reference<EntityType<LoyalItemEntity>> LOYAL_ITEM_ENTITY_TYPE = REGISTRIES.registerEntityType(
            "loyal_item", () -> EntityType.Builder.<LoyalItemEntity>of(LoyalItemEntity::new, MobCategory.MISC).sized(
                    0.25F, 0.25F).clientTrackingRange(6).updateInterval(20));
    public static final Holder.Reference<EntityType<LoyalExperienceOrb>> LOYAL_EXPERIENCE_ORB_ENTITY_TYPE = REGISTRIES.registerEntityType(
            "loyal_experience_orb", () -> EntityType.Builder.<LoyalExperienceOrb>of(LoyalExperienceOrb::new,
                    MobCategory.MISC
            ).sized(0.5F, 0.5F).clientTrackingRange(6).updateInterval(20));

    static final CapabilityController CAPABILITIES = CapabilityController.from(BetterTridents.MOD_ID);
    public static final EntityCapabilityKey<ThrownTrident, TridentSlotCapability> TRIDENT_SLOT_CAPABILITY = CAPABILITIES.registerEntityCapability(
            "trident_slot", TridentSlotCapability.class, TridentSlotCapability::new, ThrownTrident.class);
    public static final EntityCapabilityKey<LivingEntity, LastDamageSourceCapability> LAST_DAMAGE_SOURCE_CAPABILITY = CAPABILITIES.registerEntityCapability(
            "last_damage_source", LastDamageSourceCapability.class, LastDamageSourceCapability::new,
            LivingEntity.class
    );

    public static void touch() {
        // NO-OP
    }
}
