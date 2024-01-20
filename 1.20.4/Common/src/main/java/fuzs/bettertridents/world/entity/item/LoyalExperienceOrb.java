package fuzs.bettertridents.world.entity.item;

import fuzs.bettertridents.handler.LoyalDropsHandler;
import fuzs.bettertridents.init.ModRegistry;
import fuzs.bettertridents.mixin.accessor.ExperienceOrbAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class LoyalExperienceOrb extends ExperienceOrb {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(LoyalItemEntity.class, EntityDataSerializers.BYTE);

    @Nullable
    private UUID owner;

    public LoyalExperienceOrb(Level level, double pX, double pY, double pZ, int pValue, UUID owner, int loyaltyLevel) {
        super(ModRegistry.LOYAL_EXPERIENCE_ORB_ENTITY_TYPE.get(), level);
        this.setPos(pX, pY, pZ);
        this.setYRot((float)(this.random.nextDouble() * 360.0D));
        this.setDeltaMovement((this.random.nextDouble() * (double)0.2F - (double)0.1F) * 2.0D, this.random.nextDouble() * 0.2D * 2.0D, (this.random.nextDouble() * (double)0.2F - (double)0.1F) * 2.0D);
        ((ExperienceOrbAccessor) this).setValue(pValue);
        this.owner = owner;
        if (loyaltyLevel < 1) throw new IllegalStateException("Loyalty level missing from loyal item entity, was %s".formatted(loyaltyLevel));
        this.entityData.set(ID_LOYALTY, (byte) loyaltyLevel);
    }

    public LoyalExperienceOrb(EntityType<? extends ExperienceOrb> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte) 0);
    }

    @Override
    public void tick() {
        Player player;
        if (this.owner != null) {
            player = LoyalDropsHandler.isAcceptableReturnOwner(this.level(), this.level().getPlayerByUUID(this.owner));
        } else {
            player = null;
        }
        if (player != null) {
            LoyalDropsHandler.tickLoyalEntity(this, player, this.entityData.get(ID_LOYALTY));

            // allow this to age, just in case something is wrong, so we don't stay in the world forever
            ((ExperienceOrbAccessor) this).setAge(((ExperienceOrbAccessor) this).getAge() + 1);
            if (((ExperienceOrbAccessor) this).getAge() >= 6000) {
                this.discard();
            }
        } else if (!this.level().isClientSide) {
            super.tick();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.owner != null) {
            compound.putUUID("Owner", this.owner);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("Owner")) {
            this.owner = compound.getUUID("Owner");
        }
    }
}
