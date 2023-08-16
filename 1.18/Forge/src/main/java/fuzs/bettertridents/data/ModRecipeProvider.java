package fuzs.bettertridents.data;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.util.function.Consumer;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> exporter) {
        ShapedRecipeBuilder.shaped(Items.TRIDENT)
                .define('#', ModRegistry.TRIDENT_FRAGMENT_ITEM.get())
                // would be nice to use the Forge tag for prismarine shards here, but no separate data set is generated for Fabric, so this has to be compatible with both
                .define('P', Items.PRISMARINE_SHARD)
                .pattern(" ##")
                .pattern(" P#")
                .pattern("P  ")
                .unlockedBy("has_trident_fragment", has(ModRegistry.TRIDENT_FRAGMENT_ITEM.get()))
                .save(exporter, BetterTridents.id("trident"));
    }
}
