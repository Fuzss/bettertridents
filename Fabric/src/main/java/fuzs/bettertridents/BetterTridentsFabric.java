package fuzs.bettertridents;

import fuzs.bettertridents.api.event.entity.living.LivingDropsCallback;
import fuzs.bettertridents.api.event.entity.living.LivingExperienceDropCallback;
import fuzs.bettertridents.handler.LoyalDropsHandler;
import fuzs.puzzleslib.core.CoreServices;
import net.fabricmc.api.ModInitializer;

public class BetterTridentsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CoreServices.FACTORIES.modConstructor(BetterTridents.MOD_ID).accept(new BetterTridents());
        registerHandlers();
    }

    private static void registerHandlers() {
        LoyalDropsHandler loyalDropsHandler = new LoyalDropsHandler();
        LivingDropsCallback.EVENT.register(loyalDropsHandler::onLivingDrops);
        LivingExperienceDropCallback.EVENT.register(loyalDropsHandler::onLivingExperienceDrop);
    }
}
