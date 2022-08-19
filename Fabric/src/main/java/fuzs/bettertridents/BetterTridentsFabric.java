package fuzs.bettertridents;

import fuzs.bettertridents.init.FabricModRegistry;
import fuzs.puzzleslib.core.CoreServices;
import net.fabricmc.api.ModInitializer;

public class BetterTridentsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CoreServices.FACTORIES.modConstructor(BetterTridents.MOD_ID).accept(new BetterTridents());
        FabricModRegistry.touch();
    }
}
