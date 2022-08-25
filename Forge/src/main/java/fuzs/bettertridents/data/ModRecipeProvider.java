package fuzs.bettertridents.data;

import fuzs.bettertridents.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    private final String modId;

    public ModRecipeProvider(DataGenerator p_125973_, String modId) {
        super(p_125973_);
        this.modId = modId;
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> p_176532_) {
        ShapedRecipeBuilder.shaped(Items.TRIDENT)
                .define('#', ModRegistry.TRIDENT_FRAGMENT_ITEM.get())
                // would be nice to use the Forge tag for prismarine shards here, but no separate data set is generated for Fabric, so this has to be compatible with both
                .define('P', Items.PRISMARINE_SHARD)
                .pattern(" ##")
                .pattern(" P#")
                .pattern("P  ")
                .unlockedBy("has_trident_fragment", has(ModRegistry.TRIDENT_FRAGMENT_ITEM.get()))
                .save(p_176532_, new ResourceLocation(this.modId, "trident"));
    }
}
