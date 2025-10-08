package fuzs.bettertridents.fabric;

import fuzs.bettertridents.BetterTridents;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class BetterTridentsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(BetterTridents.MOD_ID, BetterTridents::new);
    }
}
