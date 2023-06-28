package fuzs.bettertridents.mixin;

import fuzs.bettertridents.world.entity.LastDamageSourceEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity implements LastDamageSourceEntity {
    @Unique
    private DamageSource bettertridents$lastDamageSource;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public @Nullable DamageSource bettertridents$getLastDamageSource() {
        return this.bettertridents$lastDamageSource;
    }

    @Inject(method = "dropAllDeathLoot", at = @At("HEAD"))
    protected void dropAllDeathLoot$inject$head$1(DamageSource pDamageSource, CallbackInfo callback) {
        this.bettertridents$lastDamageSource = pDamageSource;
    }

    @Inject(method = "dropAllDeathLoot", at = @At("TAIL"))
    protected void dropAllDeathLoot$inject$tail$1(DamageSource pDamageSource, CallbackInfo callback) {
        this.bettertridents$lastDamageSource = null;
    }
}
