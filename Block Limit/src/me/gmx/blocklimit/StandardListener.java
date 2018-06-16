package me.gmx.blocklimit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.gmx.utils.FileUtil;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_7_R4.Block;

public class StandardListener implements Listener {
	
	private BlockLimit ins;
	
	public StandardListener(BlockLimit ins){
		this.ins = ins;
		
	}
	
	
	@EventHandler
	public void onPlaceBlock(BlockPlaceEvent e){
		if (e.getPlayer().hasPermission("blocklimit.bypass") || e.getPlayer().isOp()){
			return;
		}
		if (ins.getConfig().getStringList("block-id").contains(Integer.toString(e.getBlock().getTypeId()))){
			//Bukkit.broadcastMessage(ins.prefix+ "Collector placed by: " + e.getPlayer());
				if (e.getPlayer()==null || e.getPlayer().getName().equals("[PR_FAKE]") || e.getPlayer().getName().equals("[TubeStuff]") || e.getPlayer().getName().equals("[AppliedEnergistics2")){
					e.setCancelled(true);
					return;
				}
			List<String> loc;
			try{
				loc = (List<String>) ins.getData().get(e.getPlayer().getName() + ".locations");
			}catch(NullPointerException ex){
				
				loc = new ArrayList<String>();
			}
			if (ins.getData().get(e.getPlayer().getName() + ".locations")==null){
				loc = new ArrayList<String>();
			}else{
				loc = ins.getData().getStringList(e.getPlayer().getName()+".locations");
			}
			int n = loc.size();
			if ((int)ins.getConfig().getInt("limit.member") <= n){
							e.getPlayer().sendMessage(ins.prefix+ChatColor.RED + "You cannot place any more " + ChatColor.DARK_RED + Block.getById(e.getBlock().getTypeId()).getName());
							e.setCancelled(true);
							return;
						}
			loc.add(FileUtil.getStringLocation(e.getBlock().getLocation()));
			ins.getData().set(e.getPlayer().getName().toString() + ".locations", loc);
			ins.saveData();
			
			
			
			e.getPlayer().sendMessage(ins.prefix+"You have placed a " + ChatColor.GREEN + 
					Block.getById(e.getBlock().getTypeId()).getName() + " you can place " +  Integer.toString(ins.getConfig().getInt("limit.member") - n - 1) + " more.");
			
		}
		
		
		
		
		
		
		
	}
	
	@EventHandler
	public void onDestroyBlock(BlockBreakEvent e){
		if (ins.getConfig().getStringList("block-id").contains(Integer.toString(e.getBlock().getTypeId()))){
			if (e.getPlayer().hasPermission("blocklimit.bypass") || e.getPlayer().isOp()){
				return;
			}
			
			for (String s : ins.getData().getStringList(e.getPlayer().getName() + ".locations")){
				if (s.equals(FileUtil.getStringLocation(e.getBlock().getLocation()))){
					List<String> loc = ins.getData().getStringList(e.getPlayer().getName()+ ".locations");
					loc.remove(s);
					ins.getData().set(e.getPlayer().getName()+".locations", loc);
					ins.saveData();
					return;
				}
				
			}
			e.getPlayer().sendMessage(ins.prefix+ ChatColor.RED+ "This does not belong to you!");
			e.setCancelled(true);
			return;
		}
	}
	
	
}
