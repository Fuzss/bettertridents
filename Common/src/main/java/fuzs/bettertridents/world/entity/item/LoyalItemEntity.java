package fuzs.bettertridents.world.entity.item;

import fuzs.bettertridents.init.ModRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class LoyalItemEntity extends ItemEntity {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(LoyalItemEntity.class, EntityDataSerializers.BYTE);

    public LoyalItemEntity(EntityType<? extends ItemEntity> entityType, Level level) {
        super(entityType, level);
    }

    public LoyalItemEntity(ItemEntity itemEntity, UUID owner, int loyaltyLevel) {
        super(ModRegistry.LOYAL_ITEM_ENTITY_TYPE.get(), itemEntity.level);
        this.setItem(itemEntity.getItem().copy());
        this.copyPosition(itemEntity);
//        this.age = itemEntity.age;
//        this.bobOffs = itemEntity.bobOffs;
        this.setOwner(owner);
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
        if (!this.getItem().isEmpty()) {
            Player owner = this.isAcceptableReturnOwner();
            if (owner != null) {
                this.xo = this.getX();
                this.yo = this.getY();
                this.zo = this.getZ();

                int loyaltyLevel = this.entityData.get(ID_LOYALTY);
                this.noPhysics = true;
                Vec3 vec3 = owner.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015 * loyaltyLevel, this.getZ());
                if (this.level.isClientSide) {
                    this.yOld = this.getY();
                }
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec3.normalize().scale(0.05 * loyaltyLevel)));

                this.baseTick();
                if (!this.onGround || this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-5F || (this.tickCount + this.getId()) % 4 == 0) {
                    this.move(MoverType.SELF, this.getDeltaMovement());
                }
                if (!this.level.isClientSide) {
                    if (this.getDeltaMovement().subtract(vec3).lengthSqr() > 0.01) {
                        this.hasImpulse = true;
                    }
                }
            } else {
                super.tick();
            }
        } else {
            super.tick();
        }
    }

    @Nullable
    private Player isAcceptableReturnOwner() {
        if (this.getOwner() != null) {
            Player player = this.level.getPlayerByUUID(this.getOwner());
            if (player == null || !player.isAlive()) {
                return null;
            } else {
                return !(player instanceof ServerPlayer) || !player.isSpectator() ? player : null;
            }
        }
        return null;
    }
}
