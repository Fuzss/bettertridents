package fuzs.bettertridents;

import fuzs.bettertridents.capability.TridentSlotCapability;
import fuzs.bettertridents.data.ModItemModelProvider;
import fuzs.bettertridents.data.ModLanguageProvider;
import fuzs.bettertridents.data.ModRecipeProvider;
import fuzs.bettertridents.handler.TridentShardHandler;
import fuzs.bettertridents.init.ModRegistry;
import fuzs.bettertridents.mixin.accessor.ThrownTridentAccessor;
import fuzs.bettertridents.world.entity.item.LoyalItemEntity;
import fuzs.puzzleslib.capability.ForgeCapabilityController;
import fuzs.puzzleslib.core.CoreServices;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(BetterTridents.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterTridentsForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        CoreServices.FACTORIES.modConstructor(BetterTridents.MOD_ID).accept(new BetterTridents());
        registerCapabilities();
        registerHandlers();
    }

    private static void registerCapabilities() {
        ForgeCapabilityController.setCapabilityToken(ModRegistry.TRIDENT_SLOT_CAPABILITY, new CapabilityToken<TridentSlotCapability>() {});
    }

    private static void registerHandlers() {
        // run after other mods had their chance to modify loot, we just want to teleport it
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, (final LivingDropsEvent evt) -> {
            if (evt.getSource().getEntity() instanceof Player player) {
                int loyaltyLevel;
                if (evt.getSource().getDirectEntity() instanceof ThrownTrident trident) {
                    loyaltyLevel = trident.getEntityData().get(ThrownTridentAccessor.getLoyaltyId());
                } else {
                    loyaltyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOYALTY, player);
                }
                Level level = evt.getEntity().level;
                if (level.random.nextInt(3) < loyaltyLevel) {
                    for (ItemEntity itemEntity : evt.getDrops()) {
                        itemEntity = new LoyalItemEntity(itemEntity, player.getUUID(), loyaltyLevel);
                        level.addFreshEntity(itemEntity);
                    }
                    evt.setCanceled(true);
                }
            }
        });
        TridentShardHandler tridentShardHandler = new TridentShardHandler();
        MinecraftForge.EVENT_BUS.addListener((final LootTableLoadEvent evt) -> {
            tridentShardHandler.onLootTableLoad(evt.getLootTableManager(), evt.getName(), evt.getTable()::addPool);
        });
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        final ExistingFileHelper existingFileHelper = evt.getExistingFileHelper();
        generator.addProvider(true, new ModRecipeProvider(generator));
        generator.addProvider(true, new ModLanguageProvider(generator, BetterTridents.MOD_ID));
        generator.addProvider(true, new ModItemModelProvider(generator, BetterTridents.MOD_ID, existingFileHelper));
    }
}
