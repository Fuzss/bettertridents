package fuzs.bettertridents.mixin.accessor;

import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemEntity.class)
public interface ItemEntityAccessor {

    @Accessor
    void setAge(int age);

    @Accessor
    @Mutable
    void setBobOffs(float bobOffs);
}
