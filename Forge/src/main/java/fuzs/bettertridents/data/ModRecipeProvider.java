package fuzs.bettertridents.data;

import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.core.CoreServices;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(DataGenerator p_125973_) {
        super(p_125973_);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> p_176532_) {
        ShapedRecipeBuilder.shaped(Items.TRIDENT)
                .define('#', ModRegistry.TRIDENT_FRAGMENT_ITEM.get())
                .define('P', CoreServices.ENVIRONMENT.getModLoader().isForge() ? Ingredient.of(Tags.Items.DUSTS_PRISMARINE) : Ingredient.of(Items.PRISMARINE_SHARD))
                .pattern(" ##")
                .pattern(" P#")
                .pattern("P  ")
                .unlockedBy("has_trident_fragment", has(ModRegistry.TRIDENT_FRAGMENT_ITEM.get()))
                .save(p_176532_);
        ShapedRecipeBuilder.shaped(Items.TRIDENT)
                .define('#', ModRegistry.TRIDENT_FRAGMENT_ITEM.get())
                .define('P', CoreServices.ENVIRONMENT.getModLoader().isForge() ? Ingredient.of(Tags.Items.DUSTS_PRISMARINE) : Ingredient.of(Items.PRISMARINE_SHARD))
                .pattern("## ")
                .pattern("#P ")
                .pattern("  P")
                .unlockedBy("has_trident_fragment", has(ModRegistry.TRIDENT_FRAGMENT_ITEM.get()))
                .save(p_176532_);
    }
}
