package fuzs.bettertridents;

import fuzs.bettertridents.handler.TridentAttachmentHandler;
import fuzs.bettertridents.config.CommonConfig;
import fuzs.bettertridents.config.ServerConfig;
import fuzs.bettertridents.data.DynamicEnchantmentRegistryProvider;
import fuzs.bettertridents.handler.LoyalDropsHandler;
import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.BuildCreativeModeTabContentsContext;
import fuzs.puzzleslib.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.FinalizeItemComponentsCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingDeathCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingDropsCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingExperienceDropCallback;
import fuzs.puzzleslib.api.event.v1.server.LootTableLoadEvents;
import fuzs.puzzleslib.api.resources.v1.DynamicPackResources;
import fuzs.puzzleslib.api.resources.v1.PackResourcesHelper;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Repairable;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;

public class BetterTridents implements ModConstructor {
    public static final String MOD_ID = "bettertridents";
    public static final String MOD_NAME = "Better Tridents";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID)
            .server(ServerConfig.class)
            .common(CommonConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        LivingDropsCallback.EVENT.register(LoyalDropsHandler::onLivingDrops);
        LivingExperienceDropCallback.EVENT.register(LoyalDropsHandler::onLivingExperienceDrop);
        LootTableLoadEvents.MODIFY.register((ResourceLocation resourceLocation, Consumer<LootPool> addLootPool, IntPredicate removeLootPool) -> {
            if (!BetterTridents.CONFIG.get(CommonConfig.class).tridentFragmentDrop) return;
            if (EntityType.ELDER_GUARDIAN.getDefaultLootTable()
                    .map(ResourceKey::location)
                    .filter(resourceLocation::equals)
                    .isPresent()) {
                addLootPool.accept(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModRegistry.TRIDENT_FRAGMENT_ITEM.value()))
                        .build());
            }
        });
        LivingDeathCallback.EVENT.register(TridentAttachmentHandler::onLivingDeath);
        LivingDropsCallback.EVENT.register(TridentAttachmentHandler::onLivingDrops);
        FinalizeItemComponentsCallback.EVENT.register((Item item, Consumer<Function<DataComponentMap, DataComponentPatch>> consumer) -> {
            if (!BetterTridents.CONFIG.getHolder(CommonConfig.class).isAvailable() ||
                    !BetterTridents.CONFIG.get(CommonConfig.class).repairTridents) {
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
    }

    @Override
    public void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsContext context) {
        context.registerBuildListener(CreativeModeTabs.INGREDIENTS, (itemDisplayParameters, output) -> {
            output.accept(ModRegistry.TRIDENT_FRAGMENT_ITEM.value());
        });
    }

    @Override
    public void onAddDataPackFinders(PackRepositorySourcesContext context) {
        if (!CONFIG.get(CommonConfig.class).boostImpaling) return;
        context.addRepositorySource(PackResourcesHelper.buildServerPack(id("boosted_impaling"),
                DynamicPackResources.create(DynamicEnchantmentRegistryProvider::new),
                true));
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
