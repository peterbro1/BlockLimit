package me.gmx.blocklimit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {
	
	private BlockLimit ins;
	public Commands(BlockLimit ins){
		this.ins=ins;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("limit")){
			
			
			
			if (!sender.hasPermission("blocklimit.commands")){
				sender.sendMessage(ins.prefix+"Invalid permissions");
				return false;
			}
			if (args.length==1 && args[0].equalsIgnoreCase("help")){
				sender.sendMessage(ChatColor.YELLOW + "---------------" + ChatColor.RED + "Block Limit v0.8 Made by GMX/Crystl" + ChatColor.YELLOW+"---------------");

				sender.sendMessage(ins.prefix+ChatColor.GREEN + "/limit add [block id]" + ChatColor.DARK_GREEN + " - adds a block to the limited blocks list");
				sender.sendMessage(ins.prefix+ChatColor.GREEN + "/limit check [player]" + ChatColor.DARK_GREEN + " - checks how many current blocks a player has");
				sender.sendMessage(ins.prefix+ChatColor.GREEN + "/limit remove [block id]" + ChatColor.DARK_GREEN + " - removes a block from the limited blocks list");
				sender.sendMessage(ins.prefix+ChatColor.GREEN + "/limit clear [player]" + ChatColor.DARK_GREEN + " - resets how many current blocks a player has");

			}
			if (args.length == 0){
				sender.sendMessage(ins.prefix + "Please use /limit help");
				return true;
			}
			if (args.length==2 && args[0].equalsIgnoreCase("add")){  //limit add [id]
				List<String> list = ins.getConfig().getStringList("block-id");
				list.add(args[1]);
				ins.getConfig().set("block-id", list);
				ins.saveConfig();
				sender.sendMessage(ins.prefix + "Added " + args[1] + " to blocks");
				return true;
				
			}else if (args.length==2 && args[0].equalsIgnoreCase("remove")){
				
				List<String> list = ins.getConfig().getStringList("block-id");
				list.remove(args[1]);
				ins.getConfig().set("block-id", list);
				ins.saveConfig();
				sender.sendMessage(ins.prefix + "Removed " + args[1] + " from blocks");
			}
			
			else if (args.length == 2 && args[0].equalsIgnoreCase("check")){

				if (args[1].equalsIgnoreCase("check")){
					Player p;
					try{
						p = Bukkit.getPlayer(args[2]);
						if (ins.getData().get(p.getName()+".locations") == null){
							sender.sendMessage(ins.prefix + "That player has no placed collectors.");
							return true;
						}
						
						sender.sendMessage(ins.prefix + ChatColor.AQUA + p.getName() + ChatColor.BLUE + " has placed " + ChatColor.GREEN + ((List<String>)ins.getData().get(p.getName()+".locations")).size());
						return true;
					}catch(Exception e){
						sender.sendMessage(ins.prefix + ChatColor.RED + "Player not found");
						return true;
					}
					
					
				}else if (args[1].equalsIgnoreCase("clear")){
					Player p;
					try {
						p = Bukkit.getPlayer(args[2]);
						ins.getData().set(p.getName()+".locations", new ArrayList<String>());
						ins.saveData();
						sender.sendMessage(ins.prefix+ChatColor.GREEN+"Success!");
						return true;
					}catch(Exception e){
						sender.sendMessage(ins.prefix+ChatColor.RED + "Player is not online!");
						return false;
					}
				}
			}else{
				sender.sendMessage(ins.prefix + ChatColor.RED + "Invalid syntax.");
				return true;
			}
			
			
			
			
			
			
			
			
			
		}
		
		
		return false;
	}
	
}
