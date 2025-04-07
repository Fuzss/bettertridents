package fuzs.bettertridents.data.client;

import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder builder) {
        builder.add(ModRegistry.TRIDENT_FRAGMENT_ITEM.value(), "Trident Fragment");
        builder.add(ModRegistry.LOYAL_ITEM_ENTITY_TYPE.value(), "Loyal Item");
        builder.add(ModRegistry.LOYAL_EXPERIENCE_ORB_ENTITY_TYPE.value(), "Loyal Experience Orb");
    }
}
