package fuzs.bettertridents.fabric.client;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.client.BetterTridentsClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class BetterTridentsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(BetterTridents.MOD_ID, BetterTridentsClient::new);
    }
}
