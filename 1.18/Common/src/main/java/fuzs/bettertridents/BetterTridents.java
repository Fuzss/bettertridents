package fuzs.bettertridents;

import fuzs.bettertridents.config.CommonConfig;
import fuzs.bettertridents.config.ServerConfig;
import fuzs.bettertridents.handler.LoyalDropsHandler;
import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.BuildCreativeModeTabContentsContext;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingDropsCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingExperienceDropCallback;
import fuzs.puzzleslib.api.event.v1.server.LootTableLoadEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterTridents implements ModConstructor {
    public static final String MOD_ID = "bettertridents";
    public static final String MOD_NAME = "Better Tridents";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).server(ServerConfig.class).common(CommonConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.touch();
        registerHandlers();
    }

    private static void registerHandlers() {
        LivingDropsCallback.EVENT.register(LoyalDropsHandler::onLivingDrops);
        LivingExperienceDropCallback.EVENT.register(LoyalDropsHandler::onLivingExperienceDrop);
        LootTableLoadEvents.MODIFY.register((lootManager, identifier, addPool, removePool) -> {
            if (!BetterTridents.CONFIG.get(CommonConfig.class).tridentFragmentDrop) return;
            if (EntityType.ELDER_GUARDIAN.getDefaultLootTable().equals(identifier)) {
                addPool.accept(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ModRegistry.TRIDENT_FRAGMENT_ITEM.get())).build());
            }
        });
    }

    @Override
    public void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsContext context) {
        context.registerBuildListener(CreativeModeTab.TAB_MATERIALS, (output) -> {
            output.accept(ModRegistry.TRIDENT_FRAGMENT_ITEM.get());
        });
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
