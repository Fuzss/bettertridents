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
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.function.Consumer;

public class BetterTridentsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CoreServices.FACTORIES.modConstructor(BetterTridents.MOD_ID).accept(new BetterTridents());
        registerHandlers();
    }

    private static void registerHandlers() {
        TridentShardHandler tridentShardHandler = new TridentShardHandler();
        LootTableEvents.REPLACE.register((ResourceManager resourceManager, LootTables lootManager, ResourceLocation id, LootTable original, LootTableSource source) -> {
            if (source != LootTableSource.DATA_PACK) {
                MutableObject<LootTable> replacement = new MutableObject<>();
                tridentShardHandler.onLootTableReplacement(lootManager, id, original, replacement::setValue);
                return replacement.getValue();
            }
            return null;
        });
        LootTableEvents.MODIFY.register((ResourceManager resourceManager, LootTables lootManager, ResourceLocation id, LootTable.Builder tableBuilder, LootTableSource source) -> {
            if (source != LootTableSource.DATA_PACK) tridentShardHandler.onLootTableModification(lootManager, id, tableBuilder::pool, index -> {
                MutableInt counter = new MutableInt();
                MutableBoolean result = new MutableBoolean();
                tableBuilder.modifyPools(builder -> {
                    if (index == counter.getAndIncrement()) {
                        builder.setRolls(ConstantValue.exactly(0.0F));
                        builder.setBonusRolls(ConstantValue.exactly(0.0F));
                        result.setTrue();
                    }
                });
                return result.booleanValue();
            });
        });
    }
}
