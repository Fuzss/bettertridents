package fuzs.bettertridents.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ServerConfig implements ConfigCore {
    @Config(description = "Tridents can be repaired in an anvil using prismarine shards.")
    public boolean repairTridents = true;
    @Config(description = "Tridents enchanted with loyalty will return when thrown into the void.")
    public boolean returnTridentFromVoid = true;
    @Config(description = "Tridents will be picked up in the slot they were thrown from.")
    public boolean returnTridentToSlot = true;
    @Config(description = "Tridents enchanted with loyalty bring drops and xp from the entity they have killed to the player (both melee and ranged combat).")
    public boolean loyaltyCapturesDrops = true;
}
