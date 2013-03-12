package com.ov3rk1ll.nse;

import java.io.IOException;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import com.ov3rk1ll.nse.command.EditHeadCommand;
import com.ov3rk1ll.nse.command.EditSignCommand;




public class NamedSignEditPlugin extends JavaPlugin {
	
	public void onEnable() {
		saveDefaultConfig();
		getCommand("nse").setExecutor(new EditSignCommand(this));
		getCommand("namedsignedit").setExecutor(new EditSignCommand(this));
		
		getCommand("nhe").setExecutor(new EditHeadCommand(this));
		getCommand("namedheadedit").setExecutor(new EditHeadCommand(this));
		
		try {
		    MetricsLite metrics = new MetricsLite(this);
		    metrics.start();
		} catch (IOException e) {
		}
	}

	public void onDisable(){
		saveConfig();
	}
}
