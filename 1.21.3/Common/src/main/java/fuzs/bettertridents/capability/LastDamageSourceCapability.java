package fuzs.bettertridents.capability;

import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.api.capability.v3.data.CapabilityComponent;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class LastDamageSourceCapability extends CapabilityComponent<LivingEntity> {
    @Nullable
    private DamageSource damageSource;

    @Nullable
    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    public static EventResult onLivingDeath(LivingEntity entity, DamageSource source) {
        ModRegistry.LAST_DAMAGE_SOURCE_CAPABILITY.get(entity).damageSource = source;
        return EventResult.PASS;
    }

    public static EventResult onLivingDrops(LivingEntity entity, DamageSource damageSource, Collection<ItemEntity> drops, boolean recentlyHit) {
        ModRegistry.LAST_DAMAGE_SOURCE_CAPABILITY.get(entity).damageSource = null;
        return EventResult.PASS;
    }
}
