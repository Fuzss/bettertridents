package fuzs.bettertridents.data;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractRecipeProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(this.items(), RecipeCategory.COMBAT, Items.TRIDENT)
                .define('#', ModRegistry.TRIDENT_FRAGMENT_ITEM.value())
                .define('P', Items.PRISMARINE_SHARD)
                .pattern(" ##")
                .pattern(" P#")
                .pattern("P  ")
                .unlockedBy(getHasName(ModRegistry.TRIDENT_FRAGMENT_ITEM.value()),
                        this.has(ModRegistry.TRIDENT_FRAGMENT_ITEM.value()))
                .save(recipeOutput, ResourceKey.create(Registries.RECIPE, BetterTridents.id("trident")));
    }
}
