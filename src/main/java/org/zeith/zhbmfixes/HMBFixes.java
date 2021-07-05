package org.zeith.zhbmfixes;

import com.hbm.items.ModItems;
import com.hbm.main.ModEventHandler;
import com.zeitheron.hammercore.HammerCore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(modid = HMBFixes.MOD_ID, name = HMBFixes.MOD_NAME, version = "@VERSION@", dependencies = "required-after:hbm", certificateFingerprint = "@FINGERPRINT@", updateJSON = "http://dccg.herokuapp.com/api/fmluc/@CF_ID@")
public class HMBFixes
{
	public static final String MOD_ID = "zhbmfixes";
	public static final String MOD_NAME = "HBM Fixes";

	public static final Logger LOG = LogManager.getLogger();

	@Mod.EventHandler
	public void certificateViolation(FMLFingerprintViolationEvent e)
	{
		LOG.warn("*****************************");
		LOG.warn("WARNING: Somebody has been tampering with " + HMBFixes.MOD_NAME + " jar!");
		LOG.warn("It is highly recommended that you redownload mod from https://www.curseforge.com/projects/@CF_ID@ !");
		LOG.warn("*****************************");
		HammerCore.invalidCertificates.put(HMBFixes.MOD_ID, "https://www.curseforge.com/projects/@CF_ID@");
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		Config.setup(e.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void mobSpawn(LivingSpawnEvent event)
	{
		if(Config.disableMobEquipment) return;

		EntityLivingBase entity = event.getEntityLiving();
		World world = event.getWorld();
		Random rand = ModEventHandler.rand;

		if(entity instanceof EntityZombie)
		{
			if(rand.nextInt(64) == 0)
			{
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ModItems.gas_mask_m65, 1, world.rand.nextInt(100)));
			}

			if(rand.nextInt(128) == 0)
			{
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ModItems.gas_mask, 1, world.rand.nextInt(100)));
			}

			if(rand.nextInt(256) == 0)
			{
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ModItems.mask_of_infamy, 1, world.rand.nextInt(100)));
			}

			if(rand.nextInt(1024) == 0)
			{
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(ModItems.starmetal_plate, 1, world.rand.nextInt(ModItems.starmetal_plate.getMaxDamage(ItemStack.EMPTY))));
			}

			if(rand.nextInt(128) == 0)
			{
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.pipe_lead, 1, world.rand.nextInt(100)));
			}

			if(rand.nextInt(128) == 0)
			{
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.reer_graar, 1, world.rand.nextInt(100)));
			}

			if(rand.nextInt(128) == 0)
			{
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.pipe_rusty, 1, world.rand.nextInt(100)));
			}

			if(rand.nextInt(128) == 0)
			{
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.crowbar, 1, world.rand.nextInt(100)));
			}

			if(rand.nextInt(128) == 0)
			{
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.geiger_counter, 1));
			}

			if(rand.nextInt(128) == 0)
			{
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.steel_pickaxe, 1, world.rand.nextInt(300)));
			}

			if(rand.nextInt(512) == 0)
			{
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.stopsign));
			}

			if(rand.nextInt(512) == 0)
			{
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.sopsign));
			}

			if(rand.nextInt(512) == 0)
			{
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.chernobylsign));
			}
		}

		if(entity instanceof EntitySkeleton && rand.nextInt(16) == 0)
		{
			entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ModItems.gas_mask_m65, 1, world.rand.nextInt(100)));
			if(rand.nextInt(32) == 0)
			{
				entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.syringe_poison));
			}
		}
	}
}