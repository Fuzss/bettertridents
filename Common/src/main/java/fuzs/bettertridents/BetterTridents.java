package fuzs.bettertridents;

import fuzs.bettertridents.config.CommonConfig;
import fuzs.bettertridents.config.ServerConfig;
import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.core.CoreServices;
import fuzs.puzzleslib.core.ModConstructor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterTridents implements ModConstructor {
    public static final String MOD_ID = "bettertridents";
    public static final String MOD_NAME = "Better Tridents";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @SuppressWarnings("Convert2MethodRef")
    public static final ConfigHolder CONFIG = CoreServices.FACTORIES
            .serverConfig(ServerConfig.class, () -> new ServerConfig())
            .commonConfig(CommonConfig.class, () -> new CommonConfig());

    @Override
    public void onConstructMod() {
        CONFIG.bakeConfigs(MOD_ID);
        ModRegistry.touch();
    }

    @Override
    public void onLootTableModification(LootTablesModifyContext context) {
        if (!BetterTridents.CONFIG.get(CommonConfig.class).tridentFragmentDrop) return;
        if (EntityType.ELDER_GUARDIAN.getDefaultLootTable().equals(context.getId())) {
            context.addLootPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ModRegistry.TRIDENT_FRAGMENT_ITEM.get())).build());
        }
    }
}
