package fuzs.bettertridents.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class CommonConfig implements ConfigCore {
    @Config(
            description = "Makes the impaling enchantment apply to any creature in contact with rain or water.",
            gameRestart = true
    )
    public boolean boostImpaling = true;
    @Config(description = "Tridents can be repaired in an anvil using prismarine shards.")
    public boolean repairTridents = true;
}
