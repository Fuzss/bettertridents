package fuzs.bettertridents.config;

import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

public class CommonConfig implements ConfigCore {
    @Config(description = "Elder guardians will always drop one trident fragment, you need three to craft yourself a new trident. How convenient!")
    public boolean tridentFragmentDrop = true;
}
