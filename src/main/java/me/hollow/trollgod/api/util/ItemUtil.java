package me.hollow.trollgod.api.util;

import me.hollow.trollgod.api.interfaces.Minecraftable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;

import java.util.HashMap;
import java.util.Map;

public class ItemUtil
implements Minecraftable {
    public static int getItemFromHotbar(Item item) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (ItemUtil.mc.player.inventory.getStackInSlot(i).getItem() != item) continue;
            slot = i;
            break;
        }
        return slot;
    }

    public static int getItemFromHotbar( Class < ItemSword > clazz) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (ItemUtil.mc.player.inventory.getStackInSlot(i).getItem().getClass() != clazz) continue;
            slot = i;
            break;
        }
        return slot;
    }

    public static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        return ItemUtil.getInventorySlots(9, 44);
    }

    private static Map<Integer, ItemStack> getInventorySlots(int currentI, int last) {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap <> ( );
        for (int current = currentI; current <= last; ++current) {
            fullInventorySlots.put(current, ItemUtil.mc.player.inventoryContainer.getInventory().get(current) );
        }
        return fullInventorySlots;
    }

    public static int getBlockFromHotbar(Block block) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (ItemUtil.mc.player.inventory.getStackInSlot(i).getItem() != Item.getItemFromBlock( block )) continue;
            slot = i;
            break;
        }
        return slot;
    }

    public static int getItemSlot(Class clss) {
        int itemSlot = -1;
        for (int i = 45; i > 0; --i) {
            if (ItemUtil.mc.player.inventory.getStackInSlot(i).getItem().getClass() != clss) continue;
            itemSlot = i;
            break;
        }
        return itemSlot;
    }

    public static int getItemSlot(Item item) {
        int itemSlot = -1;
        for (int i = 45; i > 0; --i) {
            if (!ItemUtil.mc.player.inventory.getStackInSlot(i).getItem().equals(item)) continue;
            itemSlot = i;
            break;
        }
        return itemSlot;
    }

    public static int getItemCount(Item item) {
        int count = 0;
        int size = ItemUtil.mc.player.inventory.mainInventory.size();
        for (int i = 0; i < size; ++i) {
            ItemStack itemStack = ItemUtil.mc.player.inventory.mainInventory.get(i);
            if (itemStack.getItem() != item) continue;
            count += itemStack.getCount();
        }
        ItemStack offhandStack = ItemUtil.mc.player.getHeldItemOffhand();
        if (offhandStack.getItem() == item) {
            count += offhandStack.getCount();
        }
        return count;
    }

    public static boolean isArmorLow(EntityPlayer player, int durability) {
        for (int i = 0; i < 4; ++i) {
            if (!(ItemUtil.getDamageInPercent( player.inventory.armorInventory.get(i) ) < (float)durability)) continue;
            return true;
        }
        return false;
    }

    public static int getItemDamage(ItemStack stack) {
        return stack.getMaxDamage() - stack.getItemDamage();
    }

    public static float getDamageInPercent(ItemStack stack) {
        float green = ((float)stack.getMaxDamage() - (float)stack.getItemDamage()) / (float)stack.getMaxDamage();
        float red = 1.0f - green;
        return 100 - (int)(red * 100.0f);
    }

    public static int getRoundedDamage(ItemStack stack) {
        return (int)ItemUtil.getDamageInPercent(stack);
    }

    public static boolean hasDurability(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ItemArmor || item instanceof ItemSword || item instanceof ItemTool || item instanceof ItemShield;
    }
}

