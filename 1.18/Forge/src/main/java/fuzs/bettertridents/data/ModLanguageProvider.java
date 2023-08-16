package fuzs.bettertridents.data;

import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractLanguageProvider;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void addTranslations() {
        this.add(ModRegistry.TRIDENT_FRAGMENT_ITEM.get(), "Trident Fragment");
    }
}
