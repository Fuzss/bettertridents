package fuzs.bettertridents.mixin;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.config.ServerConfig;
import fuzs.bettertridents.handler.TridentAttachmentHandler;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThrownTrident.class)
abstract class ThrownTridentMixin extends AbstractArrow {
    @Shadow
    @Final
    private static EntityDataAccessor<Byte> ID_LOYALTY;

    protected ThrownTridentMixin(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void onBelowWorld() {
        if (!BetterTridents.CONFIG.get(ServerConfig.class).returnTridentFromVoid) {
            super.onBelowWorld();
            return;
        }

        if (this.entityData.get(ID_LOYALTY) > 0 && this.isAcceptibleReturnOwner()) {
            if (this.getOwner() instanceof Player player) {
                this.setNoPhysics(true);
                this.playerTouch(player);
                return;
            }
        }

        super.onBelowWorld();
    }

    @Shadow
    private boolean isAcceptibleReturnOwner() {
        throw new RuntimeException();
    }

    @Inject(method = "tryPickup", at = @At("HEAD"), cancellable = true)
    protected void tryPickup(Player player, CallbackInfoReturnable<Boolean> callback) {
        if (!BetterTridents.CONFIG.get(ServerConfig.class).returnTridentToSlot) {
            return;
        }

        boolean addedToInventory;
        if (this.pickup == Pickup.ALLOWED) {
            addedToInventory = TridentAttachmentHandler.addItemToInventory(ThrownTrident.class.cast(this),
                    player,
                    this.getPickupItem());
        } else {
            addedToInventory = super.tryPickup(player);
        }

        if (!addedToInventory) {
            addedToInventory =
                    this.isNoPhysics() && this.ownedBy(player) && TridentAttachmentHandler.addItemToInventory(
                            ThrownTrident.class.cast(this),
                            player,
                            this.getPickupItem());
        }

        callback.setReturnValue(addedToInventory);
    }
}
