package fuzs.bettertridents.neoforge;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.data.ModDatapackRegistriesProvider;
import fuzs.bettertridents.data.ModRecipeProvider;
import fuzs.bettertridents.data.loot.ModEntityInjectionLootProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.minecraft.server.packs.PackType;
import net.neoforged.fml.common.Mod;

@Mod(BetterTridents.MOD_ID)
public class BetterTridentsNeoForge {

    public BetterTridentsNeoForge() {
        ModConstructor.construct(BetterTridents.MOD_ID, BetterTridents::new);
        DataProviderHelper.registerDataProviders(BetterTridents.MOD_ID, ModEntityInjectionLootProvider::new);
        DataProviderHelper.registerDataProviders(BetterTridents.BOOSTED_IMPALING_ID,
                PackType.SERVER_DATA,
                ModDatapackRegistriesProvider::new);
        DataProviderHelper.registerDataProviders(BetterTridents.TRIDENT_RECIPE_ID,
                PackType.SERVER_DATA,
                ModRecipeProvider::new);
    }
}
