package fuzs.bettertridents.init;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.capability.TridentSlotCapability;
import fuzs.bettertridents.capability.TridentSlotCapabilityImpl;
import fuzs.puzzleslib.capability.FabricCapabilityController;
import fuzs.puzzleslib.capability.data.CapabilityKey;
import net.minecraft.world.entity.projectile.ThrownTrident;

public class FabricModRegistry {
    private static final FabricCapabilityController CAPABILITIES = FabricCapabilityController.of(BetterTridents.MOD_ID);
    public static final CapabilityKey<TridentSlotCapability> TRIDENT_SLOT_CAPABILITY = CAPABILITIES.registerEntityCapability("trident_slot", TridentSlotCapability.class, entity -> new TridentSlotCapabilityImpl(), ThrownTrident.class);

    public static void touch() {

    }
}
