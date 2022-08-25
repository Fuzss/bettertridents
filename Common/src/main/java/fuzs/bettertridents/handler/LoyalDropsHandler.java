package fuzs.bettertridents.handler;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.config.ServerConfig;
import fuzs.bettertridents.mixin.accessor.ThrownTridentAccessor;
import fuzs.bettertridents.world.entity.LastDamageSourceEntity;
import fuzs.bettertridents.world.entity.item.LoyalExperienceOrb;
import fuzs.bettertridents.world.entity.item.LoyalItemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

public class LoyalDropsHandler {

    public Optional<Unit> onLivingDrops(LivingEntity entity, DamageSource source, Collection<ItemEntity> drops, int lootingLevel, boolean recentlyHit) {
        if (!BetterTridents.CONFIG.get(ServerConfig.class).loyaltyCapturesDrops) return Optional.empty();
        int loyaltyLevel = this.getLoyaltyLevel(source);
        if (loyaltyLevel > 0) {
            for (ItemEntity itemEntity : drops) {
                itemEntity = new LoyalItemEntity(itemEntity, source.getEntity().getUUID(), loyaltyLevel);
                entity.level.addFreshEntity(itemEntity);
            }
            return Optional.of(Unit.INSTANCE);
        }
        return Optional.empty();
    }

    public OptionalInt onLivingExperienceDrop(LivingEntity entity, @Nullable Player attackingPlayer, int originalExperience, int droppedExperience) {
        if (!BetterTridents.CONFIG.get(ServerConfig.class).loyaltyCapturesDrops) return OptionalInt.empty();
        DamageSource source = ((LastDamageSourceEntity) entity).custom$getLastDamageSource();
        if (source != null) {
            int loyaltyLevel = this.getLoyaltyLevel(source);
            if (loyaltyLevel > 0) {
                this.awardExperienceOrbs((ServerLevel) entity.level, entity.position(), droppedExperience, (Player) source.getEntity(), loyaltyLevel);
                return OptionalInt.of(0);
            }
        }
        return OptionalInt.empty();
    }

    private int getLoyaltyLevel(DamageSource source) {
        if (source.getEntity() instanceof Player player) {
            if (source.getDirectEntity() instanceof ThrownTrident trident) {
                return trident.getEntityData().get(ThrownTridentAccessor.getLoyaltyId());
            } else {
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.LOYALTY, player);
            }
        }
        return 0;
    }

    public void awardExperienceOrbs(ServerLevel pLevel, Vec3 pPos, int pAmount, Player player, int loyaltyLevel) {
        while (pAmount > 0) {
            int i = ExperienceOrb.getExperienceValue(pAmount);
            pAmount -= i;
            pLevel.addFreshEntity(new LoyalExperienceOrb(pLevel, pPos.x(), pPos.y(), pPos.z(), i, player.getUUID(), loyaltyLevel));
        }
    }

    public static void tickLoyalEntity(Entity entity, Player owner, int loyaltyLevel) {
        // mostly copied from trident tick and item entity tick
        entity.xo = entity.getX();
        entity.yo = entity.getY();
        entity.zo = entity.getZ();

        entity.noPhysics = true;
        Vec3 vec3 = owner.getEyePosition().subtract(entity.position());
        entity.setPosRaw(entity.getX(), entity.getY() + vec3.y * 0.015 * loyaltyLevel, entity.getZ());
        if (entity.level.isClientSide) {
            entity.yOld = entity.getY();
        }
        entity.setDeltaMovement(entity.getDeltaMovement().scale(0.95).add(vec3.normalize().scale(0.05 * loyaltyLevel)));

        entity.baseTick();
        if (!entity.isOnGround() || entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-5F || (entity.tickCount + entity.getId()) % 4 == 0) {
            entity.move(MoverType.SELF, entity.getDeltaMovement());
        }
        if (!entity.level.isClientSide) {
            if (entity.getDeltaMovement().subtract(vec3).lengthSqr() > 0.01) {
                entity.hasImpulse = true;
            }
        }
    }

    @Nullable
    public static Player isAcceptableReturnOwner(Level level, @Nullable UUID owner) {
        if (owner != null) {
            Player player = level.getPlayerByUUID(owner);
            if (player == null || !player.isAlive()) {
                return null;
            } else {
                return !(player instanceof ServerPlayer) || !player.isSpectator() ? player : null;
            }
        }
        return null;
    }
}
