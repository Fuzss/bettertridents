package fuzs.bettertridents;

import fuzs.bettertridents.capability.TridentSlotCapability;
import fuzs.bettertridents.data.ModItemModelProvider;
import fuzs.bettertridents.data.ModLanguageProvider;
import fuzs.bettertridents.data.ModRecipeProvider;
import fuzs.bettertridents.handler.LoyalDropsHandler;
import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.capability.ForgeCapabilityController;
import fuzs.puzzleslib.core.CoreServices;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(BetterTridents.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterTridentsForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        CoreServices.FACTORIES.modConstructor(BetterTridents.MOD_ID).accept(new BetterTridents());
        registerCapabilities();
        registerHandlers();
    }

    private static void registerCapabilities() {
        ForgeCapabilityController.setCapabilityToken(ModRegistry.TRIDENT_SLOT_CAPABILITY, new CapabilityToken<TridentSlotCapability>() {});
    }

    private static void registerHandlers() {
        LoyalDropsHandler loyalDropsHandler = new LoyalDropsHandler();
        // run after other mods had their chance to modify loot, we just want to teleport it
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, (final LivingDropsEvent evt) -> {
            loyalDropsHandler.onLivingDrops(evt.getEntity(), evt.getSource(), evt.getDrops(), evt.getLootingLevel(), evt.isRecentlyHit()).ifPresent(unit -> evt.setCanceled(true));
        });
        MinecraftForge.EVENT_BUS.addListener((final LivingExperienceDropEvent evt) -> {
            loyalDropsHandler.onLivingExperienceDrop(evt.getEntity(), evt.getAttackingPlayer(), evt.getOriginalExperience(), evt.getDroppedExperience()).ifPresent(i -> {
                if (i > 0) {
                    evt.setDroppedExperience(i);
                } else {
                    evt.setCanceled(true);
                }
            });
        });
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        final ExistingFileHelper existingFileHelper = evt.getExistingFileHelper();
        generator.addProvider(true, new ModRecipeProvider(generator, BetterTridents.MOD_ID));
        generator.addProvider(true, new ModLanguageProvider(generator, BetterTridents.MOD_ID));
        generator.addProvider(true, new ModItemModelProvider(generator, BetterTridents.MOD_ID, existingFileHelper));
    }
}
