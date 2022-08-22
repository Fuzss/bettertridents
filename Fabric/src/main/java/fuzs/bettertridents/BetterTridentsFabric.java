package fuzs.bettertridents;

import fuzs.bettertridents.handler.TridentShardHandler;
import fuzs.puzzleslib.core.CoreServices;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;

public class BetterTridentsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CoreServices.FACTORIES.modConstructor(BetterTridents.MOD_ID).accept(new BetterTridents());
        registerHandlers();
    }

    private static void registerHandlers() {
        TridentShardHandler tridentShardHandler = new TridentShardHandler();
        LootTableEvents.MODIFY.register((ResourceManager resourceManager, LootTables lootManager, ResourceLocation id, LootTable.Builder tableBuilder, LootTableSource source) -> {
            if (source != LootTableSource.DATA_PACK) tridentShardHandler.onLootTableLoad(lootManager, id, tableBuilder::pool);
        });
    }
}
