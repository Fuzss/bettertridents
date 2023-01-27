package fuzs.bettertridents.mixin;

import com.google.common.collect.Lists;
import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.api.event.entity.living.LivingDropsCallback;
import fuzs.bettertridents.api.event.entity.living.LivingExperienceDropCallback;
import fuzs.bettertridents.world.entity.CapturedDropsEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(LivingEntity.class)
abstract class LivingEntityFabricMixin extends Entity {
    @Shadow
    @Nullable
    protected Player lastHurtByPlayer;
    @Shadow
    protected int lastHurtByPlayerTime;

    public LivingEntityFabricMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "dropAllDeathLoot", at = @At("HEAD"))
    protected void dropAllDeathLoot$inject$head$2(DamageSource pDamageSource, CallbackInfo callback) {
        ((CapturedDropsEntity) this).custom$setCapturedDrops(Lists.newArrayList());
    }

    @Inject(method = "dropAllDeathLoot", at = @At("TAIL"))
    protected void dropAllDeathLoot(DamageSource pDamageSource, CallbackInfo callback) {
        Collection<ItemEntity> drops = ((CapturedDropsEntity) this).custom$setCapturedDrops(null);
        if (drops != null) {
            // this is bad as other mixins might modify looting level which is lost here
            // unfortunately though capturing locals does not seem to work here, even though TAIL is specified the LVT does not contain entries for killer and lootingLevel
            Entity killer = pDamageSource.getEntity();
            int lootingLevel;
            if (killer instanceof Player) {
                lootingLevel = EnchantmentHelper.getMobLooting((LivingEntity)killer);
            } else {
                lootingLevel = 0;
            }
            if (LivingDropsCallback.EVENT.invoker().onLivingDrops((LivingEntity) (Object) this, pDamageSource, drops, lootingLevel, this.lastHurtByPlayerTime > 0).isEmpty()) {
                drops.forEach(item -> this.level.addFreshEntity(item));
            }
        } else {
            BetterTridents.LOGGER.warn("Unable to invoke LivingDropsCallback for entity {}: Drops is null", this.getName().getString());
        }
    }

    @Inject(method = "dropExperience", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"), cancellable = true)
    protected void dropExperience$inject$invoke(CallbackInfo callback) {
        int experienceReward = this.getExperienceReward();
        int newExperienceReward = LivingExperienceDropCallback.EVENT.invoker().onLivingExperienceDrop((LivingEntity) (Object) this, this.lastHurtByPlayer, experienceReward, experienceReward).orElseThrow();
        if (experienceReward != newExperienceReward) {
            ExperienceOrb.award((ServerLevel) this.level, this.position(), newExperienceReward);
            callback.cancel();
        }
    }

    @Shadow
    protected abstract int getExperienceReward();
}
