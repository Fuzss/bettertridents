package fuzs.bettertridents.forge;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.forge.api.capability.v3.ForgeCapabilityHelper;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(BetterTridents.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterTridentsForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(BetterTridents.MOD_ID, BetterTridents::new);
        registerCapabilities();
    }

    private static void registerCapabilities() {
        ForgeCapabilityHelper.setCapabilityToken(ModRegistry.TRIDENT_SLOT_CAPABILITY, new CapabilityToken<>() {
            // NO-OP
        });
    }
}
