package fuzs.bettertridents.handler;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.config.CommonConfig;
import fuzs.bettertridents.init.ModRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.Consumer;
import java.util.function.IntPredicate;

public class TridentShardHandler {

    public void onLootTableReplacement(LootTables lootManager, ResourceLocation id, LootTable original, Consumer<LootTable> lootTableSetter) {

    }

    public void onLootTableModification(LootTables lootManager, ResourceLocation id, Consumer<LootPool> lootPoolAdder, IntPredicate lootPoolRemover) {
        if (!BetterTridents.CONFIG.get(CommonConfig.class).tridentFragmentDrop) return;
        if (EntityType.ELDER_GUARDIAN.getDefaultLootTable().equals(id)) {
            lootPoolAdder.accept(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ModRegistry.TRIDENT_FRAGMENT_ITEM.get())).build());
        }
        if (EntityType.CREEPER.getDefaultLootTable().equals(id) || EntityType.BLAZE.getDefaultLootTable().equals(id)) {
            lootPoolRemover.test(0);
        }
    }
}
