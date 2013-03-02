package com.ov3rk1ll.nse.command;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.ov3rk1ll.nse.NamedSignEditPlugin;

public class EditSignCommand implements CommandExecutor {

	private final NamedSignEditPlugin plugin;

	public EditSignCommand(NamedSignEditPlugin plugin)
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
			return false;
		}
		if(args[0].equals("set")){
			if(args.length < 3){
				return false;
			}
			String text = "";
			for(int i = 3; i < args.length; i++){
				text += args[i] + " ";
			}
			text = text.substring(0, text.length() - 1);
			setNamedSign(args[1], Integer.valueOf(args[2]), text, s);
			return true;
		} else if(args[0].equals("name")){
			if(args.length == 2){ // We should be a player and looking at a sign
				Player p = resolveToPlayer(s);
				if(!(p instanceof Player)){
					s.sendMessage(ChatColor.RED + NamedSignEditPlugin.CHATTAG + " You are not a player. Please provide the sign's location!");	
					return false;				
				}
				Block b = p.getTargetBlock(null, 200);
				if(b.getTypeId() != Material.SIGN_POST.getId() && b.getTypeId() != Material.WALL_SIGN.getId()){
					s.sendMessage(ChatColor.RED + NamedSignEditPlugin.CHATTAG + " You are not looking at a sign! (" + Material.getMaterial(b.getTypeId()).name() + ")");	
					return false;									
				}
				String configBase = "signs." + args[1];
				this.plugin.getConfig().set(configBase + ".world", p.getWorld().getName());
				Vector v = new Vector(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ());
				this.plugin.getConfig().set(configBase + ".location", v);
				this.plugin.saveConfig();
				s.sendMessage(ChatColor.GREEN + NamedSignEditPlugin.CHATTAG + " Named sign!");
				return true;
			} else {
				String configBase = "signs." + args[1];
				this.plugin.getConfig().set(configBase + ".world", args[2]);
				Vector v = new Vector(Integer.valueOf(args[3]), Integer.valueOf(args[4]), Integer.valueOf(args[5]));
				this.plugin.getConfig().set(configBase + ".location", v);
				this.plugin.saveConfig();
				s.sendMessage(ChatColor.GREEN + NamedSignEditPlugin.CHATTAG + " Named sign!");
				return true;
			}
		} else if(args[0].equals("clean") || args[0].equals("clear")){
			if(args.length < 2){
				return false;
			} else if(args.length == 2){
				setNamedSign(args[1], 0, "", s);
				setNamedSign(args[1], 1, "", s);
				setNamedSign(args[1], 2, "", s);
				setNamedSign(args[1], 3, "", s);
			} else if(args.length == 3){
				setNamedSign(args[1], Integer.valueOf(args[2]), "", s);
			}
			return true;
		} else if(args[0].equals("remove")){
			String configBase = "signs." + args[1];
			this.plugin.getConfig().set(configBase, null);
			this.plugin.saveConfig();
			s.sendMessage(ChatColor.RED + NamedSignEditPlugin.CHATTAG + " Sign has been removed!");
			return true;
		} else if(args[0].equals("x")){
			Player p = resolveToPlayer(s);
			Block b = p.getTargetBlock(null, 200);
			if(b.getTypeId() != Material.SIGN_POST.getId() && b.getTypeId() != Material.WALL_SIGN.getId()){
				s.sendMessage(ChatColor.RED + NamedSignEditPlugin.CHATTAG + " You are not looking at a sign! (" + Material.getMaterial(b.getTypeId()).name() + ")");	
				return false;									
			}
			String text = "";
			if(args.length > 2){
				for(int i = 2; i < args.length; i++){
					text += args[i] + " ";
				}
				text = text.substring(0, text.length() - 1);
			}
			setSignLineAt(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ(), p.getWorld().getName(), Integer.valueOf(args[1]), text, s);
			return true;
		}

		return false;
	}
	
	private void setNamedSign(String name, int line, String text, CommandSender s){
		String configBase = "signs." + name;
		if(this.plugin.getConfig().contains(configBase) == false){
			s.sendMessage(ChatColor.RED + NamedSignEditPlugin.CHATTAG + " Unknown sign name!");
			return;
		}
		String world = this.plugin.getConfig().getString(configBase + ".world");
		Vector v = this.plugin.getConfig().getVector(configBase + ".location");
		setSignLineAt(v.getBlockX(), v.getBlockY(), v.getBlockZ(), world, line, text, s);
	}
	
	private void setSignLineAt(int x, int y, int z, String world, int line, String text, CommandSender s){
		try{
			Block block = Bukkit.getWorld(world).getBlockAt(x, y, z);
			BlockState stateBlock = block.getState();
	        Sign sign = (Sign) stateBlock;        
	        sign.setLine(line, convertColors(text));
	        sign.update(true);
	        s.sendMessage(ChatColor.GREEN + NamedSignEditPlugin.CHATTAG + " set line " + line + " to " + ChatColor.YELLOW + text + ChatColor.GREEN + " at " + x + "," + y + "," + z + " in " + world);
		} catch (ClassCastException e){
			s.sendMessage(ChatColor.RED + NamedSignEditPlugin.CHATTAG + " no sign found at " + x + "," + y + "," + z + " in " + world);
			s.sendMessage(ChatColor.RED + NamedSignEditPlugin.CHATTAG + " replace the sign or use /nse remove <name> to remove the sign");
		}
	}
	
	private String convertColors(String text){
		text = text.replace("&0", ChatColor.BLACK.toString());
		text = text.replace("&1", ChatColor.DARK_BLUE.toString());
		text = text.replace("&2", ChatColor.DARK_GREEN.toString());
		text = text.replace("&3", ChatColor.DARK_AQUA.toString());
		text = text.replace("&4", ChatColor.DARK_RED.toString());
		text = text.replace("&5", ChatColor.DARK_PURPLE.toString());
		text = text.replace("&6", ChatColor.GOLD.toString());
		text = text.replace("&7", ChatColor.GRAY.toString());
		text = text.replace("&8", ChatColor.DARK_GRAY.toString());
		text = text.replace("&9", ChatColor.BLUE.toString());
		text = text.replace("&a", ChatColor.GREEN.toString());
		text = text.replace("&b", ChatColor.AQUA.toString());
		text = text.replace("&c", ChatColor.RED.toString());
		text = text.replace("&d", ChatColor.LIGHT_PURPLE.toString());
		text = text.replace("&e", ChatColor.YELLOW.toString());
		text = text.replace("&f", ChatColor.WHITE.toString());
		text = text.replace("&k", ChatColor.MAGIC.toString());
		text = text.replace("&l", ChatColor.BOLD.toString());
		text = text.replace("&m", ChatColor.STRIKETHROUGH.toString());
		text = text.replace("&n", ChatColor.UNDERLINE.toString());
		text = text.replace("&o", ChatColor.ITALIC.toString());
		text = text.replace("&r", ChatColor.RESET.toString());
		
		return text;
	}
}