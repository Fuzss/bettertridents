package fuzs.bettertridents;

import fuzs.bettertridents.capability.TridentSlotCapability;
import fuzs.bettertridents.data.ModItemModelProvider;
import fuzs.bettertridents.data.ModLanguageProvider;
import fuzs.bettertridents.data.ModRecipeProvider;
import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.api.capability.v2.ForgeCapabilityHelper;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod(BetterTridents.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterTridentsForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(BetterTridents.MOD_ID, BetterTridents::new);
        registerCapabilities();
    }

    private static void registerCapabilities() {
        ForgeCapabilityHelper.setCapabilityToken(ModRegistry.TRIDENT_SLOT_CAPABILITY, new CapabilityToken<TridentSlotCapability>() {});
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        evt.getGenerator().addProvider(new ModItemModelProvider(evt, BetterTridents.MOD_ID));
        evt.getGenerator().addProvider(new ModLanguageProvider(evt, BetterTridents.MOD_ID));
        evt.getGenerator().addProvider(new ModRecipeProvider(evt, BetterTridents.MOD_ID));
    }
}
