package fuzs.bettertridents.world.entity.item;

import fuzs.bettertridents.handler.LoyalDropsHandler;
import fuzs.bettertridents.init.ModRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class LoyalItemEntity extends ItemEntity {
    private static final EntityDataAccessor<Byte> DATA_LOYALTY = SynchedEntityData.defineId(LoyalItemEntity.class,
            EntityDataSerializers.BYTE);

    public LoyalItemEntity(EntityType<? extends ItemEntity> entityType, Level level) {
        super(entityType, level);
    }

    public LoyalItemEntity(ItemEntity itemEntity, Entity thrower, int loyaltyLevel) {
        super(ModRegistry.LOYAL_ITEM_ENTITY_TYPE.value(), itemEntity.level());
        this.setItem(itemEntity.getItem().copy());
        this.copyPosition(itemEntity);
        this.age = itemEntity.getAge();
        this.bobOffs = itemEntity.bobOffs;
        this.setThrower(thrower);
        if (loyaltyLevel < 1) {
            throw new IllegalStateException("Loyalty level missing from loyal item entity, was %s".formatted(
                    loyaltyLevel));
        }
        this.getEntityData().set(DATA_LOYALTY, (byte) loyaltyLevel);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_LOYALTY, (byte) 0);
    }

    @Override
    public void tick() {
        // just let super handle discarding an empty item
        if (!this.getItem().isEmpty()) {
            if (this.getOwner() instanceof Player player && EntitySelector.NO_SPECTATORS.test(player)) {
                LoyalDropsHandler.tickLoyalEntity(this, player, this.getEntityData().get(DATA_LOYALTY));
                // allow this to age, just in case something is wrong, so we don't stay in the world forever
                if (this.getAge() != -32768) {
                    this.age++;
                }
                if (!this.level().isClientSide() && this.getAge() >= 6000) {
                    this.discard();
                }
            } else if (!this.level().isClientSide()) {
                super.tick();
            }
        } else {
            super.tick();
        }
    }
}
