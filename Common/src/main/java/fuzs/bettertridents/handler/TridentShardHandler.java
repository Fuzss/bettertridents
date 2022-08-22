package fuzs.bettertridents.handler;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.config.ServerConfig;
import fuzs.bettertridents.init.ModRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.Consumer;

public class TridentShardHandler {

    public void onLootTableLoad(LootTables lootManager, ResourceLocation id, Consumer<LootPool> tableBuilder) {
        if (!BetterTridents.CONFIG.get(ServerConfig.class).tridentFragmentDrop) return;
        if (EntityType.ELDER_GUARDIAN.getDefaultLootTable().equals(id)) {
            tableBuilder.accept(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ModRegistry.TRIDENT_FRAGMENT_ITEM.get())).build());
        }
    }
}
