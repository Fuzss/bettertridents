package fuzs.bettertridents.world.entity.item;

import fuzs.bettertridents.handler.LoyalDropsHandler;
import fuzs.bettertridents.init.ModRegistry;
import fuzs.bettertridents.mixin.accessor.ItemEntityAccessor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class LoyalItemEntity extends ItemEntity {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(LoyalItemEntity.class, EntityDataSerializers.BYTE);

    public LoyalItemEntity(EntityType<? extends ItemEntity> entityType, Level level) {
        super(entityType, level);
    }

    public LoyalItemEntity(ItemEntity itemEntity, UUID owner, int loyaltyLevel) {
        super(ModRegistry.LOYAL_ITEM_ENTITY_TYPE.get(), itemEntity.level());
        this.setItem(itemEntity.getItem().copy());
        this.copyPosition(itemEntity);
        ((ItemEntityAccessor) this).setAge(itemEntity.getAge());
        ((ItemEntityAccessor) this).setBobOffs(itemEntity.bobOffs);
        this.setThrower(owner);
        if (loyaltyLevel < 1) throw new IllegalStateException("Loyalty level missing from loyal item entity, was %s".formatted(loyaltyLevel));
        this.entityData.set(ID_LOYALTY, (byte) loyaltyLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte) 0);
    }

    @Override
    public void tick() {
        // just let super handle discarding an empty item
        if (!this.getItem().isEmpty()) {
            Player owner = LoyalDropsHandler.isAcceptableReturnOwner(this.level(), this.getOwner());
            if (owner != null) {
                LoyalDropsHandler.tickLoyalEntity(this, owner, this.entityData.get(ID_LOYALTY));

                // allow this to age, just in case something is wrong, so we don't stay in the world forever
                if (this.getAge() != -32768) {
                    ((ItemEntityAccessor) this).setAge(this.getAge() + 1);
                }
                if (!this.level().isClientSide && this.getAge() >= 6000) {
                    this.discard();
                }
            } else {
                super.tick();
            }
        } else {
            super.tick();
        }
    }
}
