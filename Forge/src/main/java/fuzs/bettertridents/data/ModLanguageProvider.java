package fuzs.bettertridents.data;

import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractLanguageProvider;
import net.minecraft.data.PackOutput;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(PackOutput packOutput, String modId) {
        super(packOutput, modId);
    }

    @Override
    protected void addTranslations() {
        this.add(ModRegistry.TRIDENT_FRAGMENT_ITEM.get(), "Trident Fragment");
    }
}
