package fuzs.bettertridents.world.item.enchantment;

import fuzs.bettertridents.BetterTridents;
import fuzs.bettertridents.config.ServerConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class ModEnchantmentHelper {

    public static float getAquaticDamageBonus(ItemStack stack, LivingEntity entity, float damageAmount) {
        if (!BetterTridents.CONFIG.get(ServerConfig.class).boostImpaling) return damageAmount;
        if (entity.getMobType() != MobType.WATER && entity.isInWaterRainOrBubble()) {
            return damageAmount + EnchantmentHelper.getDamageBonus(stack, MobType.WATER);
        }
        return damageAmount;
    }
}
