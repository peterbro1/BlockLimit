package me.gmx.blocklimit;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockLimit extends JavaPlugin{
	public static String prefix;
	private FileConfiguration data_fc;
	private File data_f;
	Logger log = Logger.getLogger("Minecraft");
	private BlockLimit ins;
	
	public void onEnable(){
	    createConfig();
	    saveConfig();
	    log.log(Level.INFO, String.format("[%s] Successfully enabled version %s!", new Object[] { getDescription().getName(), getDescription().getVersion() }));
	    ins = this;
	    prefix = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Block limit" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET;
	    Bukkit.getPluginManager().registerEvents(new StandardListener(ins), this);
		getCommand("limit").setExecutor(new Commands(ins));

	}
	private void createConfig() {
	      try {
	          if (!getDataFolder().exists()) {
	              getDataFolder().mkdirs();
	          }
	          File file = new File(getDataFolder(), "config.yml");
	          if (!file.exists()) {
	        	  
	        	  saveDefaultConfig();
	              getConfig().options().copyDefaults();
	              saveConfig();
	          } else {
	              getLogger().info("Config.yml found, loading!");
	          }
	          
	          if (getConfig().getInt("limit.member") == 0){
	        	  Bukkit.broadcastMessage("setting member limit 30");
	        	  getConfig().set("limit.member", 30);
	        	  
	          }
	          data_f = new File(getDataFolder(),"data.yml");
	          //creating players.yml
	  		if (!data_f.exists()){
	  			data_f.getParentFile().mkdirs();
	            saveResource("data.yml", false);

	  		}
	  		data_fc = new YamlConfiguration();
	  		
	  			data_fc.load(data_f);
	  			saveData();
	  			
	      }catch(Exception e){
	    	  e.printStackTrace();
	      }
	}
	
			public FileConfiguration getData(){
				return this.data_fc;
			}
	      
	      public void saveData(){
	    	  try{
	    		  data_fc.save(data_f);
	    	  }catch(Exception e){
	    		  log.log(Level.WARNING, ins.prefix + "FUCK");
	    		  
	    	  }
	      }
	
	
	public void onDisable(){
		saveData();
		saveConfig();
		//saveDefaultConfig();
		
	    log.log(Level.INFO, String.format("[%s] Successfully disabled version %s!", new Object[] { getDescription().getName(), getDescription().getVersion() }));

	}
	
	

}
