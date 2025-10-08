package fuzs.bettertridents.world.entity.item;

import fuzs.bettertridents.handler.LoyalDropsHandler;
import fuzs.bettertridents.init.ModRegistry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class LoyalExperienceOrb extends ExperienceOrb {
    private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_OWNER = SynchedEntityData.defineId(
            LoyalExperienceOrb.class,
            EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    private static final EntityDataAccessor<Byte> DATA_LOYALTY = SynchedEntityData.defineId(LoyalExperienceOrb.class,
            EntityDataSerializers.BYTE);

    public LoyalExperienceOrb(Level level, double x, double y, double z, int value, @Nullable UUID owner, int loyaltyLevel) {
        super(ModRegistry.LOYAL_EXPERIENCE_ORB_ENTITY_TYPE.value(), level);
        this.setPos(x, y, z);
        this.setYRot((float) (this.random.nextDouble() * 360.0D));
        this.setDeltaMovement((this.random.nextDouble() * (double) 0.2F - (double) 0.1F) * 2.0D,
                this.random.nextDouble() * 0.2D * 2.0D,
                (this.random.nextDouble() * (double) 0.2F - (double) 0.1F) * 2.0D);
        this.getEntityData().set(DATA_VALUE, value);
        this.getEntityData().set(DATA_OWNER, Optional.ofNullable(owner).map(EntityReference::of));
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
        Player player = this.getOwnerReference()
                .map((EntityReference<LivingEntity> entityReference) -> entityReference.getEntity(this.level(),
                        LivingEntity.class))
                .filter(livingEntity -> EntitySelector.NO_SPECTATORS.test(livingEntity)
                        && livingEntity instanceof Player)
                .map(Player.class::cast)
                .orElse(null);
        if (player != null) {
            LoyalDropsHandler.tickLoyalEntity(this, player, this.getEntityData().get(DATA_LOYALTY));
            // allow this to age, just in case something is wrong, so we don't stay in the world forever
            this.age++;
            if (this.age >= 6000) {
                this.discard();
            }
        } else if (!this.level().isClientSide()) {
            super.tick();
        }
    }

    private Optional<EntityReference<LivingEntity>> getOwnerReference() {
        return this.getEntityData().get(DATA_OWNER);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput compound) {
        super.addAdditionalSaveData(compound);
        this.getOwnerReference().ifPresent((EntityReference<LivingEntity> entityReference) -> {
            entityReference.store(compound, "Owner");
        });

    }

    @Override
    protected void readAdditionalSaveData(ValueInput compound) {
        super.readAdditionalSaveData(compound);
        this.getEntityData().set(DATA_OWNER, Optional.ofNullable(EntityReference.read(compound, "Owner")));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        return new ClientboundAddEntityPacket(this, entity);
    }
}
