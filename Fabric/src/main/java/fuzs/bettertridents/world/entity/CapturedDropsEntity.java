package fuzs.bettertridents.world.entity;

import net.minecraft.world.entity.item.ItemEntity;

import java.util.Collection;

public interface CapturedDropsEntity {

    Collection<ItemEntity> custom$setCapturedDrops(Collection<ItemEntity> collection);

    Collection<ItemEntity> custom$getCapturedDrops();
}
