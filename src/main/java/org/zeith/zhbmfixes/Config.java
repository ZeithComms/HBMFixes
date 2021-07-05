package org.zeith.zhbmfixes;

import net.minecraftforge.common.config.Configuration;
import org.zeith.zhbmfixes.asm.ASM;

import java.io.File;
import java.util.Objects;

public class Config
{
	public static Configuration cfg;

	public static boolean disableMobEquipment;

	public static boolean[] overrides = new boolean[13];

	public static boolean doOverride(int i)
	{
		if(i >= 1 && i <= 13) return overrides[i - 1];
		return false;
	}

	public static void setup(File file)
	{
		cfg = new Configuration(file);
		reload();
	}

	public static void reload()
	{
		for(int i = 1; i <= 13; ++i)
		{
			String id = Objects.toString(ASM.getLoot(i));
			overrides[i - 1] = cfg.getBoolean(id, "Overrides", false, "Should HBM Fixes override the loot table for " + id + "?");
		}

		disableMobEquipment = cfg.getBoolean("Disable Mob Equipment", "Tweaks", true, "If enabled, prevents zombies and skeletons from weaing HBM's equipment.");

		if(cfg.hasChanged())
			cfg.save();
	}
}