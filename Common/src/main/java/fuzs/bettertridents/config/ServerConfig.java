package fuzs.bettertridents.config;

import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

public class ServerConfig implements ConfigCore {
    @Config(description = "Makes the impaling enchantment apply to any creature in contact with rain or water.")
    public boolean boostImpaling = true;
    @Config(description = "Tridents can be repaired in an anvil using prismarine shards.")
    public boolean repairTridents = true;
    @Config(description = "Tridents enchanted with loyalty will return when thrown into the void.")
    public boolean returnTridentFromVoid = true;
    @Config(description = "Tridents will be picked up in the slot they were thrown from.")
    public boolean returnTridentToSlot = true;
    @Config(description = "Elder guardians will always drop one trident fragment, you need three to craft yourself a new trident. How convenient!")
    public boolean tridentFragmentDrop = true;
}
