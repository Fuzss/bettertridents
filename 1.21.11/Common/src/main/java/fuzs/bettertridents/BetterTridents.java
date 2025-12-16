package fuzs.bettertridents;

import fuzs.bettertridents.config.CommonConfig;
import fuzs.bettertridents.config.ServerConfig;
import fuzs.bettertridents.handler.LoyalDropsHandler;
import fuzs.bettertridents.handler.TridentAttachmentHandler;
import fuzs.bettertridents.init.ModLootTables;
import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.api.event.v1.BuildCreativeModeTabContentsCallback;
import fuzs.puzzleslib.api.event.v1.FinalizeItemComponentsCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingDeathCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingDropsCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingExperienceDropCallback;
import fuzs.puzzleslib.api.event.v1.server.LootTableLoadCallback;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Repairable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Function;

public class BetterTridents implements ModConstructor {
    public static final String MOD_ID = "bettertridents";
    public static final String MOD_NAME = "Better Tridents";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID)
            .server(ServerConfig.class)
            .common(CommonConfig.class);
    public static final Identifier BOOSTED_IMPALING_ID = id("boosted_impaling");
    public static final Identifier TRIDENT_RECIPE_ID = id("trident_recipe");

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        LivingDropsCallback.EVENT.register(LoyalDropsHandler::onLivingDrops);
        LivingExperienceDropCallback.EVENT.register(LoyalDropsHandler::onLivingExperienceDrop);
        LootTableLoadCallback.EVENT.register(ModLootTables::onLootTableLoad);
        LivingDeathCallback.EVENT.register(TridentAttachmentHandler::onLivingDeath);
        LivingDropsCallback.EVENT.register(TridentAttachmentHandler::onLivingDrops);
        FinalizeItemComponentsCallback.EVENT.register((Item item, Consumer<Function<DataComponentMap, DataComponentPatch>> consumer) -> {
            if (!BetterTridents.CONFIG.getHolder(CommonConfig.class).isAvailable() || !BetterTridents.CONFIG.get(
                    CommonConfig.class).repairTridents) {
                return;
            }

            if (item == Items.TRIDENT) {
                consumer.accept((DataComponentMap dataComponents) -> {
                    return DataComponentPatch.builder()
                            .set(DataComponents.REPAIRABLE,
                                    new Repairable(HolderSet.direct(Items.PRISMARINE_SHARD.builtInRegistryHolder())))
                            .build();
                });
            }
        });
        BuildCreativeModeTabContentsCallback.buildCreativeModeTabContents(CreativeModeTabs.INGREDIENTS)
                .register((CreativeModeTab creativeModeTab, CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) -> {
                    output.accept(ModRegistry.TRIDENT_FRAGMENT_ITEM.value());
                });
    }

    @Override
    public void onAddDataPackFinders(PackRepositorySourcesContext context) {
        context.registerBuiltInPack(BOOSTED_IMPALING_ID, Component.literal("Impaling When Wet"), true);
        context.registerBuiltInPack(TRIDENT_RECIPE_ID, Component.literal("Trident Recipe"), true);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
