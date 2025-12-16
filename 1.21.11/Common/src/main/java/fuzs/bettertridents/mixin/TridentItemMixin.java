package fuzs.bettertridents.mixin;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.config.ServerConfig;
import fuzs.bettertridents.init.ModRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
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

    @ModifyVariable(method = "releaseUsing", at = @At("STORE"), ordinal = 0)
    public ThrownTrident releaseUsing(ThrownTrident thrownTrident, ItemStack itemStack, Level level, LivingEntity thrower) {
        if (!BetterTridents.CONFIG.get(ServerConfig.class).returnTridentToSlot) return thrownTrident;
        if (thrower instanceof Player player && thrower.getUseItem() == itemStack) {
            if (thrower.getUsedItemHand() == InteractionHand.OFF_HAND) {
                ModRegistry.TRIDENT_SLOT_ATTACHMENT_TYPE.set(thrownTrident, 40);
            } else {
                int selectedInventorySlot = player.getInventory().getSelectedSlot();
                ModRegistry.TRIDENT_SLOT_ATTACHMENT_TYPE.set(thrownTrident, selectedInventorySlot);
            }
        }

        return thrownTrident;
    }
}
