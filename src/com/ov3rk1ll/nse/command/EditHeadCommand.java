package com.ov3rk1ll.nse.command;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ov3rk1ll.nse.NamedSignEditPlugin;
import com.ov3rk1ll.nse.config.WorldLocation;

public class EditHeadCommand implements CommandExecutor {
	public static String TAG = "NamedHeadEdit";
	public static String CHATTAG = "[" + TAG + "]";

	private final NamedSignEditPlugin plugin;

	public EditHeadCommand(NamedSignEditPlugin plugin)
	{
		this.plugin = plugin;
	}

	public Player resolveToPlayer(CommandSender s){
		return Bukkit.getServer().getPlayer(s.getName());
	}

	/**
	 * nse set [name] [line] [text]
	 * nse name [name] [world] [x] [y] [z]
	 * nse clean [name] <line>
	 * nse x [name] [line] [text]
	 * 
	 */
	public boolean onCommand(CommandSender s, Command c, String l, String[] args){	
		if(!s.isOp()){
			s.sendMessage(ChatColor.RED + CHATTAG + " You must be op to run this command!");	
			return true;
		}
		if(args[0].equals("set") || args[0].equals("s")){
			if(args.length < 3){
				return false;
			}
			setNamedHead(args[1], args[2], s);
			return true;
		} else if(args[0].equals("name") || args[0].equals("n")){
			if(args.length == 2){ // We should be a player and looking at a head
				Player p = resolveToPlayer(s);
				if(!(p instanceof Player)){
					s.sendMessage(ChatColor.RED + CHATTAG + " You are not a player. Please provide the head's location!");	
					return false;				
				}
				Block b = p.getTargetBlock(null, 200);
				if(b.getTypeId() != Material.SKULL.getId() && b.getTypeId() != Material.SKULL_ITEM.getId()){
					s.sendMessage(ChatColor.RED + CHATTAG + " You are not looking at a head! (" + Material.getMaterial(b.getTypeId()).name() + ")");	
					return false;									
				}
				this.plugin.getConfig().set("heads." + args[1], new WorldLocation(p.getWorld().getName(), b.getLocation()).toString());
				this.plugin.saveConfig();
				s.sendMessage(ChatColor.GREEN + CHATTAG + " Named head!");
				return true;
			} else {
				this.plugin.getConfig().set("heads." + args[1], new WorldLocation(args[2], Integer.valueOf(args[3]), Integer.valueOf(args[4]), Integer.valueOf(args[5])).toString());
				this.plugin.saveConfig();
				s.sendMessage(ChatColor.GREEN + CHATTAG + " Named head!");
				return true;
			}
		} else if(args[0].equals("clean") || args[0].equals("clear") || args[0].equals("c")){
			setNamedHead(args[1], "", s);
			return true;
		} else if(args[0].equals("remove") || args[0].equals("r")){
			this.plugin.getConfig().set("heads." + args[1], null);
			this.plugin.saveConfig();
			s.sendMessage(ChatColor.RED + CHATTAG + " Head has been removed!");
			return true;
		} else if(args[0].equals("x")){
			Player p = resolveToPlayer(s);
			Block b = p.getTargetBlock(null, 200);
			if(b.getTypeId() != Material.SKULL.getId() && b.getTypeId() != Material.SKULL_ITEM.getId()){
				s.sendMessage(ChatColor.RED + CHATTAG + " You are not looking at a Head! (" + Material.getMaterial(b.getTypeId()).name() + ")");	
				return false;									
			}
			setHeadAt(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ(), p.getWorld().getName(), args[1], s);
			return true;
		}

		return false;
	}
	
	private void setNamedHead(String name, String player, CommandSender s){
		String configBase = "heads." + name;
		if(this.plugin.getConfig().contains(configBase) == false){
			s.sendMessage(ChatColor.RED + CHATTAG + " Unknown head name!");
			return;
		}
		WorldLocation location = new WorldLocation(this.plugin.getConfig().getString(configBase));
		setHeadAt(location.getX(), location.getY(), location.getZ(), location.getWorld(), player, s);
	}
	
	private void setHeadAt(int x, int y, int z, String world, String player, CommandSender s){
		try{
			Block block = Bukkit.getWorld(world).getBlockAt(x, y, z);
			BlockState stateBlock = block.getState();
	        Skull skull = (Skull) stateBlock;        
	        skull.setOwner(player);
	        skull.setSkullType(SkullType.PLAYER);
	        skull.update(true);
	        s.sendMessage(ChatColor.GREEN + CHATTAG + " set head to " + ChatColor.YELLOW + player + ChatColor.GREEN + " at " + x + "," + y + "," + z + " in " + world);
		} catch (ClassCastException e){
			s.sendMessage(ChatColor.RED + CHATTAG + " no head found at " + x + "," + y + "," + z + " in " + world);
			s.sendMessage(ChatColor.RED + CHATTAG + " replace the head or use /nhe remove <name> to remove the head");
		}
	}
}