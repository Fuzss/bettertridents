package fuzs.bettertridents.core;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;

public class ForgeAbstractions implements CommonAbstractions {

    @Override
    public boolean isValidTridentRepairItem(ItemStack itemStack, ItemStack itemStack2) {
        return itemStack2.is(Tags.Items.DUSTS_PRISMARINE);
    }
}
