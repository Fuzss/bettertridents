package fuzs.bettertridents.init;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.capability.TridentSlotCapability;
import fuzs.puzzleslib.capability.CapabilityController;
import fuzs.puzzleslib.capability.data.CapabilityKey;

public class ModRegistry {
    public static final CapabilityKey<TridentSlotCapability> TRIDENT_SLOT_CAPABILITY = CapabilityController.makeCapabilityKey(BetterTridents.MOD_ID, "trident_slot", TridentSlotCapability.class);

    public static void touch() {

    }
}
