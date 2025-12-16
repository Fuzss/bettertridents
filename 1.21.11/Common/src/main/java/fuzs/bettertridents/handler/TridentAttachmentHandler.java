package fuzs.bettertridents.handler;

import fuzs.bettertridents.init.ModRegistry;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class TridentAttachmentHandler {

    public static EventResult onLivingDeath(LivingEntity entity, DamageSource damageSource) {
        ModRegistry.LAST_DAMAGE_SOURCE_ATTACHMENT_TYPE.set(entity, damageSource);
        return EventResult.PASS;
    }

    public static EventResult onLivingDrops(LivingEntity entity, DamageSource damageSource, Collection<ItemEntity> drops, boolean recentlyHit) {
        ModRegistry.LAST_DAMAGE_SOURCE_ATTACHMENT_TYPE.set(entity, null);
        return EventResult.PASS;
    }

    public static boolean addItemToInventory(ThrownTrident thrownTrident, Player player, ItemStack itemStack) {
        int tridentSlot = ModRegistry.TRIDENT_SLOT_ATTACHMENT_TYPE.getOrDefault(thrownTrident, -1);
        Inventory inventory = player.getInventory();
        int slot = findSlotAtIndex(inventory, tridentSlot);
        if (slot != -1) {
            inventory.setItem(slot, itemStack.copy());
            inventory.getItem(slot).setPopTime(5);
            itemStack.setCount(0);
            return true;
        } else {
            return inventory.add(itemStack);
        }
    }

    private static int findSlotAtIndex(Inventory inventory, int slot) {
        if (slot != -1 && inventory.getItem(slot).isEmpty()) {
            return slot;
        } else if (inventory.getSelectedItem().isEmpty()) {
            // try to return to the main hand as a secondary option
            return inventory.getSelectedSlot();
        } else {
            return -1;
        }
    }
}
