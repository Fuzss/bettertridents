package fuzs.bettertridents.handler;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.config.ServerConfig;
import fuzs.bettertridents.init.ModRegistry;
import fuzs.bettertridents.world.entity.item.LoyalExperienceOrb;
import fuzs.bettertridents.world.entity.item.LoyalItemEntity;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import fuzs.puzzleslib.api.item.v2.EnchantingHelper;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

public class LoyalDropsHandler {

    public static EventResult onLivingDrops(LivingEntity entity, DamageSource damageSource, Collection<ItemEntity> drops, boolean recentlyHit) {
        if (!BetterTridents.CONFIG.get(ServerConfig.class).loyaltyCapturesDrops) return EventResult.PASS;
        int loyaltyLevel = getLoyaltyLevel(damageSource);
        if (loyaltyLevel > 0) {
            for (ItemEntity itemEntity : drops) {
                itemEntity = new LoyalItemEntity(itemEntity, damageSource.getEntity(), loyaltyLevel);
                entity.level().addFreshEntity(itemEntity);
            }
            return EventResult.INTERRUPT;
        } else {
            return EventResult.PASS;
        }
    }

    public static EventResult onLivingExperienceDrop(LivingEntity entity, @Nullable Player attackingPlayer, MutableInt droppedExperience) {
        if (!BetterTridents.CONFIG.get(ServerConfig.class).loyaltyCapturesDrops) return EventResult.PASS;
        DamageSource damageSource = ModRegistry.LAST_DAMAGE_SOURCE_ATTACHMENT_TYPE.get(entity);
        if (damageSource != null && damageSource.getEntity() instanceof Player player && !entity.level()
                .isClientSide()) {
            int loyaltyLevel = getLoyaltyLevel(damageSource);
            if (loyaltyLevel > 0) {
                awardExperienceOrbs((ServerLevel) entity.level(),
                        entity.position(),
                        droppedExperience.getAsInt(),
                        player,
                        loyaltyLevel);
                return EventResult.INTERRUPT;
            }
        }
        return EventResult.PASS;
    }

    private static int getLoyaltyLevel(DamageSource source) {
        if (source.getEntity() instanceof Player player) {
            if (source.getDirectEntity() instanceof ThrownTrident trident) {
                return trident.getEntityData().get(ThrownTrident.ID_LOYALTY);
            } else {
                Holder<Enchantment> enchantment = EnchantingHelper.lookup(player, Enchantments.LOYALTY);
                return EnchantmentHelper.getEnchantmentLevel(enchantment, player);
            }
        } else {
            return 0;
        }
    }

    public static void awardExperienceOrbs(ServerLevel level, Vec3 pos, int amount, Player player, int loyaltyLevel) {
        while (amount > 0) {
            int i = ExperienceOrb.getExperienceValue(amount);
            amount -= i;
            level.addFreshEntity(new LoyalExperienceOrb(level,
                    pos.x(),
                    pos.y(),
                    pos.z(),
                    i,
                    player.getUUID(),
                    loyaltyLevel));
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
        if (entity.level().isClientSide()) {
            entity.yOld = entity.getY();
        }
        entity.setDeltaMovement(entity.getDeltaMovement().scale(0.95).add(vec3.normalize().scale(0.05 * loyaltyLevel)));

        entity.baseTick();

        if (!entity.onGround() || entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-5F
                || (entity.tickCount + entity.getId()) % 4 == 0) {
            entity.move(MoverType.SELF, entity.getDeltaMovement());
        }

        if (!entity.level().isClientSide()) {
            if (entity.getDeltaMovement().subtract(vec3).lengthSqr() > 0.01) {
                entity.needsSync = true;
            }
        }
    }
}
