package fuzs.bettertridents.client;

import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.BuildCreativeModeTabContentsContext;
import fuzs.puzzleslib.api.client.core.v1.context.EntityRenderersContext;
import net.minecraft.client.renderer.entity.ExperienceOrbRenderer;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.world.item.CreativeModeTabs;

public class BetterTridentsClient implements ClientModConstructor {

    @Override
    public void onRegisterEntityRenderers(EntityRenderersContext context) {
        context.registerEntityRenderer(ModRegistry.LOYAL_ITEM_ENTITY_TYPE.get(), ItemEntityRenderer::new);
        context.registerEntityRenderer(ModRegistry.LOYAL_EXPERIENCE_ORB_ENTITY_TYPE.get(), ExperienceOrbRenderer::new);
    }

    @Override
    public void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsContext context) {
        context.registerBuildListener(CreativeModeTabs.INGREDIENTS, (featureFlagSet, output, bl) -> {
            output.accept(ModRegistry.TRIDENT_FRAGMENT_ITEM.get());
        });
    }
}
