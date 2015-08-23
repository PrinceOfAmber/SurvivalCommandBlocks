package com.lothrazar.survivalcommandblocks;

import org.apache.logging.log4j.Logger; 

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
 
@Mod(modid = ModSamsContent.MODID, useMetadata = true) 
public class ModSamsContent
{
	//forked from my old repo: 
	//https://github.com/PrinceOfAmber/SamsPowerups/commit/00a32f4a16739c307cf3c6149d2417dfff7ea3f3
	
	@Instance(value = ModSamsContent.MODID)
	public static ModSamsContent instance;
	public static Logger logger;
	public final static String MODID = "survivalcommandblocks";
	 public static String TEXTURE_LOCATION = MODID+":";

	//public static ConfigFile settings;
	
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{ 
		logger = event.getModLog();
		 
		//settings = new ConfigFile(new Configuration(event.getSuggestedConfigurationFile()));
 
		MinecraftForge.EVENT_BUS.register(instance);
		FMLCommonHandler.instance().bus().register(instance);
	}

	@EventHandler
	public void onInit(FMLInitializationEvent event)
	{     
		BlockRegistry.init();  
	}

	 public static void registerBlockHelper(Block s, String name)
	 {
		 s.setBlockName(name).setBlockTextureName(TEXTURE_LOCATION + name);
		 GameRegistry.registerBlock(s, name);
		 
	 }
	 public static void registerItemHelper(Item s, String name)
	 {
		 s.setUnlocalizedName(name).setTextureName(TEXTURE_LOCATION + name);
		 GameRegistry.registerItem(s, name);
	 }
	 
}
