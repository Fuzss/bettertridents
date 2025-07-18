package fuzs.bettertridents.neoforge;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.data.ModEntityInjectionLootProvider;
import fuzs.bettertridents.data.ModRecipeProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.fml.common.Mod;

@Mod(BetterTridents.MOD_ID)
public class BetterTridentsNeoForge {

    public BetterTridentsNeoForge() {
        ModConstructor.construct(BetterTridents.MOD_ID, BetterTridents::new);
        DataProviderHelper.registerDataProviders(BetterTridents.MOD_ID,
                ModEntityInjectionLootProvider::new,
                ModRecipeProvider::new);
    }
}
