package fuzs.bettertridents.world.entity.item;

import fuzs.bettertridents.handler.LoyalDropsHandler;
import fuzs.bettertridents.init.ModRegistry;
import fuzs.bettertridents.mixin.accessor.ExperienceOrbAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class LoyalExperienceOrb extends ExperienceOrb {
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER = SynchedEntityData.defineId(LoyalExperienceOrb.class,
            EntityDataSerializers.OPTIONAL_UUID
    );
    private static final EntityDataAccessor<Byte> DATA_LOYALTY = SynchedEntityData.defineId(LoyalExperienceOrb.class,
            EntityDataSerializers.BYTE
    );

    public LoyalExperienceOrb(Level level, double x, double y, double z, int value, @Nullable UUID owner, int loyaltyLevel) {
        super(ModRegistry.LOYAL_EXPERIENCE_ORB_ENTITY_TYPE.value(), level);
        this.setPos(x, y, z);
        this.setYRot((float) (this.random.nextDouble() * 360.0D));
        this.setDeltaMovement((this.random.nextDouble() * (double) 0.2F - (double) 0.1F) * 2.0D,
                this.random.nextDouble() * 0.2D * 2.0D,
                (this.random.nextDouble() * (double) 0.2F - (double) 0.1F) * 2.0D
        );
        ((ExperienceOrbAccessor) this).setValue(value);
        this.getEntityData().set(DATA_OWNER, Optional.ofNullable(owner));
        this.getEntityData().set(DATA_LOYALTY, (byte) loyaltyLevel);
    }

    public LoyalExperienceOrb(EntityType<? extends ExperienceOrb> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_OWNER, Optional.empty());
        builder.define(DATA_LOYALTY, (byte) 0);
    }

    @Override
    public void tick() {
        Player player;
        if (this.getOwner() != null) {
            player = LoyalDropsHandler.isAcceptableReturnOwner(this.level(), this.level().getPlayerByUUID(this.getOwner()));
        } else {
            player = null;
        }
        if (player != null) {
            LoyalDropsHandler.tickLoyalEntity(this, player, this.getEntityData().get(DATA_LOYALTY));

            // allow this to age, just in case something is wrong, so we don't stay in the world forever
            ((ExperienceOrbAccessor) this).setAge(((ExperienceOrbAccessor) this).getAge() + 1);
            if (((ExperienceOrbAccessor) this).getAge() >= 6000) {
                this.discard();
            }
        } else if (!this.level().isClientSide) {
            super.tick();
        }
    }

    @Nullable
    public UUID getOwner() {
        return this.getEntityData().get(DATA_OWNER).orElse(null);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.getOwner() != null) {
            compound.putUUID("Owner", this.getOwner());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("Owner")) {
            this.getEntityData().set(DATA_OWNER, Optional.of(compound.getUUID("Owner")));
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        return new ClientboundAddEntityPacket(this, entity);
    }
}
