package fuzs.bettertridents.mixin;

import fuzs.bettertridents.util.AquaticEnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 0), ordinal = 1)
    public float attack(float damageAmount, Entity entity) {
        return AquaticEnchantmentHelper.getAquaticDamageBonus(this.getMainHandItem(), (LivingEntity) entity, damageAmount);
    }
}
