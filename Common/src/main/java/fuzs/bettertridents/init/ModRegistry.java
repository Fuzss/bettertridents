package fuzs.bettertridents.init;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.capability.TridentSlotCapability;
import fuzs.bettertridents.capability.TridentSlotCapabilityImpl;
import fuzs.bettertridents.world.entity.item.LoyalItemEntity;
import fuzs.puzzleslib.capability.CapabilityController;
import fuzs.puzzleslib.capability.data.CapabilityKey;
import fuzs.puzzleslib.core.CoreServices;
import fuzs.puzzleslib.init.RegistryManager;
import fuzs.puzzleslib.init.RegistryReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.ThrownTrident;

public class ModRegistry {
    private static final RegistryManager REGISTRY = CoreServices.FACTORIES.registration(BetterTridents.MOD_ID);
    public static final RegistryReference<EntityType<LoyalItemEntity>> LOYAL_ITEM_ENTITY_TYPE = REGISTRY.registerEntityTypeBuilder("loyal_item", () -> EntityType.Builder.<LoyalItemEntity>of(LoyalItemEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(20));

    private static final CapabilityController CAPABILITIES = CoreServices.FACTORIES.capabilities(BetterTridents.MOD_ID);
    public static final CapabilityKey<TridentSlotCapability> TRIDENT_SLOT_CAPABILITY = CAPABILITIES.registerEntityCapability("trident_slot", TridentSlotCapability.class, entity -> new TridentSlotCapabilityImpl(), ThrownTrident.class);

    public static void touch() {

    }
}
