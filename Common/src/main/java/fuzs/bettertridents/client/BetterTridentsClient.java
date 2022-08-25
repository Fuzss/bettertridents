package fuzs.bettertridents.client;

import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.client.core.ClientModConstructor;
import net.minecraft.client.renderer.entity.ExperienceOrbRenderer;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;

public class BetterTridentsClient implements ClientModConstructor {

    @Override
    public void onRegisterEntityRenderers(EntityRenderersContext context) {
        context.registerEntityRenderer(ModRegistry.LOYAL_ITEM_ENTITY_TYPE.get(), ItemEntityRenderer::new);
        context.registerEntityRenderer(ModRegistry.LOYAL_EXPERIENCE_ORB_ENTITY_TYPE.get(), ExperienceOrbRenderer::new);
    }
}
