package fuzs.bettertridents;

import fuzs.bettertridents.init.ForgeModRegistry;
import fuzs.puzzleslib.core.CoreServices;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(BetterTridents.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterTridentsForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        CoreServices.FACTORIES.modConstructor(BetterTridents.MOD_ID).accept(new BetterTridents());
        ForgeModRegistry.touch();
    }
}
