package fuzs.bettertridents.data;

import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractModelProvider;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class ModItemModelProvider extends AbstractModelProvider {

    public ModItemModelProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void registerStatesAndModels() {
        this.basicItem(ModRegistry.TRIDENT_FRAGMENT_ITEM.get());
    }
}
