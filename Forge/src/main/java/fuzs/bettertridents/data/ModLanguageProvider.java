package fuzs.bettertridents.data;

import fuzs.bettertridents.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String modId) {
        super(gen, modId, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(ModRegistry.TRIDENT_FRAGMENT_ITEM.get(), "Trident Fragment");
    }
}
