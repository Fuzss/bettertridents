package fuzs.bettertridents.mixin.accessor;

import net.minecraft.world.entity.ExperienceOrb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ExperienceOrb.class)
public interface ExperienceOrbAccessor {

    @Accessor
    void setValue(int value);

    @Accessor
    void setAge(int age);

    @Accessor
    int getAge();
}
