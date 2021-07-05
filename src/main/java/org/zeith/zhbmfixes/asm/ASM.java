package org.zeith.zhbmfixes.asm;

import com.hbm.handler.WeightedRandomChestContentFrom1710;
import com.hbm.lib.HbmChestContents;
import com.hbm.tileentity.machine.TileEntityCrateSteel;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.zeith.zhbmfixes.Config;
import org.zeith.zhbmfixes.LootTableHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ASM
{
	static final Map<WeightedRandomChestContentFrom1710[], Tuple<Integer, ResourceLocation>> cache = new HashMap<>();

	public static final ResourceLocation GENERIC_LOOT = LootTableList.register(new ResourceLocation("zhbmfixes", "generic"));
	public static final ResourceLocation ANTENNA_LOOT = LootTableList.register(new ResourceLocation("zhbmfixes", "antenna"));
	public static final ResourceLocation EXPENSIVE_LOOT = LootTableList.register(new ResourceLocation("zhbmfixes", "expensive"));
	public static final ResourceLocation NUKE_TRASH = LootTableList.register(new ResourceLocation("zhbmfixes", "nuke_trash"));
	public static final ResourceLocation NUCLEAR = LootTableList.register(new ResourceLocation("zhbmfixes", "nuclear"));
	public static final ResourceLocation VERTIBIRD = LootTableList.register(new ResourceLocation("zhbmfixes", "vertibird"));
	public static final ResourceLocation MISSILE_LOOT = LootTableList.register(new ResourceLocation("zhbmfixes", "missile"));
	public static final ResourceLocation SPACESHIP_LOOT = LootTableList.register(new ResourceLocation("zhbmfixes", "spaceship"));
	public static final ResourceLocation POWDER_LOOT = LootTableList.register(new ResourceLocation("zhbmfixes", "powder"));
	public static final ResourceLocation VAULT1_LOOT = LootTableList.register(new ResourceLocation("zhbmfixes", "vaults/1"));
	public static final ResourceLocation VAULT2_LOOT = LootTableList.register(new ResourceLocation("zhbmfixes", "vaults/2"));
	public static final ResourceLocation VAULT3_LOOT = LootTableList.register(new ResourceLocation("zhbmfixes", "vaults/3"));
	public static final ResourceLocation VAULT4_LOOT = LootTableList.register(new ResourceLocation("zhbmfixes", "vaults/4"));

	public static ResourceLocation getLoot(int i)
	{
		switch(i)
		{
			case 1:
				return GENERIC_LOOT;
			case 2:
				return ANTENNA_LOOT;
			case 3:
				return EXPENSIVE_LOOT;
			case 4:
				return NUKE_TRASH;
			case 5:
				return NUCLEAR;
			case 6:
				return VERTIBIRD;
			case 7:
				return MISSILE_LOOT;
			case 8:
				return SPACESHIP_LOOT;
			case 9:
				return POWDER_LOOT;
			case 10:
				return VAULT1_LOOT;
			case 11:
				return VAULT2_LOOT;
			case 12:
				return VAULT3_LOOT;
			case 13:
				return VAULT4_LOOT;
			default:
				return null;
		}
	}

	public static void generateChestContents(Random p_76293_0_, WeightedRandomChestContentFrom1710[] contents, ICapabilityProvider tile, int itemCount2Generate)
	{
		// dirty caching trick to map original HBM's loot "tables" to actual resource location of the real loot tables.
		if(cache.isEmpty())
			for(int i = 1; i <= 13; ++i) cache.put(HbmChestContents.getLoot(i), new Tuple<>(i, getLoot(i)));

		if(cache.containsKey(contents) && tile instanceof TileEntity)
		{
			TileEntity te = (TileEntity) tile;

			if(te instanceof TileEntityLockableLoot)
			{
				Tuple<Integer, ResourceLocation> loot = cache.get(contents);
				if(Config.doOverride(loot.getFirst()))
				{
					((TileEntityLockableLoot) te).setLootTable(loot.getSecond(), 0L);
					return;
				}
			} else if(te.getWorld() instanceof WorldServer)
			{
				Tuple<Integer, ResourceLocation> loot = cache.get(contents);
				if(Config.doOverride(loot.getFirst()))
				{
					LootTableManager manager = te.getWorld().getLootTableManager();

					LootTable table = manager.getLootTableFromLocation(loot.getSecond());

					if(table != LootTable.EMPTY_LOOT_TABLE && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
					{
						IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
						if(itemHandler instanceof IItemHandlerModifiable)
						{
							LootTableHelper.populate((WorldServer) te.getWorld(), table, (IItemHandlerModifiable) itemHandler);
							return;
						}
					}
				}
			}
		}

		if(tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
		{
			IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(inventory instanceof IItemHandlerModifiable)
			{
				WeightedRandomChestContentFrom1710.generateChestContents(p_76293_0_, contents, (IItemHandlerModifiable) inventory, itemCount2Generate);
			}
		}
	}
}