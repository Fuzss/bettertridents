package fuzs.bettertridents;

import fuzs.bettertridents.capability.TridentSlotCapability;
import fuzs.bettertridents.init.ModRegistry;
import fuzs.bettertridents.mixin.accessor.ThrownTridentAccessor;
import fuzs.bettertridents.world.entity.item.LoyalItemEntity;
import fuzs.puzzleslib.capability.ForgeCapabilityController;
import fuzs.puzzleslib.core.CoreServices;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityToken;
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
    }
}
