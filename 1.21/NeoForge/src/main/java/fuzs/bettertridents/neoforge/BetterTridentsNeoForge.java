package fuzs.bettertridents.neoforge;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.data.ModRecipeProvider;
import fuzs.bettertridents.data.client.ModLanguageProvider;
import fuzs.bettertridents.data.client.ModModelProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod(BetterTridents.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterTridentsNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(BetterTridents.MOD_ID, BetterTridents::new);
        DataProviderHelper.registerDataProviders(BetterTridents.MOD_ID, ModLanguageProvider::new, ModModelProvider::new, ModRecipeProvider::new);
    }
}
