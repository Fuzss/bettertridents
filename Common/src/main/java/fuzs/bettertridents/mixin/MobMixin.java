package fuzs.bettertridents.mixin;

import fuzs.bettertridents.world.item.enchantment.ModEnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyVariable(method = "doHurtTarget", at = @At(value = "STORE", ordinal = 1), ordinal = 0)
    public float doHurtTarget$modifyVariable$store(float damageAmount, Entity entity) {
        return ModEnchantmentHelper.getAquaticDamageBonus(this.getMainHandItem(), (LivingEntity) entity, damageAmount);
    }
}
