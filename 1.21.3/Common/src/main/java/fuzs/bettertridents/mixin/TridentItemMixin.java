package fuzs.bettertridents.mixin;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.capability.TridentSlotCapability;
import fuzs.bettertridents.config.ServerConfig;
import fuzs.bettertridents.init.ModRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TridentItem.class)
abstract class TridentItemMixin extends Item {

    public TridentItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        if (!BetterTridents.CONFIG.get(ServerConfig.class).repairTridents) {
            return super.isValidRepairItem(stack, repairCandidate);
        } else {
            return repairCandidate.is(Items.PRISMARINE_SHARD);
        }
    }

    @ModifyVariable(method = "releaseUsing", at = @At("STORE"), ordinal = 0)
    public ThrownTrident releaseUsing(ThrownTrident trident, ItemStack stack, Level level, LivingEntity thrower) {
        if (!BetterTridents.CONFIG.get(ServerConfig.class).returnTridentToSlot) return trident;
        if (thrower.getUseItem() == stack) {
            TridentSlotCapability capability = ModRegistry.TRIDENT_SLOT_CAPABILITY.get(trident);
            if (thrower.getUsedItemHand() == InteractionHand.OFF_HAND) {
                capability.setSlot(40);
            } else {
                capability.setSlot(((Player) thrower).getInventory().selected);
            }
        }

        return trident;
    }
}
