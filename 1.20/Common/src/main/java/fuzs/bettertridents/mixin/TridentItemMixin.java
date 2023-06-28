package fuzs.bettertridents.mixin;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.config.ServerConfig;
import fuzs.bettertridents.core.CommonAbstractions;
import fuzs.bettertridents.init.ModRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
    public boolean isValidRepairItem(ItemStack itemStack, ItemStack itemStack2) {
        if (!BetterTridents.CONFIG.get(ServerConfig.class).repairTridents) return super.isValidRepairItem(itemStack, itemStack2);
        return CommonAbstractions.INSTANCE.isValidTridentRepairItem(itemStack, itemStack2);
    }

    @ModifyVariable(method = "releaseUsing", at = @At("STORE"), ordinal = 0)
    public ThrownTrident releaseUsing$storeThrownTrident(ThrownTrident trident, ItemStack stack, Level level, LivingEntity thrower) {
        if (!BetterTridents.CONFIG.get(ServerConfig.class).returnTridentToSlot) return trident;
        if (thrower.getUseItem() == stack) {
            ModRegistry.TRIDENT_SLOT_CAPABILITY.maybeGet(trident).ifPresent(capability -> {
                if (thrower.getUsedItemHand() == InteractionHand.OFF_HAND) {
                    capability.setSlot(40);
                } else {
                    capability.setSlot(((Player) thrower).getInventory().selected);
                }
            });
        }
        return trident;
    }
}
