package fuzs.bettertridents.init;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.HashMap;
import java.util.Map;

public class ModLootTables {
    static final Map<Identifier, ResourceKey<LootTable>> LOOT_TABLE_INJECTIONS = new HashMap<>();
    public static final ResourceKey<LootTable> ELDER_GUARDIAN_INJECTION = registerLootTableInjection(EntityType.ELDER_GUARDIAN.getDefaultLootTable()
            .orElseThrow());

    public static void bootstrap() {
        // NO-OP
    }

    static ResourceKey<LootTable> registerLootTableInjection(ResourceKey<LootTable> resourceKey) {
        ResourceKey<LootTable> newResourceKey = ModRegistry.REGISTRIES.makeResourceKey(Registries.LOOT_TABLE,
                "inject/" + resourceKey.identifier().getPath());
        LOOT_TABLE_INJECTIONS.put(resourceKey.identifier(), newResourceKey);
        return newResourceKey;
    }

    public static void onLootTableLoad(Identifier identifier, LootTable.Builder lootTable, HolderLookup.Provider registries) {
        if (LOOT_TABLE_INJECTIONS.containsKey(identifier)) {
            lootTable.withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(NestedLootTable.lootTableReference(LOOT_TABLE_INJECTIONS.get(identifier))));
        }
    }
}
