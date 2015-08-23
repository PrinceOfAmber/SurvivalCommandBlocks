package com.lothrazar.survivalcommandblocks;

import java.util.Random; 

import cpw.mods.fml.common.registry.GameRegistry; 
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.world.World;

public class BlockCommandBlockCraftable extends BlockCommandBlock
{ 
	public static enum CommandType
	{
		Teleport, Gamerule, Weather
	}
	
	private CommandType type;
	private String rule = null;
	
	private void setConstructorDefaults()
	{ 
		this.setHardness(3F);
		this.setResistance(5F);
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}
	
	public BlockCommandBlockCraftable(CommandType t)
	{   
		type = t;
		this.rule = null;
		setConstructorDefaults(); 
	}
	
	public BlockCommandBlockCraftable(CommandType t, String rl)
	{     
		type = t;
		this.rule = rl;
		setConstructorDefaults();
	}
	 
	@Override
	public void onBlockClicked(World w, int x, int y, int z, EntityPlayer p) 
	{  
		super.onBlockClicked( w,  x,  y,  z,  p) ;
	}

	@Override
	public void updateTick(World w, int x, int y, int z, Random r)
    {  
		//this fires on redstone power 
        TileEntity tileentity = w.getTileEntity(x, y, z); 
        if (tileentity == null ) {return;}
        if(!(tileentity instanceof TileEntityCommandBlock)) {return;}
     
        String command = null;   //set the command of the block as a string, just as a player would type it

        switch(type)
        {
	        case Teleport:
	        	int _x = w.getWorldInfo().getSpawnX();
	    		int _y = w.getWorldInfo().getSpawnY();
	    		int _z = w.getWorldInfo().getSpawnZ();
	    		
	    		//try to find air block up from 64. since world spawn is usually fixed at 64.
	    		boolean inWall = true;
	    		Block current;
	    		while(inWall && _y < 200)
	    		{
	    			current = w.getBlock(_x, _y, _z); 
	    			
	    			if(current == Blocks.air) 
	    			{
	    				inWall = false;
	    			}
	    			else 
	    			{
	    				_y++; 
	    			}
	    			//either we are out in open air, or we have moved up one block so loop again
	    		}
	    		
	    		command = "tp @p " + _x +  " "+_y+" "+_z;
	        break; 
	        case Gamerule:
	        	
	        	String lastVal = w.getGameRules().getGameRuleStringValue(rule); 
	
	        	//toggle it based on previous value
				lastVal = (lastVal.equals("false")) ? "true" : "false";  
	
				//Chat.addMessage(w, rule+" = "+lastVal);  
		        
	            command = "gamerule "+ rule +" "+lastVal;
	            
	        	break;
	        case Weather:
	        	
	        	command = "toggledownfall";
	        	
	        break;
        }
         
        //in 1.8 snapshot, we will use execute possibly?
       // commandblocklogic.func_145752_a("/execute @p "+x+" "+y+" "+z+" toggledownfall");
         
        if(command != null)
        {
        	command = "/"+command;
	        CommandBlockLogic commandblocklogic = ((TileEntityCommandBlock)tileentity).func_145993_a();
	         
	        commandblocklogic.func_145752_a(command); //set current command into this CommandClock
	        
	        //execute my current command in the World
	        commandblocklogic.func_145755_a(w);
	        w.func_147453_f(x, y, z, this);
        }
    }
	  
	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    { 
		//disables the player from opening the edit screen to alter the command
		return false;
    }
	
	@Override 
	public int quantityDropped(Random p_149745_1_)
    {
		//change from 0 to 1 so it is harvestable
        return 4;
    }
	
	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
       return Item.getItemFromBlock(Blocks.redstone_block);//force them to use silk touch to get it back
    }
	
	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
    {
		return true;
    }

	static BlockCommandBlockCraftable command_block_regen;
	static BlockCommandBlockCraftable command_block_firetick;
	static BlockCommandBlockCraftable command_block_daycycle;
	static BlockCommandBlockCraftable command_block_mobgrief;
	static BlockCommandBlockCraftable command_block_weather;
	public static void init()
	{ 
		command_block_regen = new BlockCommandBlockCraftable( CommandType.Gamerule, "naturalRegeneration");
		ModSamsContent.registerBlockHelper(command_block_regen,"command_block_regen");
		addRecipe(command_block_regen,new ItemStack(Items.golden_apple)); 
  
		command_block_mobgrief = new BlockCommandBlockCraftable(CommandType.Gamerule, "mobGriefing");
		ModSamsContent.registerBlockHelper(command_block_mobgrief,"command_block_mobgrief");
		addRecipe(command_block_mobgrief,new ItemStack(Blocks.tnt)); 

		command_block_firetick = new BlockCommandBlockCraftable(CommandType.Gamerule, "doFireTick"); 
		ModSamsContent.registerBlockHelper(command_block_firetick,"command_block_firetick");
		addRecipe(command_block_firetick,new ItemStack( Items.lava_bucket)); 
 
		command_block_daycycle = new BlockCommandBlockCraftable(CommandType.Gamerule,"doDaylightCycle");
		ModSamsContent.registerBlockHelper(command_block_daycycle,"command_block_daycycle");
		addRecipe(command_block_daycycle,new ItemStack(Blocks.glowstone)); 
  
		command_block_weather = new BlockCommandBlockCraftable(CommandType.Weather); 
		ModSamsContent.registerBlockHelper(command_block_weather,"command_block_weather"); 
		addRecipe(command_block_weather,new ItemStack(Items.water_bucket)); 
	}
	
	private static void addRecipe(BlockCommandBlockCraftable output, ItemStack center)
	{ 
		GameRegistry.addRecipe(new ItemStack(output), 
				"rcr", 
				"tet",
				"rcr", 
				'c', Items.comparator, 
				'e', center, 
				'r', Blocks.redstone_block, 
				't', Items.ghast_tear);
	}
}
