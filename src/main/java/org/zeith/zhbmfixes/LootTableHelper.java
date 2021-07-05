package org.zeith.zhbmfixes;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.Random;

public class LootTableHelper
{
	/**
	 * Alternative to populating IInventory, this may populate the IItemHandlerModifiable with a use of a basic inventory.
	 */
	public static void populate(WorldServer world, LootTable loottable, IItemHandlerModifiable items)
	{
		Random random = new Random();
		LootContext.Builder lootcontext$builder = new LootContext.Builder(world);
		InventoryBasic inventory = new InventoryBasic("", false, items.getSlots());
		loottable.fillInventory(inventory, random, lootcontext$builder.build());
		for(int i = 0; i < items.getSlots(); ++i) items.setStackInSlot(i, inventory.getStackInSlot(i));
	}
}