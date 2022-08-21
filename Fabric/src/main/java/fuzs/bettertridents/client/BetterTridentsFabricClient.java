package fuzs.bettertridents.client;

import fuzs.bettertridents.BetterTridents;
import fuzs.puzzleslib.client.core.ClientCoreServices;
import net.fabricmc.api.ClientModInitializer;

public class BetterTridentsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCoreServices.FACTORIES.clientModConstructor(BetterTridents.MOD_ID).accept(new BetterTridentsClient());
    }
}
