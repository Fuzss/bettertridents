package fuzs.bettertridents.init;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.capability.TridentSlotCapability;
import fuzs.bettertridents.capability.TridentSlotCapabilityImpl;
import fuzs.puzzleslib.capability.ForgeCapabilityController;
import fuzs.puzzleslib.capability.data.CapabilityKey;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ForgeModRegistry {
    private static final ForgeCapabilityController CAPABILITIES = ForgeCapabilityController.of(BetterTridents.MOD_ID);
    public static final CapabilityKey<TridentSlotCapability> TRIDENT_SLOT_CAPABILITY = CAPABILITIES.registerEntityCapability("trident_slot", TridentSlotCapability.class, entity -> new TridentSlotCapabilityImpl(), ThrownTrident.class, new CapabilityToken<TridentSlotCapability>() {});

    public static void touch() {

    }
}
