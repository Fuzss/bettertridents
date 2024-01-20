package fuzs.bettertridents.core;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FabricAbstractions implements CommonAbstractions {

    @Override
    public boolean isValidTridentRepairItem(ItemStack itemStack, ItemStack itemStack2) {
        return itemStack2.is(Items.PRISMARINE_SHARD);
    }
}
