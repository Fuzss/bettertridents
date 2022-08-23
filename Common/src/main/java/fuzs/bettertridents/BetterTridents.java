package fuzs.bettertridents;

import fuzs.bettertridents.config.CommonConfig;
import fuzs.bettertridents.config.ServerConfig;
import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.core.CoreServices;
import fuzs.puzzleslib.core.ModConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterTridents implements ModConstructor {
    public static final String MOD_ID = "bettertridents";
    public static final String MOD_NAME = "Better Tridents";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @SuppressWarnings("Convert2MethodRef")
    public static final ConfigHolder CONFIG = CoreServices.FACTORIES
            .serverConfig(ServerConfig.class, () -> new ServerConfig())
            .commonConfig(CommonConfig.class, () -> new CommonConfig());

    @Override
    public void onConstructMod() {
        CONFIG.bakeConfigs(MOD_ID);
        ModRegistry.touch();
    }
}
